package modist.antlrdemo.frontend.semantic.scope;

import modist.antlrdemo.frontend.error.CompileException;
import modist.antlrdemo.frontend.metadata.Position;
import modist.antlrdemo.frontend.semantic.BuiltinFeatures;
import modist.antlrdemo.frontend.semantic.Symbol;
import modist.antlrdemo.frontend.semantic.SymbolTable;
import modist.antlrdemo.frontend.semantic.Type;
import modist.antlrdemo.frontend.syntax.node.ProgramNode;

public class GlobalScope extends Scope {
    private final SymbolTable<Symbol.TypeName> typeNames = new SymbolTable<>();
    private final SymbolTable<Symbol.Class> classes = new SymbolTable<>();
    private final Symbol.Class arrayClass = BuiltinFeatures.ARRAY_CLASS; // a virtual class for arrays

    // you should add global variables manually
    public GlobalScope(ProgramNode program) {
        addBuiltInFeatures();
        program.classes.forEach(typeNode -> typeNames.declare(new Symbol.TypeName(typeNode)));
        program.classes.forEach(typeNode -> classes.declare(new Symbol.Class(this, typeNode)));
        program.functions.forEach(this::declareFunction);
        getMainFunction(program);
    }

    private void addBuiltInFeatures() {
        typeNames.declare(BuiltinFeatures.INT_TYPE_NAME);
        typeNames.declare(BuiltinFeatures.BOOL_TYPE_NAME);
        typeNames.declare(BuiltinFeatures.STRING_TYPE_NAME);
        typeNames.declare(BuiltinFeatures.VOID_TYPE_NAME);
        classes.declare(BuiltinFeatures.INT_CLASS);
        classes.declare(BuiltinFeatures.BOOL_CLASS);
        classes.declare(BuiltinFeatures.STRING_CLASS);
        classes.declare(BuiltinFeatures.VOID_CLASS);
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
            throw new CompileException("Main function not found", program.position);
        }
        Symbol.Function mainFunction = functions.get("main");
        if (!BuiltinFeatures.INT.equals(mainFunction.returnType)) {
            throw new CompileException("Main function must return int", mainFunction.position);
        }
        if (mainFunction.parameters.size() != 0) {
            throw new CompileException("Main function must have no parameters", mainFunction.position);
        }
    }

    @Override
    protected GlobalScope getGlobalScope() {
        return this;
    }

    @Override
    protected Symbol.TypeName getTypeName(String name) {
        return typeNames.get(name);
    }

    @Override
    public Scope getParent() {
        return null;
    }

    @Override
    public Symbol.Class resolveClass(Type type, Position position) {
        if (type.typeName() == null) {
            throw new CompileException("Null type is ambiguous and cannot be resolved for a certain class", position);
        }
        if (type.isArray()) {
            return arrayClass;
        }
        return classes.resolve(type.typeName().name, position);
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