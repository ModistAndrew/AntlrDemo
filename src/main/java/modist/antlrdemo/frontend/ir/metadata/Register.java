package modist.antlrdemo.frontend.ir.metadata;

import modist.antlrdemo.frontend.semantic.Symbol;

public record Register(String name, IrType type) implements Variable {
    public Register(Symbol.Variable symbol) { // create a ptr from variable
        this(symbol.irName, IrType.PTR);
        if (symbol.isMember) {
            // TODO: member variable should be specially handled?
            throw new IllegalArgumentException();
        }
    }
}
