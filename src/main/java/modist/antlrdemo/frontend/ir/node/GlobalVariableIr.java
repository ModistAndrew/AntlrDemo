package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.metadata.IrConstant;
import modist.antlrdemo.frontend.ir.metadata.IrType;
import modist.antlrdemo.frontend.ir.metadata.IrRegister;

public record GlobalVariableIr(IrRegister result, IrType type, IrConstant value) implements Ir {
    public GlobalVariableIr(IrRegister result, IrType type) {
        this(result, type, type.defaultValue);
    }
}
