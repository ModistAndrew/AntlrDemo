package modist.antlrdemo.frontend.semantic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// used when some symbol table needs to keep the order of symbols
public class OrderedSymbolTable<T extends Symbol> extends SymbolTable<T> {
    private final List<T> list = new ArrayList<>();
    private final Map<String, Integer> index = new HashMap<>();

    @Override
    public void declare(T value) {
        super.declare(value);
        index.put(value.name, list.size());
        list.add(value);
    }

    public T get(int index) {
        return list.get(index);
    }

    public int indexOf(String name) {
        return index.get(name);
    }

    public List<T> getList() {
        return list;
    }
}
