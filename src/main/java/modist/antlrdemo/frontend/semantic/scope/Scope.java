package modist.antlrdemo.frontend.semantic.scope;

import modist.antlrdemo.frontend.semantic.error.MultipleDefinitionsException;
import modist.antlrdemo.frontend.semantic.Symbol;
import modist.antlrdemo.frontend.semantic.SemanticNamer;
import modist.antlrdemo.frontend.semantic.SymbolTable;
import modist.antlrdemo.frontend.semantic.Type;
import modist.antlrdemo.frontend.ast.node.DefinitionAst;
import org.jetbrains.annotations.Nullable;

public abstract class Scope {
    protected final SymbolTable<Symbol.Function> functions = new SymbolTable<>();
    protected final SymbolTable<Symbol.Variable> variables = new SymbolTable<>();
    @Nullable
    public final Symbol.TypeName classType; // present when in class scope
    @Nullable
    protected final SemanticNamer namer; // present when in function scope
    @Nullable
    public final Type returnType; // present when in function scope
    @Nullable
    public final String loopLabelName; // present when in loop scope
    public boolean returned; // spread upwards in popScope to check if a function has return statement

    protected Scope(@Nullable Symbol.TypeName classType, @Nullable SemanticNamer namer, @Nullable Type returnType, @Nullable String loopLabelName) {
        this.loopLabelName = loopLabelName;
        this.returnType = returnType;
        this.classType = classType;
        this.namer = namer;
    }

    protected abstract GlobalScope getGlobalScope();

    @Nullable
    protected abstract Symbol.TypeName getTypeName(String name);

    @Nullable
    public abstract Scope getParent();

    public abstract Symbol.Function resolveFunction(String name);

    public abstract Symbol.Variable resolveVariable(String name);

    public abstract Symbol.TypeName resolveTypeName(String name);

    // variables don't support forward references
    // we provide a method to declare them out of constructor
    // also check name conflict here
    // symbol may have been created in Symbol.Class or Symbol.Function but not declared
    // create if not exists (for global or local variables), then declare
    public void declareVariable(DefinitionAst.Variable variableDefinition) {
        Symbol.Variable symbol = variableDefinition.symbol != null ? variableDefinition.symbol :
                namer == null ? new Symbol.Variable(this, variableDefinition) : new Symbol.Variable(this, variableDefinition, namer);
        Symbol.Function function = functions.get(variableDefinition.name);
        if (function != null) {
            throw new MultipleDefinitionsException(symbol, function);
        }
        variables.declare(symbol);
    }

    // constructor should use this to check name conflict
    // symbol may have been created in Symbol.Class but not declared
    // create if not exists (for global functions), then declare
    protected void declareFunction(DefinitionAst.Function functionDefinition) {
        Symbol.Function symbol = functionDefinition.symbol == null ? new Symbol.Function(this, functionDefinition) : functionDefinition.symbol;
        Symbol.TypeName typeName = getTypeName(functionDefinition.name);
        if (typeName != null) {
            throw new MultipleDefinitionsException(symbol, typeName);
        }
        functions.declare(symbol);
    }
}