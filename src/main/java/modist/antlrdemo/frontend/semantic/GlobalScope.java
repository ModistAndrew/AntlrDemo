package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.error.SemanticException;
import modist.antlrdemo.frontend.metadata.Position;
import modist.antlrdemo.frontend.syntax.node.ProgramNode;

public class GlobalScope extends Scope {
    private final SymbolTable<Symbol.TypeName> typeNames = new SymbolTable<>();
    private final SymbolTable<Symbol.Class> classes = new SymbolTable<>();
    private final Symbol.Class arrayClass = BuiltinFeatures.ARRAY_CLASS; // a virtual class for arrays
    private final Symbol.Class nullClass = BuiltinFeatures.NULL_CLASS; // a virtual class for null

    public GlobalScope(ProgramNode program) {
        addBuiltInFeatures();
        program.classes.forEach(typeNode -> typeNames.declare(new Symbol.TypeName(typeNode)));
        program.classes.forEach(typeNode -> classes.declare(new Symbol.Class(this, typeNode)));
        program.functions.forEach(functionNode -> functions.declare(new Symbol.Function(this, functionNode)));
        program.variables.forEach(variableNode -> variables.declare(new Symbol.Variable(this, variableNode)));
        getMainFunction(program);
    }

    private void addBuiltInFeatures() {
        typeNames.declare(BuiltinFeatures.INT_TYPE_NAME);
        typeNames.declare(BuiltinFeatures.BOOL_TYPE_NAME);
        typeNames.declare(BuiltinFeatures.STRING_TYPE_NAME);
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
    }

    private void getMainFunction(ProgramNode program) {
        if (!functions.contains("main")) {
            throw new SemanticException("Main function not found", program.position);
        }
        Symbol.Function mainFunction = functions.get("main");
        if (!BuiltinFeatures.INT.equals(mainFunction.returnType)) {
            throw new SemanticException("Main function must return int", mainFunction.position);
        }
        if (mainFunction.parameters.size() != 0) {
            throw new SemanticException("Main function must have no parameters", mainFunction.position);
        }
    }

    @Override
    protected GlobalScope getGlobalScope() {
        return this;
    }

    @Override
    public Scope getParent() {
        return null;
    }

    @Override
    public Symbol.Class getClass(Type type) {
        if (type.isArray()) {
            return arrayClass;
        }
        if (type.typeName() == null) {
            return nullClass;
        }
        return classes.get(type.typeName().name);
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