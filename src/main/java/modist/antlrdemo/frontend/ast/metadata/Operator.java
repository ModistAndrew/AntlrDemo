package modist.antlrdemo.frontend.ast.metadata;

import modist.antlrdemo.frontend.ir.metadata.IrOperator;
import modist.antlrdemo.frontend.semantic.BuiltinFeatures;
import modist.antlrdemo.frontend.semantic.Type;
import org.jetbrains.annotations.Nullable;

public enum Operator {
    // names correspond to the grammar
    // ir operators are used in the ir generation phase to simplify switch cases
    INC(IrOperator.ADD), DEC(IrOperator.SUB), NOT, LOGICAL_NOT,
    ADD(IrOperator.ADD), SUB(IrOperator.SUB), MUL(IrOperator.MUL), DIV(IrOperator.SDIV),
    MOD(IrOperator.SREM), SHL(IrOperator.SHL), SHR(IrOperator.ASHR),
    GT(IrOperator.SGT), LT(IrOperator.SLT), GE(IrOperator.SGE), LE(IrOperator.SLE),
    NE(IrOperator.NE), EQ(IrOperator.EQ),
    AND(IrOperator.AND), XOR(IrOperator.XOR), OR(IrOperator.OR),
    LOGICAL_AND, LOGICAL_OR;

    public final IrOperator irOperator;

    Operator() {
        this.irOperator = null;
    }

    Operator(IrOperator irOperator) {
        this.irOperator = irOperator;
    }

    // sub can be used in both preUnaryAssign and binaryExpression, but luckily both situations support the same type
    @Nullable
    public Type operate(Type operand) {
        return switch (this) {
            case EQ, NE -> operand.isVoid() ? null : BuiltinFeatures.BOOL;
            case ADD -> (operand.isInt() || operand.isString()) ? operand : null;
            case LT, GT, LE, GE -> (operand.isInt() || operand.isString()) ? BuiltinFeatures.BOOL : null;
            case INC, DEC, NOT, AND, XOR, OR, SUB, MUL, DIV, MOD, SHL, SHR -> operand.isInt() ? operand : null;
            case LOGICAL_AND, LOGICAL_OR, LOGICAL_NOT -> operand.isBool() ? operand : null;
        };
    }

    public String getIrPrefix() {
        return name().toLowerCase();
    }
}