package modist.antlrdemo.backend.asm;

import modist.antlrdemo.backend.asm.node.BlockAsm;
import modist.antlrdemo.backend.asm.node.FunctionAsm;
import modist.antlrdemo.backend.asm.node.InstructionAsm;
import modist.antlrdemo.backend.asm.metadata.Opcode;
import modist.antlrdemo.backend.asm.metadata.Register;
import modist.antlrdemo.frontend.ir.IrNamer;
import modist.antlrdemo.frontend.ir.metadata.IrRegister;
import modist.antlrdemo.frontend.ir.metadata.IrType;
import modist.antlrdemo.frontend.ir.node.BlockIr;
import modist.antlrdemo.frontend.ir.node.FunctionIr;
import modist.antlrdemo.frontend.ir.node.InstructionIr;

import java.util.*;

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
    // function call with too many arguments may enlarge the stack size
    // stackSize should be aligned
    // ir registers use the stack from the top to the bottom; function parameters use the stack from the bottom to the top
    private final Map<IrRegister, Integer> irRegisters = new HashMap<>();

    // (new -> add* -> (newBlock -> add* ->)* build)*
    public FunctionBuilder(FunctionIr function) {
        current = new FunctionAsm(IrNamer.removePrefix(function.name));
        savedRegisters = List.of(Register.RA); // only ra needs to be saved currently: ra may be overwritten
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
                allocRegister(parameter);
                storeIrRegister(parameter, Register.ARG_REGISTERS[i]);
            } else {
                // a trick here: we needn't allocate a stack slot for the parameter but use the original stack slot
                // note that sp is already decremented by stackSize; we need to add stackSize back
                irRegisters.put(parameter, stackSize + IrType.MAX_BYTE_SIZE * (i - Register.ARG_REGISTERS.length));
            }
        }
        // allocate stack slots for ir registers
        for (BlockIr block : function.body) {
            for (InstructionIr instruction : block.instructions) {
                if (instruction instanceof InstructionIr.Result result && result.result() != null) {
                    allocRegister(result.result());
                }
            }
        }
    }

    // we have to calculate the stack size before building the function so that stack pointer won't be changed during the process
    // must be careful that this corresponds to calls that change the stack pointer
    private int calculateStackSize(FunctionIr function) {
        int size = savedRegisters.size() * Register.BYTE_SIZE; // saved registers
        size += IrType.MAX_BYTE_SIZE * Math.min(Register.ARG_REGISTERS.length, function.parameters.size()); // parameters in arg registers
        int maxFunctionCallParameters = 0;
        Set<IrRegister> registers = new HashSet<>();
        for (BlockIr block : function.body) {
            for (InstructionIr instruction : block.instructions) {
                if (instruction instanceof InstructionIr.Result result && result.result() != null) {
                    registers.add(result.result());
                }
                if (instruction instanceof InstructionIr.FunctionCall functionCall) {
                    maxFunctionCallParameters = Math.max(maxFunctionCallParameters, functionCall.arguments().size());
                }
            }
        }
        size += registers.size() * IrType.MAX_BYTE_SIZE; // ir registers
        size += Math.max(maxFunctionCallParameters - Register.ARG_REGISTERS.length, 0) * IrType.MAX_BYTE_SIZE; // space for function call parameters
        size = (size + STACK_ALIGN - 1) / STACK_ALIGN * STACK_ALIGN; // align the stack size
        return size;
    }

    // allocate a stack slot for an ir register if it hasn't been allocated
    private void allocRegister(IrRegister register) {
        if (!irRegisters.containsKey(register)) {
            stackTop -= IrType.MAX_BYTE_SIZE;
            irRegisters.put(register, stackTop);
        }
    }

    // may perform some checks, e.g. check immediate range
    public void add(InstructionAsm instruction) {
        if (instruction instanceof InstructionAsm.Immediate immediate && !immediate.isImmInRange()) {
            Register immediateRegister = add(new InstructionAsm.Li(Register.T6, immediate.immediate()));
            switch (immediate) {
                case InstructionAsm.BinImm binImm ->
                        add(new InstructionAsm.Bin(binImm.result(), Opcode.imm2reg(binImm.opcode()), binImm.left(), immediateRegister));
                case InstructionAsm.Lw lw -> add(new InstructionAsm.Lw(lw.result(), 0,
                        add(new InstructionAsm.Bin(Register.T6, Opcode.ADD, immediateRegister, lw.base()))));
                case InstructionAsm.Sw sw -> add(new InstructionAsm.Sw(sw.value(), 0,
                        add(new InstructionAsm.Bin(Register.T6, Opcode.ADD, immediateRegister, sw.base()))));
            }
        } else {
            current.blocks.getLast().instructions.add(instruction);
        }
    }

    public Register add(InstructionAsm.Result instruction) {
        add((InstructionAsm) instruction);
        return instruction.result();
    }

    public void newBlock(String name) {
        current.blocks.add(new BlockAsm(renameLabel(name)));
    }

    public String renameLabel(String name) {
        return current.name + "." + name;
    }

    public void prepareReturn() {
        // restore saved registers
        for (int i = savedRegisters.size() - 1; i >= 0; i--) {
            add(new InstructionAsm.Lw(savedRegisters.get(i), stackSize - Register.BYTE_SIZE * (i + 1), Register.SP));
        }
        // pop stack pointer
        add(new InstructionAsm.BinImm(Register.SP, Opcode.ADDI, Register.SP, stackSize));
    }

    public FunctionAsm build() {
        return current;
    }

    // load an ir register from the stack into a temp register
    public Register loadIrRegister(IrRegister register, Register destination) {
        return add(new InstructionAsm.Lw(destination, irRegisters.get(register), Register.SP));
    }

    public void storeIrRegister(IrRegister register, Register value) {
        add(new InstructionAsm.Sw(value, irRegisters.get(register), Register.SP));
    }
}
