package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.metadata.Position;
import modist.antlrdemo.frontend.syntax.node.StatementNode;
import org.jetbrains.annotations.Nullable;

public abstract class Scope {
    protected final SymbolTable<Symbol.Function> functions = new SymbolTable<>();
    protected final SymbolTable<Symbol.Variable> variables = new SymbolTable<>();
    public boolean inLoop;
    public boolean inFunction;
    @Nullable
    public Type returnType;
    public boolean inClass;
    @Nullable
    public Type thisType;

    protected abstract GlobalScope getGlobalScope();

    public abstract Scope getParent();

    public abstract Symbol.Class getClass(Type type);

    public abstract Symbol.Function resolveFunction(String name, Position position);

    public abstract Symbol.Variable resolveVariable(String name, Position position);

    public abstract Symbol.TypeName resolveTypeName(String name, Position position);

    // local variables don't support forward references
    // we provide a method to declare them out of constructor
    // otherwise, we should declare them in constructor
    public void declareLocalVariables(StatementNode.VariableDeclarations variableDeclarations) {
        variableDeclarations.variables.forEach(declaration -> variables.declare(new Symbol.Variable(this, declaration)));
    }
}