package modist.antlrdemo.backend.asm;

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
import org.jetbrains.annotations.Nullable;

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
            case InstructionIr.Result result -> {
                Register data = visitResult(result);
                IrRegister destination = result.result();
                if (destination != null) {
                    if (destination.isGlobal()) {
                        add(new InstructionAsm.SwLabel(data, IrNamer.removePrefix(destination.name())));
                    } else {
                        currentFunction.storeIrRegister(destination, data);
                    }
                }
            }
            case InstructionIr.Store store -> add(new InstructionAsm.Sw(load(store.value()), 0, load(store.pointer())));
            case InstructionIr.Jump jump -> add(new InstructionAsm.J(currentFunction.renameLabel(jump.label())));
            case InstructionIr.Br br -> {
                if (br.nearTrue()) {
                    add(new InstructionAsm.Bnez(load(br.condition()), currentFunction.renameLabel(br.trueLabel())));
                    add(new InstructionAsm.J(currentFunction.renameLabel(br.falseLabel())));
                } else {
                    add(new InstructionAsm.Beqz(load(br.condition()), currentFunction.renameLabel(br.falseLabel())));
                    add(new InstructionAsm.J(currentFunction.renameLabel(br.trueLabel())));
                }
            }
            case InstructionIr.Ret ret -> {
                if (ret.value() != null) {
                    load(ret.value(), Register.A0);
                }
                currentFunction.prepareReturn();
                add(new InstructionAsm.Ret());
            }
        }
        Register.resetTempRegisters(); // reset temp register for each instruction
    }

    // return the register that holds the result of the instruction
    // return null for void function calls
    @Nullable
    private Register visitResult(InstructionIr.Result ir) {
        return switch (ir) {
            case InstructionIr.Subscript subscript -> add(new InstructionAsm.Bin(temp(),
                    Opcode.ADD, load(subscript.pointer()),
                    add(new InstructionAsm.BinImm(temp(), Opcode.SLLI, load(subscript.index()), IrType.LOG_MAX_BYTE_SIZE))));
            case InstructionIr.MemberVariable memberVariable -> add(new InstructionAsm.BinImm(temp(),
                    Opcode.ADDI, load(memberVariable.pointer()), memberVariable.memberIndex() * IrType.MAX_BYTE_SIZE));
            case InstructionIr.FunctionCall functionCall -> {
                for (int i = 0; i < functionCall.arguments().size(); i++) {
                    IrOperand argument = functionCall.arguments().get(i);
                    if (i < Register.ARG_REGISTERS.length) {
                        load(argument, Register.ARG_REGISTERS[i]);
                    } else {
                        add(new InstructionAsm.Sw(load(argument),
                                (i - Register.ARG_REGISTERS.length) * IrType.MAX_BYTE_SIZE, Register.SP));
                    }
                    Register.resetTempRegisters(); // must reuse temp registers for each argument
                }
                add(new InstructionAsm.Call(IrNamer.removePrefix(functionCall.function())));
                yield functionCall.result() != null ? Register.A0 : null;
            }
            case InstructionIr.Load load -> add(new InstructionAsm.Lw(temp(), 0, load(load.pointer())));
            case InstructionIr.Phi ignored -> throw new UnsupportedOperationException();
            case InstructionIr.Bin bin -> add(new InstructionAsm.Bin(temp(),
                    bin.operator().opcode, load(bin.left()), load(bin.right())));
            case InstructionIr.Icmp icmp -> switch (icmp.operator()) {
                case EQ, NE -> add(new InstructionAsm.Un(temp(),
                        icmp.operator() == IrOperator.EQ ? Opcode.SEQZ : Opcode.SNEZ,
                        add(new InstructionAsm.Bin(temp(), Opcode.XOR, load(icmp.left()), load(icmp.right())))));
                case SLT, SGT -> add(new InstructionAsm.Bin(temp(), Opcode.SLT,
                        load(icmp.operator() == IrOperator.SLT ? icmp.left() : icmp.right()),
                        load(icmp.operator() == IrOperator.SLT ? icmp.right() : icmp.left())));
                case SLE, SGE -> add(new InstructionAsm.Un(temp(),
                        Opcode.SEQZ, add(new InstructionAsm.Bin(temp(), Opcode.SLT,
                        load(icmp.operator() == IrOperator.SGE ? icmp.left() : icmp.right()),
                        load(icmp.operator() == IrOperator.SGE ? icmp.right() : icmp.left())))));
                default -> throw new IllegalArgumentException();
            };
        };
    }

    // may load from global variables, local variables or constants
    // specially notice that global variables are addresses in IR while we store the data directly in .data section
    // as a result, we need to use La instruction to get the address of the global variable
    private Register load(IrOperand operand, Register destination) {
        return switch (operand.asConcrete()) {
            case IrRegister register -> register.isGlobal() ?
                    add(new InstructionAsm.La(destination, IrNamer.removePrefix(register.name()))) :
                    currentFunction.loadIrRegister(register, destination);
            // TODO: use asm for immediate values directly
            case IrConstant constant -> add(new InstructionAsm.Li(destination, constant.asImmediate()));
            case null -> Register.ZERO; // undefined value. use an arbitrary register
        };
    }

    private Register load(IrOperand operand) {
        return load(operand, temp());
    }

    private void add(InstructionAsm instruction) {
        currentFunction.add(instruction);
    }

    private Register add(InstructionAsm.Result instruction) {
        return currentFunction.add(instruction);
    }

    private Register temp() {
        return Register.newTempRegister();
    }
}
