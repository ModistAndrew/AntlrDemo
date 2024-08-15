package modist.antlrdemo.frontend.error;

public class DimensionOutOfBoundException extends CompileException {
    public DimensionOutOfBoundException() {
        super("Dimension out of bound");
    }

    @Override
    public String getErrorType() {
        return "Dimension Out of Bound";
    }
}
