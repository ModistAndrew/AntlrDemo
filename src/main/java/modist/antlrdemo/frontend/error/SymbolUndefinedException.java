package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.syntax.Position;

public class SymbolUndefinedException extends SemanticException {

    public SymbolUndefinedException(String name, Position position) {
        super(String.format("Symbol '%s' undefined", name), position);
    }
}
