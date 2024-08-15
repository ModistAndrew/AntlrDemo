package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.syntax.node.DeclarationNode;
import modist.antlrdemo.frontend.syntax.node.TypeNode;
import org.jetbrains.annotations.Nullable;

public record Type(@Nullable Symbol.TypeName typeName, int dimension) {
    // typeName==null: null type. the type is unfixed and can be any non-primitive type. dimension must be 0
    // typeName==VOID: void type. the type is fixed and can only be void. dimension must be 0
    public Type(Scope scope, TypeNode typeNode) {
        this(scope.resolveTypeName(typeNode.typeName, typeNode.position), typeNode.dimension);
    }

    public Type(Symbol.TypeName typeName) {
        this(typeName, 0);
    }

    public Type(Scope scope, DeclarationNode.Class classNode) {
        this(scope.resolveTypeName(classNode.name, classNode.position));
    }

    public boolean isArray() {
        return dimension > 0;
    }

    @Override
    public String toString() {
        if (typeName == null) {
            return "null";
        }
        return typeName.name + "[]".repeat(dimension);
    }
}