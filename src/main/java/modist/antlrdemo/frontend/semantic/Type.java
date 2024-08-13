package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.syntax.node.DeclarationNode;
import modist.antlrdemo.frontend.syntax.node.TypeNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Type(@Nullable Symbol.TypeName typeName, int dimension) {
    public static final Type NULL = new Type(null, 0);

    public Type(Scope scope, TypeNode typeNode) {
        this(scope.resolveTypeName(typeNode.typeName, typeNode.position), typeNode.dimension);
    }

    public Type(Scope scope, DeclarationNode.Class classNode) {
        this(scope.resolveTypeName(classNode.name, classNode.position), 0);
    }

    public Type(Symbol.TypeName typeName) {
        this(typeName, 0);
    }

    public Type increaseDimension() {
        return new Type(typeName, dimension + 1);
    }

    public Type decreaseDimension() {
        return new Type(typeName, dimension - 1);
    }

    public boolean isPrimitive() {
        return typeName != null && typeName.primitive && dimension == 0;
    }

    public boolean isArray() {
        return dimension > 0;
    }

    // join two types, return the common type if they match, otherwise return null
    // null-type can match any type with higher or equal dimension
    // specially, null-type with dimension 0 can match any non-primitive type
    @Nullable
    public Type join(@NotNull Type other) {
        if (typeName == null && other.typeName == null) {
            return new Type(null, Math.max(dimension, other.dimension));
        }
        if (typeName == null) {
            return !other.isPrimitive() && dimension <= other.dimension ? other : null;
        }
        if (other.typeName == null) {
            return !isPrimitive() && other.dimension <= dimension ? this : null;
        }
        return this.equals(other) ? other : null;
    }

    @Override
    public String toString() {
        return (typeName == null ? "null" : typeName.name) + "[]".repeat(dimension);
    }
}