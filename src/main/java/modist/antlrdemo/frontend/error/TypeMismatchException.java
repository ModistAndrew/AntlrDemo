package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.metadata.Position;
import modist.antlrdemo.frontend.semantic.Type;
import org.jetbrains.annotations.Nullable;

public class TypeMismatchException extends SemanticException {

    public TypeMismatchException(@Nullable Type provided, @Nullable Type expected, Position position) {
        super(String.format("Expression type %s does not match expected type %s", toString(provided), toString(expected)), position);
    }

    public TypeMismatchException(@Nullable Type provided, String predicateDescription, Position position) {
        super(String.format("Expression type %s should be %s", toString(provided), predicateDescription), position);
    }

    private static String toString(Type type) {
        return type == null ? "'void'" : String.format("'%s'", type);
    }
}
