package modist.antlrdemo.frontend.ir.metadata;

import modist.antlrdemo.frontend.semantic.Symbol;
import modist.antlrdemo.frontend.semantic.Type;

public record Register(String name, boolean isGlobal, Type type) implements Variable {
    public Register(Symbol.Variable symbol) {
        this(symbol.irName, symbol.isGlobal, symbol.type);
        if (symbol.isMember) {
            // TODO: member variable should be specially handled
            throw new IllegalArgumentException();
        }
    }
}
