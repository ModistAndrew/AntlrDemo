package modist.antlrdemo.backend;

import modist.antlrdemo.backend.asm.BlockAsm;
import modist.antlrdemo.backend.asm.FunctionAsm;
import modist.antlrdemo.backend.asm.InstructionAsm;
import modist.antlrdemo.backend.metadata.Opcode;
import modist.antlrdemo.backend.metadata.Register;
import modist.antlrdemo.frontend.ir.metadata.IrRegister;
import modist.antlrdemo.frontend.ir.metadata.IrType;
import modist.antlrdemo.frontend.ir.node.BlockIr;
import modist.antlrdemo.frontend.ir.node.FunctionIr;
import modist.antlrdemo.frontend.ir.node.InstructionIr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionBuilder {
    private static final int STACK_ALIGN = 16;
    private final FunctionAsm current;
    // every ir register is assigned with a stack location relative to function stack pointer
    private final List<Register> savedRegisters;
    private final int stackSize;
    private int stackTop;
    // the current stack size(in bytes)
    // saved registers should be pushed to the stack
    // one ir register occupies one stack slot(including parameters)
    // alloc instruction may enlarge the stack size
    // function call with too many arguments may enlarge the stack size
    // stackSize should be aligned
    // ir registers use the stack from the top to the bottom; function parameters use the stack from the bottom to the top
    private final Map<IrRegister, Integer> irRegisters = new HashMap<>();

    // (new -> add* -> (newBlock -> add* ->)* build)*
    public FunctionBuilder(FunctionIr function) {
        current = new FunctionAsm(function.name);
        savedRegisters = List.of(Register.RA); // only ra needs to be saved currently
        stackSize = calculateStackSize(function);
        stackTop = stackSize;
        // push stack pointer
        add(new InstructionAsm.BinImm(Register.SP, Opcode.ADDI, Register.SP, -stackSize));
        // save saved registers
        for (int i = 0; i < savedRegisters.size(); i++) {
            add(new InstructionAsm.Sw(savedRegisters.get(i), stackSize - Register.BYTE_SIZE * (i + 1), Register.SP));
        }
        stackTop -= Register.BYTE_SIZE * savedRegisters.size(); // the top of the stack is occupied by saved registers
        // store parameters
        for (int i = 0; i < function.parameters.size(); i++) {
            IrRegister parameter = function.parameters.get(i);
            if (i < Register.ARG_REGISTERS.length) {
                add(new InstructionAsm.Sw(Register.ARG_REGISTERS[i], allocRegister(parameter), Register.SP));
            } else {
                // a trick here: we needn't allocate a stack slot for the parameter but use the original stack slot
                // note that sp is already decremented by stackSize; we need to add stackSize back
                irRegisters.put(parameter, stackSize + IrType.MAX_BYTE_SIZE * (i - Register.ARG_REGISTERS.length));
            }
        }
    }

    // we have to calculate the stack size before building the function so that stack pointer won't be changed during the process
    // must be careful that this corresponds to calls that change the stack pointer
    private int calculateStackSize(FunctionIr function) {
        int size = savedRegisters.size() * Register.BYTE_SIZE;
        size += IrType.MAX_BYTE_SIZE * Math.min(Register.ARG_REGISTERS.length, function.parameters.size());
        int maxFunctionCallParameters = 0;
        for (BlockIr block : function.body) {
            for (InstructionIr instruction : block.instructions) {
                if (instruction instanceof InstructionIr.Result result && result.result() != null) {
                    size += IrType.MAX_BYTE_SIZE;
                }
                if (instruction instanceof InstructionIr.Alloc) {
                    size += IrType.MAX_BYTE_SIZE;
                }
                if (instruction instanceof InstructionIr.FunctionCall functionCall) {
                    maxFunctionCallParameters = Math.max(maxFunctionCallParameters, functionCall.arguments().size());
                }
            }
        }
        size += Math.max(maxFunctionCallParameters - Register.ARG_REGISTERS.length, 0) * IrType.MAX_BYTE_SIZE;
        size = (size + STACK_ALIGN - 1) / STACK_ALIGN * STACK_ALIGN;
        return size;
    }

    public void add(InstructionAsm instruction) {
        current.blocks.getLast().instructions.add(instruction);
    }

    public Register add(InstructionAsm.Result instruction) {
        current.blocks.getLast().instructions.add(instruction);
        return instruction.result();
    }

    public void newBlock(String name) {
        current.blocks.add(new BlockAsm(renameLabel(name)));
    }

    public String renameLabel(String name) {
        return current.name + "." + name;
    }

    public FunctionAsm build() {
        // load saved registers
        for (int i = 0; i < savedRegisters.size(); i++) {
            add(new InstructionAsm.Lw(savedRegisters.get(i), stackSize - Register.BYTE_SIZE * (i + 1), Register.SP));
        }
        // pop stack pointer
        add(new InstructionAsm.BinImm(Register.SP, Opcode.ADDI, Register.SP, stackSize));
        return current;
    }

    // load an ir register from the stack into a temp register
    public Register loadIrRegister(IrRegister register, Register destination) {
        return add(new InstructionAsm.Lw(destination, irRegisters.get(register), Register.SP));
    }

    public void storeIrRegister(IrRegister register, Register value) {
        add(new InstructionAsm.Sw(value, irRegisters.get(register), Register.SP));
    }

    // allocate a stack slot for an ir register and return the offset from the stack pointer
    public int allocRegister(IrRegister register) {
        stackTop -= IrType.MAX_BYTE_SIZE;
        irRegisters.put(register, stackTop);
        return stackTop;
    }

    // for alloc instruction. return a temp register that points to the allocated memory
    public Register alloc() {
        stackTop -= IrType.MAX_BYTE_SIZE;
        return add(new InstructionAsm.BinImm(Register.newTempRegister(), Opcode.ADDI, Register.SP, stackTop));
    }
}
