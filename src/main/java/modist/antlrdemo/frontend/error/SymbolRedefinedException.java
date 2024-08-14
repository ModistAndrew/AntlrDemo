package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.metadata.Position;
import modist.antlrdemo.frontend.semantic.Symbol;

public class SymbolRedefinedException extends SemanticException {
    public SymbolRedefinedException(String name, Position position, Position previous) {
        super(String.format("Symbol '%s' redefined, previous definition at [%s]", name, previous), position);
    }

    public SymbolRedefinedException(Symbol symbol, Symbol previous) {
        this(symbol.name, symbol.position, previous.position);
    }
}
