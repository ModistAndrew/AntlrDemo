package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.syntax.node.ExpressionNode;
import modist.antlrdemo.frontend.syntax.node.ExpressionNode.*;

public record ExpressionType(Type type, boolean isLValue) {
    public ExpressionType(Scope scope, ExpressionNode expression) {
        switch (expression) {
            case This ignored -> {
                if (scope instanceof ChildScope.Class classScope) {
                    this(classScope.classType, false);
                } else {
                    throw new IllegalStateException("Cannot use 'this' outside a class");
                }
            }
            case Variable variable -> this(variable, false);
            default -> throw new IllegalArgumentException("Unsupported expression node: " + expression);
        }
    }
}
