package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.metadata.IrType;

import java.util.List;

public record FunctionVarargsDeclarationIr(String name, IrType returnType, List<IrType> parameterTypes) implements Ir {
}
