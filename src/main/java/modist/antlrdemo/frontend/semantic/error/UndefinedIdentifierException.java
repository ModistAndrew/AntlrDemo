package modist.antlrdemo.frontend.semantic.error;

public class UndefinedIdentifierException extends CompileException {
    public UndefinedIdentifierException(String name) {
        super(String.format("Symbol '%s' undefined", name));
    }

    @Override
    public String getErrorType() {
        return "Undefined Identifier";
    }
}
