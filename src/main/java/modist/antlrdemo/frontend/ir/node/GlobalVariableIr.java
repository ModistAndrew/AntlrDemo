package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.metadata.IrConstant;
import modist.antlrdemo.frontend.ir.metadata.IrGlobal;
import modist.antlrdemo.frontend.ir.metadata.IrType;

public record GlobalVariableIr(IrGlobal result, IrType type, IrConstant value) implements Ir {
    public GlobalVariableIr(IrGlobal result, IrType type) {
        this(result, type, type.defaultValue);
    }
}
