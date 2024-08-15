package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.semantic.Type;

public class InvalidTypeException extends CompileException {
    public InvalidTypeException(Type provided, String predicateDescription) {
        super(String.format("Expression type '%s' should be %s", provided, predicateDescription));
    }

    @Override
    public String getErrorType() {
        return "Invalid Type";
    }
}
