package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.metadata.Position;

public class SymbolUndefinedException extends CompileException {

    public SymbolUndefinedException(String name, Position position) {
        super(CompileErrorType.UNDEFINED_IDENTIFIER, String.format("Symbol '%s' undefined", name), position);
    }
}
