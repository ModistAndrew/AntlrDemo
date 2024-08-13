package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.error.SymbolRedefinedException;
import modist.antlrdemo.frontend.error.SymbolUndefinedException;
import modist.antlrdemo.frontend.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SymbolTable<T extends Symbol> {
    private final HashMap<String, T> table = new HashMap<>();

    public void declare(T value) {
        table.merge(value.name, value, (oldValue, newValue) -> {
            throw new SymbolRedefinedException(newValue, oldValue);
        });
    }

    public T get(String name) {
        return table.get(name);
    }

    public boolean contains(String name) {
        return table.containsKey(name);
    }

    public T resolve(String name, Position position) {
        if (!contains(name)) {
            throw new SymbolUndefinedException(name, position);
        }
        return get(name);
    }

    public int size() {
        return table.size();
    }

    public static class Ordered<T extends Symbol> extends SymbolTable<T> {
        private final List<T> list = new ArrayList<>();

        @Override
        public void declare(T value) {
            super.declare(value);
            list.add(value);
        }

        public T get(int index) {
            return list.get(index);
        }
    }
}