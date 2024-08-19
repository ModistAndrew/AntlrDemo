package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.ast.metadata.Position;

public class CompileException extends RuntimeException {
    protected Position position;

    public CompileException(String message) {
        super(message);
    }

    public CompileException(String message, Position position) {
        super(message);
        this.position = position;
    }

    public String getErrorType() {
        return "Compile Error";
    }

    public Position getPosition() {
        return position == null ? PositionRecord.get() : position;
    }

    public static <T extends CompileException> T withPosition(T exception, Position position) {
        exception.position = position;
        return exception;
    }
}