package modist.antlrdemo.frontend.error;

public enum CompileErrorType {
    INVALID_IDENTIFIER("Invalid Identifier"),
    MULTIPLE_DEFINITIONS("Multiple Definitions"),
    UNDEFINED_IDENTIFIER("Undefined Identifier"),
    TYPE_MISMATCH("Type Mismatch"),
    INVALID_CONTROL_FLOW("Invalid Control Flow"),
    INVALID_TYPE("Invalid Type"),
    MISSING_RETURN_STATEMENT("Missing Return Statement"),
    DIMENSION_OUT_OF_BOUND("Dimension Out of Bound"),
    DEFAULT("Compile Error");

    public final String name;

    CompileErrorType(String name) {
        this.name = name;
    }
}
