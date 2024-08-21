package modist.antlrdemo.frontend.ast.metadata;

import modist.antlrdemo.frontend.semantic.BuiltinFeatures;
import modist.antlrdemo.frontend.semantic.Type;
import org.jetbrains.annotations.Nullable;

public enum Operator {
    INC, DEC, NOT, LOGICAL_NOT,
    ADD, SUB, MUL, DIV, MOD, SHL, SHR, GT, LT, GE, LE, NE, EQ, AND, XOR, OR, LOGICAL_AND, LOGICAL_OR;

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
}
