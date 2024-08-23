package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.IrStringUtil;
import modist.antlrdemo.frontend.ir.metadata.IrType;

import java.util.List;

public record FunctionDeclarationIr(String name, IrType returnType, List<IrType> parameterTypes) implements Ir {
    @Override
    public String toString() {
        return String.format("declare %s %s(%s)", returnType, name, IrStringUtil.toStringTypes(parameterTypes));
    }
}
