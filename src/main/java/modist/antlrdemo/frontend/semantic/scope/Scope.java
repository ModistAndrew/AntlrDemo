package modist.antlrdemo.frontend.semantic.scope;

import modist.antlrdemo.frontend.error.MultipleDefinitionsException;
import modist.antlrdemo.frontend.semantic.Symbol;
import modist.antlrdemo.frontend.semantic.SymbolTable;
import modist.antlrdemo.frontend.semantic.Type;
import modist.antlrdemo.frontend.syntax.node.DeclarationNode;
import org.jetbrains.annotations.Nullable;

public abstract class Scope {
    // declaration order: typeName -> class -> function -> variable
    protected final SymbolTable<Symbol.Function> functions = new SymbolTable<>();
    protected final SymbolTable<Symbol.Variable> variables = new SymbolTable<>();
    public boolean inLoop;
    @Nullable
    public Type returnType;
    @Nullable
    public Type thisType;

    protected abstract GlobalScope getGlobalScope();

    @Nullable
    protected abstract Symbol.TypeName getTypeName(String name);

    public abstract Scope getParent();

    // should not throw UndefinedIdentifierException, but may throw when type is null type
    public abstract Symbol.Class resolveClass(Type type);

    public abstract Symbol.Function resolveFunction(String name);

    public abstract Symbol.Variable resolveVariable(String name);

    public abstract Symbol.TypeName resolveTypeName(String name);

    // variables don't support forward references
    // we provide a method to declare them out of constructor
    // what's more, we check the initializer and name conflict here
    public void declareVariable(DeclarationNode.Variable variableDeclaration) {
        if (variableDeclaration.initializer != null) {
            new Type.Builder(this).tryMatchExpression(variableDeclaration.initializer, new Type(this, variableDeclaration.type));
        }
        Symbol.Function function = functions.get(variableDeclaration.name);
        Symbol.Variable symbol = new Symbol.Variable(this, variableDeclaration);
        if (function != null) {
            throw new MultipleDefinitionsException(symbol, function);
        }
        variables.declare(symbol);
    }

    // check name conflict here
    protected void declareFunction(DeclarationNode.Function functionDeclaration) {
        Symbol.TypeName typeName = getTypeName(functionDeclaration.name);
        Symbol.Function symbol = new Symbol.Function(this, functionDeclaration);
        if (typeName != null) {
            throw new MultipleDefinitionsException(symbol, typeName);
        }
        functions.declare(symbol);
    }
}