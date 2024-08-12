package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.syntax.node.DeclarationNode;
import modist.antlrdemo.frontend.syntax.node.TypeNode;

public record Type(Symbol.TypeName typeName, int dimension) {
    public Type(Scope scope, TypeNode typeNode) {
        this(scope.resolveTypeName(typeNode.typeName, typeNode.position), typeNode.dimension);
    }

    public Type(Scope scope, DeclarationNode.Class classNode) {
        this(scope.resolveTypeName(classNode.name, classNode.position), 0);
    }

    public Type(Symbol.TypeName typeName) {
        this(typeName, 0);
    }
}