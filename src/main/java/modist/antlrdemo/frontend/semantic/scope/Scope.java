package modist.antlrdemo.frontend.semantic.scope;

import modist.antlrdemo.frontend.semantic.error.MultipleDefinitionsException;
import modist.antlrdemo.frontend.semantic.Symbol;
import modist.antlrdemo.frontend.semantic.SemanticNamer;
import modist.antlrdemo.frontend.semantic.SymbolTable;
import modist.antlrdemo.frontend.semantic.Type;
import modist.antlrdemo.frontend.ast.node.DefinitionAst;
import org.jetbrains.annotations.Nullable;

public abstract class Scope {
    // declaration order: typeName -> class -> function -> variable(out of scope constructor)
    // build scope and store symbols in ast nodes
    protected final SymbolTable<Symbol.Function> functions = new SymbolTable<>();
    protected final SymbolTable<Symbol.Variable> variables = new SymbolTable<>();
    @Nullable
    public String loopLabelName; // present when in loop scope
    @Nullable
    public Type returnType; // present when in function scope
    @Nullable
    public Symbol.TypeName classType; // present when in class scope
    @Nullable
    protected SemanticNamer namer; // present when in function scope

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
    // if symbol != null, we don't create a new symbol as it has been created in class symbol or function symbol
    // we need to rename the symbol if it is a parameter or local variable
    // skip when it is a class member or global variable
    public void declareVariable(DefinitionAst.Variable variableDefinition) {
        Symbol.Variable symbol = variableDefinition.symbol == null ? new Symbol.Variable(this, variableDefinition) : variableDefinition.symbol;
        // class member needs no renaming
        // global variable is renamed when created
        // local variable is renamed here
        // parameter should be renamed as a local variable; the real parameter will be stored into that local variable
        if (symbol.classType == null && namer != null) {
            namer.setLocalVariable(symbol);
        }
        if (variableDefinition.initializer != null) { // for global or local variable. member or parameter have no initializer
            new Type.Builder(this).tryMatchExpression(variableDefinition.initializer, symbol.type);
        }
        Symbol.Function function = functions.get(variableDefinition.name);
        if (function != null) {
            throw new MultipleDefinitionsException(symbol, function);
        }
        variables.declare(symbol);
    }

    // check name conflict here
    // if symbol != null, we don't create a new symbol as it has been created in class symbol
    // we needn't rename the symbol here as it is already renamed in class symbol or when created
    protected void declareFunction(DefinitionAst.Function functionDefinition) {
        Symbol.Function symbol = functionDefinition.symbol == null ? new Symbol.Function(this, functionDefinition) : functionDefinition.symbol;
        Symbol.TypeName typeName = getTypeName(functionDefinition.name);
        if (typeName != null) {
            throw new MultipleDefinitionsException(symbol, typeName);
        }
        functions.declare(symbol);
    }
}