package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.semantic.error.MultipleDefinitionsException;
import modist.antlrdemo.frontend.semantic.error.UndefinedIdentifierException;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable<T extends Symbol> {
    private final Map<String, T> table = new HashMap<>();

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

    public boolean isEmpty() {
        return table.isEmpty();
    }
}