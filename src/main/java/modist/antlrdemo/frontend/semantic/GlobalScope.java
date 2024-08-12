package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.syntax.Position;
import modist.antlrdemo.frontend.syntax.node.ProgramNode;

// store global symbols like classes. Built-in features are also stored here.
public class GlobalScope extends Scope {
    private final SymbolTable<Symbol.TypeName> typeNames = new SymbolTable<>();
    private final SymbolTable<Symbol.Class> classes = new SymbolTable<>();

    public GlobalScope(ProgramNode program) {
        // TODO: add built-in features
        program.classes.forEach(typeNode -> typeNames.declare(typeNode.name, new Symbol.TypeName(typeNode)));
        program.classes.forEach(typeNode -> classes.declare(typeNode.name, new Symbol.Class(this, typeNode)));
        program.functions.forEach(functionNode -> functions.declare(functionNode.name, new Symbol.Function(this, functionNode)));
        program.variables.forEach(variableNode -> variables.declare(variableNode.name, new Symbol.Variable(this, variableNode)));
    }

    @Override
    protected GlobalScope getGlobalScope() {
        return this;
    }

    @Override
    public Symbol.TypeName resolveTypeName(String name, Position position) {
        return typeNames.resolve(name, position);
    }

    @Override
    public Symbol.Function resolveFunction(String name, Position position) {
        return functions.resolve(name, position);
    }

    @Override
    public Symbol.Variable resolveVariable(String name, Position position) {
        return variables.resolve(name, position);
    }
}