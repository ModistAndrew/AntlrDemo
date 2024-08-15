package modist.antlrdemo.frontend.error;

public class CompileException extends RuntimeException {
    public CompileException(String message) {
        super(String.format("[%s]: %s", PositionRecorder.get(), message));
    }

    public String getErrorType() {
        return "Compile Error";
    }
}