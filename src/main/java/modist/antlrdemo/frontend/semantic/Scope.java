package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.metadata.Position;
import modist.antlrdemo.frontend.syntax.node.DeclarationNode;
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

    // variables don't support forward references
    // we provide a method to declare them out of constructor
    // what's more, we check the initializer here
    // should be used in the constructor and the method that declares local variables
    public void declareVariable(DeclarationNode.Variable variableDeclaration) {
        if (variableDeclaration.initializer != null) {
            new ExpressionType.Builder(this).joinType(variableDeclaration.initializer, new Type(this, variableDeclaration.type));
        }
        variables.declare(new Symbol.Variable(this, variableDeclaration));
    }
}