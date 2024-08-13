package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.Position;
import modist.antlrdemo.frontend.semantic.Type;

public class ExpressionTypeMismatchException extends SemanticException {

    public ExpressionTypeMismatchException(Type provided, Type expected, Position position) {
        super(String.format("Expression type '%s' does not match expected type '%s'", provided, expected), position);
    }

    public ExpressionTypeMismatchException(Type provided, String predicateDescription, Position position) {
        super(String.format("Expression type '%s' should be '%s'", provided, predicateDescription), position);
    }
}
