package modist.antlrdemo.frontend.error;

public class MissingReturnStatementException extends CompileException {
    public MissingReturnStatementException() {
        super("Missing return statement");
    }

    @Override
    public String getErrorType() {
        return "Missing Return Statement";
    }
}
