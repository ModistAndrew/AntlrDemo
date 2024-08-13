package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.metadata.Position;
import modist.antlrdemo.frontend.syntax.node.StatementNode;

public abstract sealed class Scope permits ChildScope, GlobalScope {
    protected final SymbolTable<Symbol.Function> functions = new SymbolTable<>();
    protected final SymbolTable<Symbol.Variable> variables = new SymbolTable<>();

    protected abstract GlobalScope getGlobalScope();

    public abstract Symbol.Class getClass(Type type);

    public abstract Symbol.Function resolveFunction(String name, Position position);

    public abstract Symbol.Variable resolveVariable(String name, Position position);

    public abstract Symbol.TypeName resolveTypeName(String name, Position position);

    // local variables don't support forward references
    // we provide a method to declare them out of constructor
    public void declareLocalVariables(StatementNode.VariableDeclarations variableDeclarations) {
        variableDeclarations.variables.forEach(declaration -> variables.declare(new Symbol.Variable(this, declaration)));
    }
}