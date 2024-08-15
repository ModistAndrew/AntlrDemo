package modist.antlrdemo.frontend.error;

public class InvalidControlFlowException extends CompileException {
    public InvalidControlFlowException(String message) {
        super(message);
    }

    @Override
    public String getErrorType() {
        return "Invalid Control Flow";
    }
}
