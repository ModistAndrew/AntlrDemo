package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.metadata.Constant;
import modist.antlrdemo.frontend.ir.metadata.IrType;
import modist.antlrdemo.frontend.ir.metadata.Register;

public record GlobalVariableIr(Register result, IrType type, Constant value) implements Ir {
    public GlobalVariableIr(Register result, IrType type) {
        this(result, type, type.defaultValue);
    }

    @Override
    public String toString() {
        return String.format("%s = global %s %s", result, type, value);
    }
}
