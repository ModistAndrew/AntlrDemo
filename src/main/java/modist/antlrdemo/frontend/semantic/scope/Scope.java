package modist.antlrdemo.frontend.semantic.scope;

import modist.antlrdemo.frontend.error.MultipleDefinitionsException;
import modist.antlrdemo.frontend.semantic.Symbol;
import modist.antlrdemo.frontend.semantic.SymbolRenamer;
import modist.antlrdemo.frontend.semantic.SymbolTable;
import modist.antlrdemo.frontend.semantic.Type;
import modist.antlrdemo.frontend.ast.node.DefinitionAst;
import org.jetbrains.annotations.Nullable;

public abstract class Scope {
    // declaration order: typeName -> class -> function -> variable(out of scope constructor)
    // build scope and store symbols in ast nodes
    protected final SymbolTable<Symbol.Function> functions = new SymbolTable<>();
    protected final SymbolTable<Symbol.Variable> variables = new SymbolTable<>();
    public boolean inLoop;
    @Nullable
    public Type returnType;
    @Nullable
    public Symbol.TypeName thisType;
    @Nullable
    protected SymbolRenamer renamer; // present when in function scope

    protected abstract GlobalScope getGlobalScope();

    @Nullable
    protected abstract Symbol.TypeName getTypeName(String name);

    public abstract Scope getParent();

    public abstract Symbol.Function resolveFunction(String name);

    public abstract Symbol.Variable resolveVariable(String name);

    public abstract Symbol.TypeName resolveTypeName(String name);

    // variables don't support forward references
    // we provide a method to declare them out of constructor
    // check name conflict here
    // we check the initializer after symbol is created but before it is declared, for convenience
    // we rename the symbol here
    // if symbol != null, we don't create a new symbol as it has been created in class symbol or function symbol and has been checked and renamed
    public void declareVariable(DefinitionAst.Variable variableDefinition) {
        Symbol.Variable symbol;
        if (variableDefinition.symbol == null) {
            symbol = new Symbol.Variable(this, variableDefinition);
            if (renamer != null) {
                renamer.setLocalVariable(symbol);
            }
            if (variableDefinition.initializer != null) {
                new Type.Builder(this).tryMatchExpression(variableDefinition.initializer, symbol.type);
            }
        } else {
            symbol = variableDefinition.symbol;
        }
        Symbol.Function function = functions.get(variableDefinition.name);
        if (function != null) {
            throw new MultipleDefinitionsException(symbol, function);
        }
        variables.declare(symbol);
    }

    // check name conflict here
    // if symbol != null, we don't create a new symbol as it has been created in class symbol and has been renamed
    protected void declareFunction(DefinitionAst.Function functionDefinition) {
        Symbol.Function symbol = functionDefinition.symbol == null ? new Symbol.Function(this, functionDefinition) : functionDefinition.symbol;
        Symbol.TypeName typeName = getTypeName(functionDefinition.name);
        if (typeName != null) {
            throw new MultipleDefinitionsException(symbol, typeName);
        }
        functions.declare(symbol);
    }
}