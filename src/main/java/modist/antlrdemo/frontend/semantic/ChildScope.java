package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.syntax.Position;

public class ChildScope extends Scope {
    private final Scope parent; // the direct parent scope
    private final GlobalScope globalScope; // the global scope

    public ChildScope(Scope parent) { // create an empty child scope
        this.parent = parent;
        this.globalScope = parent.getGlobalScope();
    }

    @Override
    protected GlobalScope getGlobalScope() {
        return globalScope;
    }

    @Override
    public Symbol.Class resolveClass(String name, Position position) {
        return globalScope.resolveClass(name, position);
    }

    @Override
    public Symbol.TypeName resolveTypeName(String name, Position position) {
        return globalScope.resolveTypeName(name, position);
    }

    @Override
    public Symbol.Function resolveFunction(String name, Position position) {
        return functions.contains(name) ? functions.get(name) : parent.resolveFunction(name, position);
    }

    @Override
    public Symbol.Variable resolveVariable(String name, Position position) {
        return variables.contains(name) ? variables.get(name) : parent.resolveVariable(name, position);
    }
}
