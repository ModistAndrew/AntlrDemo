package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.syntax.Position;

public abstract class Scope {
    protected final SymbolTable<Symbol.Function> functions = new SymbolTable<>();
    protected final SymbolTable<Symbol.Variable> variables = new SymbolTable<>();

    protected abstract GlobalScope getGlobalScope();

    public abstract Symbol.Class resolveClass(String name, Position position);

    public abstract Symbol.Function resolveFunction(String name, Position position);

    public abstract Symbol.Variable resolveVariable(String name, Position position);

    public abstract Symbol.TypeName resolveTypeName(String name, Position position);

}