package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.semantic.Symbol;

public class SymbolRedefinedException extends SemanticException {

    public SymbolRedefinedException(String name, Symbol symbol, Symbol previous) {
        super(String.format("Symbol '%s' redefined, previous definition at %s", name, previous.position), symbol.position);
    }
}
