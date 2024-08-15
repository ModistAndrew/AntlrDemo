package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.metadata.Position;
import modist.antlrdemo.frontend.semantic.Type;

public class InvalidTypeException extends CompileException {

    public InvalidTypeException(Type provided, String predicateDescription, Position position) {
        super(CompileErrorType.INVALID_TYPE,
                String.format("Expression type '%s' should be %s", provided, predicateDescription), position);
    }
}
