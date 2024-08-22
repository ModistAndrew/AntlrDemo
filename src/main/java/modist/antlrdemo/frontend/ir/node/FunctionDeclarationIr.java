package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.metadata.IrType;

import java.util.List;

// automatically add %this as the first parameter if isMember is true
public record FunctionDeclarationIr(String name, IrType returnType, List<IrType> parameters, boolean isMember) implements Ir {
}
