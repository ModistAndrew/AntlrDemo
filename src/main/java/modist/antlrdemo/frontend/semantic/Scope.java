package modist.antlrdemo.frontend.semantic;

import java.util.HashMap;

public abstract class Scope {
    public Scope parent;

    public Scope(Scope parent) {
        this.parent = parent;
    }

    protected static class SymbolTable<T extends Symbol> {
        private final HashMap<String, T> table = new HashMap<>();

        public void declare(String name, T value) {
            table.merge(name, value);
        }

        public T refer(String name) {
            return table;
        }
    }
}
