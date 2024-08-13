package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.metadata.Position;
import modist.antlrdemo.frontend.semantic.Type;

public class TypeMismatchException extends SemanticException {

    public TypeMismatchException(Type provided, Type expected, Position position) {
        super(String.format("Expression type %s does not match expected type %s", toString(provided), toString(expected)), position);
    }

    public TypeMismatchException(Type provided, String predicateDescription, Position position) {
        super(String.format("Expression type %s should be %s", toString(provided), predicateDescription), position);
    }

    private static String toString(Type type) {
        return type == null ? "void" : type.toString();
    }
}
