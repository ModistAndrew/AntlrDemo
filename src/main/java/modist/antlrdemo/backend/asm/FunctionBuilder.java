package modist.antlrdemo.backend.asm;

import modist.antlrdemo.backend.asm.metadata.Location;
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
    private final int stackSize;
    private final int savedRegisterCount;
    private final Map<IrRegister, Location> registerMap = new HashMap<>();

    // (new -> add* -> (newBlock -> add* ->)* build)*
    public FunctionBuilder(FunctionIr function) {
        current = new FunctionAsm(IrNamer.removePrefix(function.name));
        stackSize = calculateStackSize(function);
        // push stack pointer
        add(new InstructionAsm.BinImm(Register.SP, Opcode.ADDI, Register.SP, -stackSize));
        // save RA
        add(new InstructionAsm.Sw(Register.RA, stackSize - Register.BYTE_SIZE, Register.SP));
        // save saved registers
        savedRegisterCount = Math.min(Register.SAVED_REGISTERS.length, function.colorCount);
        for (int i = 0; i < savedRegisterCount; i++) {
            add(new InstructionAsm.Sw(Register.SAVED_REGISTERS[i], stackSize - Register.BYTE_SIZE * (i + 2), Register.SP));
        }
        function.colorMap.forEach((irRegister, color) -> registerMap.put(irRegister,
                color < Register.SAVED_REGISTERS.length ? new Location.Reg(Register.SAVED_REGISTERS[color]) :
                        new Location.Stack(stackSize - Register.BYTE_SIZE * (savedRegisterCount + 2 + color - Register.SAVED_REGISTERS.length))));
    }

    // we have to calculate the stack size before building the function so that stack pointer won't be changed during the process
    // must be careful that this corresponds to calls that change the stack pointer
    private int calculateStackSize(FunctionIr function) {
        int size = Register.BYTE_SIZE; // RA
        size += function.colorCount * IrType.MAX_BYTE_SIZE; // ir registers on saved registers or stack. including parameters
        int maxFunctionCallParameters = 0;
        for (BlockIr block : function.body) {
            for (InstructionIr instruction : block.instructions) {
                if (instruction instanceof InstructionIr.FunctionCall functionCall) {
                    maxFunctionCallParameters = Math.max(maxFunctionCallParameters, functionCall.arguments().size());
                }
            }
        }
        size += Math.max(maxFunctionCallParameters - Register.ARG_REGISTERS.length, 0) * IrType.MAX_BYTE_SIZE; // space for function call parameters
        size = (size + STACK_ALIGN - 1) / STACK_ALIGN * STACK_ALIGN; // align the stack size
        return size;
    }

    // may perform some checks, e.g. check immediate range
    public void add(InstructionAsm instruction) {
        if (instruction instanceof InstructionAsm.Immediate immediate && !immediate.isImmInRange()) {
            Register immediateRegister = add(new InstructionAsm.Li(Register.T2, immediate.immediate()));
            switch (immediate) {
                case InstructionAsm.BinImm binImm ->
                        add(new InstructionAsm.Bin(binImm.result(), Opcode.imm2reg(binImm.opcode()), binImm.left(), immediateRegister));
                case InstructionAsm.Lw lw -> add(new InstructionAsm.Lw(lw.result(), 0,
                        add(new InstructionAsm.Bin(Register.T2, Opcode.ADD, immediateRegister, lw.base()))));
                case InstructionAsm.Sw sw -> add(new InstructionAsm.Sw(sw.value(), 0,
                        add(new InstructionAsm.Bin(Register.T2, Opcode.ADD, immediateRegister, sw.base()))));
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
        current.blocks.add(new BlockAsm(name));
    }

    public String renameLabel(String name) {
        return current.name + "." + name;
    }

    public void prepareReturn() {
        // restore RA
        add(new InstructionAsm.Lw(Register.RA, stackSize - Register.BYTE_SIZE, Register.SP));
        // restore saved registers
        for (int i = savedRegisterCount - 1; i >= 0; i--) {
            add(new InstructionAsm.Lw(Register.SAVED_REGISTERS[i], stackSize - Register.BYTE_SIZE * (i + 2), Register.SP));
        }
        // pop stack pointer
        add(new InstructionAsm.BinImm(Register.SP, Opcode.ADDI, Register.SP, stackSize));
    }

    public FunctionAsm build() {
        return current;
    }

    public void moveParameter(IrRegister destination, int index) {
        Location parameterLocation = index < Register.ARG_REGISTERS.length ?
                new Location.Reg(Register.ARG_REGISTERS[index]) :
                new Location.Stack(stackSize + IrType.MAX_BYTE_SIZE * (index - Register.ARG_REGISTERS.length));
        move(getLocation(destination), parameterLocation);
    }

    public Location getLocation(IrRegister register) {
        return registerMap.get(register);
    }

    public Register get(Location source, Register temp) {
        return switch (source) {
            case Location.Reg reg -> reg.register();
            case Location.Stack stack -> add(new InstructionAsm.Lw(temp, stack.offset(), Register.SP));
        };
    }

    public void move(Location destination, Location source) {
        switch (destination) {
            case Location.Reg destinationReg -> {
                switch (source) {
                    case Location.Reg sourceReg ->
                            add(new InstructionAsm.Mv(destinationReg.register(), sourceReg.register()));
                    case Location.Stack sourceStack ->
                            add(new InstructionAsm.Lw(destinationReg.register(), sourceStack.offset(), Register.SP));
                }
            }
            case Location.Stack destinationStack -> {
                switch (source) {
                    case Location.Reg sourceReg ->
                            add(new InstructionAsm.Sw(sourceReg.register(), destinationStack.offset(), Register.SP));
                    case Location.Stack sourceStack -> {
                        add(new InstructionAsm.Lw(Register.T0, sourceStack.offset(), Register.SP));
                        add(new InstructionAsm.Sw(Register.T0, destinationStack.offset(), Register.SP));
                    }
                }
            }
        }
    }
}
