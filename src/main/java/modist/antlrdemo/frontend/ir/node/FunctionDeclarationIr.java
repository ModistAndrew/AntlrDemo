package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.semantic.Type;

import java.util.List;

public record FunctionDeclarationIr(String name, Type returnType, List<Type> parameters) implements Ir {
}
