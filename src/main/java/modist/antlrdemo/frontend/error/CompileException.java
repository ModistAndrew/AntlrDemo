package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.metadata.Position;

public class CompileException extends RuntimeException {
    public final CompileErrorType errorType;

    public CompileException(CompileErrorType errorType, String message, Position position) {
        super(String.format("[%s]: %s", position, message));
        this.errorType = errorType;
    }

    public CompileException(String message, Position position) {
        this(CompileErrorType.DEFAULT, message, position);
    }
}