package modist.antlrdemo.frontend.semantic.error;

public class DimensionOutOfBoundException extends CompileException {
    public DimensionOutOfBoundException() {
        super("Dimension out of bound");
    }

    @Override
    public String getErrorType() {
        return "Dimension Out Of Bound";
    }
}
