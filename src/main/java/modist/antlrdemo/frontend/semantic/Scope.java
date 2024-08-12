package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.syntax.node.DeclarationNode;

import java.util.*;

public class Scope {
    public Scope parent;


    public Scope(Scope parent) {
        this.parent = parent;
    }



    private static class SymbolTable<T extends Symbol> {
        private final HashMap<String, T> table = new HashMap<>();

        public void declare(String name, T value) {
        }

        public T refer(String name) {
            return null;
        }
    }
}