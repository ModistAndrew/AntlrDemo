package modist.antlrdemo.frontend.semantic.error;

import modist.antlrdemo.frontend.semantic.Type;

public class InvalidTypeException extends CompileException {
    public InvalidTypeException(Type provided, String predicateDescription) {
        super(String.format("Expression type '%s' is not %s", provided, predicateDescription));
    }

    @Override
    public String getErrorType() {
        return "Invalid Type";
    }
}
