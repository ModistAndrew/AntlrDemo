package modist.antlrdemo.frontend.semantic.error;

public class InvalidIdentifierException extends CompileException {
    public InvalidIdentifierException(String message) {
        super(message);
    }

    @Override
    public String getErrorType() {
        return "Invalid Identifier";
    }
}
