package modist.antlrdemo.backend.asm;

import modist.antlrdemo.backend.asm.metadata.Location;
import modist.antlrdemo.backend.asm.node.ConstantStringAsm;
import modist.antlrdemo.backend.asm.node.GlobalVariableAsm;
import modist.antlrdemo.backend.asm.node.InstructionAsm;
import modist.antlrdemo.backend.asm.node.ProgramAsm;
import modist.antlrdemo.backend.asm.metadata.Opcode;
import modist.antlrdemo.backend.asm.metadata.Register;
import modist.antlrdemo.frontend.ir.IrNamer;
import modist.antlrdemo.frontend.ir.metadata.*;
import modist.antlrdemo.frontend.ir.node.InstructionIr;
import modist.antlrdemo.frontend.ir.node.ProgramIr;

public class AsmBuilder {
    private ProgramAsm program;
    private FunctionBuilder currentFunction;

    public ProgramAsm visitProgram(ProgramIr programIr) {
        program = new ProgramAsm();
        programIr.globalVariables.forEach(globalVariable ->
                program.data.add(new GlobalVariableAsm(IrNamer.removePrefix(globalVariable.result().name()))));
        programIr.constantStrings.forEach(constantString ->
                program.rodata.add(new ConstantStringAsm(IrNamer.removePrefix(constantString.result().name()), constantString.value())));
        programIr.functions.forEach(function -> {
            currentFunction = new FunctionBuilder(function);
            function.body.forEach(block -> {
                currentFunction.newBlock(block.label);
                block.instructions.forEach(this::visit);
            });
            program.text.add(currentFunction.build());
        });
        return program;
    }

    private void visit(InstructionIr ir) {
        switch (ir) {
            case InstructionIr.Param param -> currentFunction.moveParameter(param.result(), param.index());
            case InstructionIr.FunctionCall functionCall -> {
                for (int i = 0; i < functionCall.arguments().size(); i++) {
                    IrOperand argument = functionCall.arguments().get(i);
                    Location parameterLocation = i < Register.ARG_REGISTERS.length ?
                            new Location.Reg(Register.ARG_REGISTERS[i]) :
                            new Location.Stack(IrType.MAX_BYTE_SIZE * (i - Register.ARG_REGISTERS.length));
                    move(parameterLocation, argument);
                }
                add(new InstructionAsm.Call(IrNamer.removePrefix(functionCall.function())));
                if (functionCall.result() != null) {
                    currentFunction.move(currentFunction.getLocation(functionCall.result()), new Location.Reg(Register.A0));
                }
            }
            case InstructionIr.Result result -> {
                switch (currentFunction.getLocation(result.result())) {
                    case Location.Reg reg -> visitResult(result, reg.register());
                    case Location.Stack stack -> {
                        visitResult(result, Register.T0);
                        add(new InstructionAsm.Sw(Register.T0, stack.offset(), Register.SP));
                    }
                }
            }
            case InstructionIr.Store store -> add(new InstructionAsm.Sw(get(store.value()), 0, get(store.pointer())));
            case InstructionIr.Jump jump -> add(new InstructionAsm.J(currentFunction.renameLabel(jump.label())));
            case InstructionIr.Br br -> {
                if (br.nearTrue()) {
                    add(new InstructionAsm.Bnez(get(br.condition()), currentFunction.renameLabel(br.trueLabel())));
                    add(new InstructionAsm.J(currentFunction.renameLabel(br.falseLabel())));
                } else {
                    add(new InstructionAsm.Beqz(get(br.condition()), currentFunction.renameLabel(br.falseLabel())));
                    add(new InstructionAsm.J(currentFunction.renameLabel(br.trueLabel())));
                }
            }
            case InstructionIr.Ret ret -> {
                if (ret.value() != null) {
                    move(new Location.Reg(Register.A0), ret.value());
                }
                currentFunction.prepareReturn();
                add(new InstructionAsm.Ret());
            }
        }
    }

    private void visitResult(InstructionIr.Result ir, Register destination) {
        switch (ir) {
            case InstructionIr.Subscript subscript -> {
                add(new InstructionAsm.BinImm(Register.T0, Opcode.SLLI, get(subscript.index()), IrType.LOG_MAX_BYTE_SIZE));
                add(new InstructionAsm.Bin(destination, Opcode.ADD, Register.T0, get(subscript.pointer(), Register.T1)));
            }
            case InstructionIr.MemberVariable memberVariable -> add(new InstructionAsm.BinImm(destination,
                    Opcode.ADDI, get(memberVariable.pointer()), memberVariable.memberIndex() * IrType.MAX_BYTE_SIZE));
            case InstructionIr.Load load -> add(new InstructionAsm.Lw(destination, 0, get(load.pointer())));
            case InstructionIr.Bin bin -> add(new InstructionAsm.Bin(destination,
                    bin.operator().opcode, get(bin.left()), get(bin.right(), Register.T1)));
            case InstructionIr.Icmp icmp -> {
                switch (icmp.operator()) {
                    case EQ, NE -> {
                        add(new InstructionAsm.Bin(Register.T0, Opcode.XOR, get(icmp.left()), get(icmp.right(), Register.T1)));
                        add(new InstructionAsm.Un(destination, icmp.operator() == IrOperator.EQ ? Opcode.SEQZ : Opcode.SNEZ, Register.T0));
                    }
                    case SLT, SGT -> add(new InstructionAsm.Bin(destination, Opcode.SLT,
                            get(icmp.operator() == IrOperator.SLT ? icmp.left() : icmp.right()),
                            get(icmp.operator() == IrOperator.SLT ? icmp.right() : icmp.left(), Register.T1)));
                    case SLE, SGE -> {
                        add(new InstructionAsm.Bin(Register.T0, Opcode.SLT,
                                get(icmp.operator() == IrOperator.SGE ? icmp.left() : icmp.right()),
                                get(icmp.operator() == IrOperator.SGE ? icmp.right() : icmp.left(), Register.T1)));
                        add(new InstructionAsm.Un(destination, Opcode.SEQZ, Register.T0));
                    }
                    default -> throw new IllegalArgumentException();
                }
            }
            case InstructionIr.Phi ignored -> throw new UnsupportedOperationException(); // handled specially
            case InstructionIr.Param ignored -> throw new UnsupportedOperationException(); // handled in visit
            case InstructionIr.FunctionCall ignored -> throw new UnsupportedOperationException(); // handled in visit
        }
    }

    private Register get(IrOperand source, Register temp) {
        return switch (source.asConcrete()) {
            case IrGlobal global -> add(new InstructionAsm.La(temp, IrNamer.removePrefix(global.name())));
            case IrRegister register -> currentFunction.get(currentFunction.getLocation(register), temp);
            case IrConstant constant -> add(new InstructionAsm.Li(temp, constant.asImmediate()));
            case IrUndefined ignored -> Register.ZERO;
        };
    }

    private void move(Location destination, IrOperand source) {
        switch (source.asConcrete()) {
            case IrGlobal global -> {
                switch (destination) {
                    case Location.Reg reg ->
                            add(new InstructionAsm.La(reg.register(), IrNamer.removePrefix(global.name())));
                    case Location.Stack stack -> {
                        add(new InstructionAsm.La(Register.T0, IrNamer.removePrefix(global.name())));
                        add(new InstructionAsm.Sw(Register.T0, stack.offset(), Register.SP));
                    }
                }
            }
            case IrRegister register -> currentFunction.move(destination, currentFunction.getLocation(register));
            case IrConstant constant -> {
                switch (destination) {
                    case Location.Reg reg -> add(new InstructionAsm.Li(reg.register(), constant.asImmediate()));
                    case Location.Stack stack -> {
                        add(new InstructionAsm.Li(Register.T0, constant.asImmediate()));
                        add(new InstructionAsm.Sw(Register.T0, stack.offset(), Register.SP));
                    }
                }
            }
            case IrUndefined ignored -> {
            }
        }
    }

    private Register get(IrOperand operand) {
        return get(operand, Register.T0);
    }

    private void add(InstructionAsm instruction) {
        currentFunction.add(instruction);
    }

    private Register add(InstructionAsm.Result instruction) {
        return currentFunction.add(instruction);
    }
}
