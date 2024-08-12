package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.error.SemanticException;
import modist.antlrdemo.frontend.syntax.Position;
import modist.antlrdemo.frontend.syntax.node.ProgramNode;

// store global symbols like classes. Built-in features are also stored here.
// check function names
public class GlobalScope extends Scope {
    private final SymbolTable<Symbol.TypeName> typeNames = new SymbolTable<>();
    private final SymbolTable<Symbol.Class> classes = new SymbolTable<>();
    private final SymbolTable<Symbol.Function> arrayFunctions = new SymbolTable<>(); // array functions are stored in a separate table as they are not associated with any class
    private final Symbol.Function mainFunction;

    public GlobalScope(ProgramNode program) {
        addBuiltInFeatures();
        program.classes.forEach(typeNode -> typeNames.declare(new Symbol.TypeName(typeNode)));
        program.classes.forEach(typeNode -> classes.declare(new Symbol.Class(this, typeNode)));
        program.functions.forEach(functionNode -> functions.declare(new Symbol.Function(this, functionNode)));
        program.variables.forEach(variableNode -> variables.declare(new Symbol.Variable(this, variableNode)));
        mainFunction = getMainFunction(program);
    }

    private void addBuiltInFeatures() {
        typeNames.declare(BuiltinFeatures.INT);
        typeNames.declare(BuiltinFeatures.BOOL);
        typeNames.declare(BuiltinFeatures.STRING);
        classes.declare(BuiltinFeatures.INT_CLASS);
        classes.declare(BuiltinFeatures.BOOL_CLASS);
        classes.declare(BuiltinFeatures.STRING_CLASS);
        functions.declare(BuiltinFeatures.PRINT);
        functions.declare(BuiltinFeatures.PRINTLN);
        functions.declare(BuiltinFeatures.PRINT_INT);
        functions.declare(BuiltinFeatures.PRINTLN_INT);
        functions.declare(BuiltinFeatures.GET_STRING);
        functions.declare(BuiltinFeatures.GET_INT);
        functions.declare(BuiltinFeatures.TO_STRING);
        arrayFunctions.declare(BuiltinFeatures.ARRAY_SIZE);
    }

    private Symbol.Function getMainFunction(ProgramNode program) {
        if (!functions.contains("main")) {
            throw new SemanticException("Main function not found", program.position);
        }
        Symbol.Function mainFunction = functions.get("main");
        if (!BuiltinFeatures.INT_TYPE.equals(mainFunction.returnType)) {
            throw new SemanticException("Main function must return int", mainFunction.position);
        }
        if (mainFunction.parameters.size() != 0) {
            throw new SemanticException("Main function must have no parameters", mainFunction.position);
        }
        return mainFunction;
    }

    @Override
    protected GlobalScope getGlobalScope() {
        return this;
    }

    @Override
    public Symbol.Class resolveClass(String name, Position position) {
        return classes.resolve(name, position);
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