package modist.antlrdemo.frontend.semantic.scope;

import modist.antlrdemo.frontend.semantic.error.CompileException;
import modist.antlrdemo.frontend.semantic.BuiltinFeatures;
import modist.antlrdemo.frontend.semantic.Symbol;
import modist.antlrdemo.frontend.semantic.SymbolTable;
import modist.antlrdemo.frontend.ast.node.ProgramAst;
import org.jetbrains.annotations.Nullable;

public class GlobalScope extends Scope {
    private final SymbolTable<Symbol.TypeName> typeNames = new SymbolTable<>();

    public GlobalScope(ProgramAst program) {
        super(null, null, null, null);
        addBuiltInFeatures();
        program.classes.forEach(typeNode -> typeNames.declare(new Symbol.TypeName(typeNode)));
        program.classes.forEach(typeNode -> typeNode.symbol.createClass(this, typeNode));
        program.functions.forEach(this::declareFunction);
        checkMainFunction(program);
    }

    private void addBuiltInFeatures() {
        typeNames.declare(BuiltinFeatures.INT_TYPE_NAME);
        typeNames.declare(BuiltinFeatures.BOOL_TYPE_NAME);
        typeNames.declare(BuiltinFeatures.STRING_TYPE_NAME);
        typeNames.declare(BuiltinFeatures.VOID_TYPE_NAME);
        functions.declare(BuiltinFeatures.PRINT);
        functions.declare(BuiltinFeatures.PRINTLN);
        functions.declare(BuiltinFeatures.PRINT_INT);
        functions.declare(BuiltinFeatures.PRINTLN_INT);
        functions.declare(BuiltinFeatures.GET_STRING);
        functions.declare(BuiltinFeatures.GET_INT);
        functions.declare(BuiltinFeatures.TO_STRING);
    }

    private void checkMainFunction(ProgramAst program) {
        if (!functions.contains("main")) {
            throw new CompileException("Main function not found", program.position);
        }
        Symbol.Function mainFunction = functions.get("main");
        if (!mainFunction.returnType.equals(BuiltinFeatures.INT)) {
            throw new CompileException("Main function must return int", mainFunction.position);
        }
        if (!mainFunction.parameters.isEmpty()) {
            throw new CompileException("Main function must have no parameters", mainFunction.position);
        }
        mainFunction.isMain = true;
    }

    @Override
    protected GlobalScope getGlobalScope() {
        return this;
    }

    @Override
    @Nullable
    protected Symbol.TypeName getTypeName(String name) {
        return typeNames.get(name);
    }

    @Override
    public Scope getParent() {
        return null;
    }

    @Override
    public Symbol.TypeName resolveTypeName(String name) {
        return typeNames.resolve(name);
    }

    @Override
    public Symbol.Function resolveFunction(String name) {
        return functions.resolve(name);
    }

    @Override
    public Symbol.Variable resolveVariable(String name) {
        return variables.resolve(name);
    }
}