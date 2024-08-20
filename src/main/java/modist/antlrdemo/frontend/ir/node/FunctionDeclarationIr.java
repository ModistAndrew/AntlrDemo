package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.metadata.IrType;

import java.util.List;

public record FunctionDeclarationIr(String name, IrType returnType, List<IrType> parameters) implements Ir {
}
