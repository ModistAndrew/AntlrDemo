package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.metadata.Position;
import modist.antlrdemo.frontend.semantic.Type;

public class TypeMismatchException extends CompileException {
    public TypeMismatchException(Type provided, Type expected, Position position) {
        super(CompileErrorType.TYPE_MISMATCH,
                String.format("Expression type '%s' does not match with type '%s'", provided, expected), position);
    }
}
