package modist.antlrdemo.frontend.semantic;

import java.util.ArrayList;
import java.util.List;

// used when some symbol table needs to keep the order of symbols
public class OrderedSymbolTable<T extends Symbol> extends SymbolTable<T> {
    public final List<T> list = new ArrayList<>();

    @Override
    public void declare(T value) {
        super.declare(value);
        list.add(value);
    }
}
