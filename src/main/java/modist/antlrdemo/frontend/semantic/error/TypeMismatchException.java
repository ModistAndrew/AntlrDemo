package modist.antlrdemo.frontend.semantic.error;

import modist.antlrdemo.frontend.semantic.Type;

public class TypeMismatchException extends CompileException {
    public TypeMismatchException(String description, Type expected) {
        super(String.format("Given type is %s which does not match with type %s", description, expected));
    }

    public TypeMismatchException(Type provided, Type expected) {
        super(String.format("Type '%s' does not match with type '%s'", provided, expected));
    }

    @Override
    public String getErrorType() {
        return "Type Mismatch";
    }
}
