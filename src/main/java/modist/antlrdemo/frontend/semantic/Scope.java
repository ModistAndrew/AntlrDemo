package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.error.SymbolRedefinedException;
import modist.antlrdemo.frontend.metadata.Position;
import modist.antlrdemo.frontend.syntax.node.DeclarationNode;
import org.jetbrains.annotations.Nullable;

public abstract class Scope {
    // declaration order: typeName -> class -> function -> variable
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

    protected abstract Symbol.TypeName getTypeName(String name);

    public abstract Scope getParent();

    // should not throw SymbolUndefinedException, but may throw when type is null type
    public abstract Symbol.Class resolveClass(Type type, Position position);

    public abstract Symbol.Function resolveFunction(String name, Position position);

    public abstract Symbol.Variable resolveVariable(String name, Position position);

    public abstract Symbol.TypeName resolveTypeName(String name, Position position);

    // variables don't support forward references
    // we provide a method to declare them out of constructor
    // what's more, we check the initializer and name conflict here
    public void declareVariable(DeclarationNode.Variable variableDeclaration) {
        if (variableDeclaration.initializer != null) {
            new Type.Builder(this).testExpressionType(variableDeclaration.initializer, new Type(this, variableDeclaration.type));
        }
        Symbol.Function function = functions.get(variableDeclaration.name);
        if (function != null) {
            throw new SymbolRedefinedException(variableDeclaration.name, variableDeclaration.position, function.position);
        }
        variables.declare(new Symbol.Variable(this, variableDeclaration));
    }

    // check name conflict here
    protected void declareFunction(DeclarationNode.Function functionDeclaration) {
        Symbol.TypeName typeName = getTypeName(functionDeclaration.name);
        if (typeName != null) {
            throw new SymbolRedefinedException(functionDeclaration.name, functionDeclaration.position, typeName.position);
        }
        functions.declare(new Symbol.Function(this, functionDeclaration));
    }
}