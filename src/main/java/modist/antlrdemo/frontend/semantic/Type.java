package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.syntax.node.TypeNode;

public record Type(Symbol.TypeName typeName, int dimension) {
    public Type(Scope scope, TypeNode typeNode) {
        this(scope.resolveTypeName(typeNode.typeName, typeNode.position), typeNode.dimension);
    }
}