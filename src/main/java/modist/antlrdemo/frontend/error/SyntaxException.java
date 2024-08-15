package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.metadata.Position;

public class SyntaxException extends CompileException {

    public SyntaxException(String message, Position position) {
        super(CompileErrorType.INVALID_IDENTIFIER, message, position);
    }
}