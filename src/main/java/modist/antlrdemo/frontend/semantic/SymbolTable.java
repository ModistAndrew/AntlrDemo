package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.error.MultipleDefinitionsException;
import modist.antlrdemo.frontend.error.UndefinedIdentifierException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//TODO: keep order of declaration
public class SymbolTable<T extends Symbol> {
    private final HashMap<String, T> table = new HashMap<>();

    public void declare(T value) {
        table.merge(value.name, value, (oldValue, newValue) -> {
            throw new MultipleDefinitionsException(newValue, oldValue);
        });
    }

    public T get(String name) {
        return table.get(name);
    }

    public boolean contains(String name) {
        return table.containsKey(name);
    }

    public T resolve(String name) {
        if (!contains(name)) {
            throw new UndefinedIdentifierException(name);
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