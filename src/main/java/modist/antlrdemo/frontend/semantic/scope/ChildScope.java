package modist.antlrdemo.frontend.semantic.scope;

import modist.antlrdemo.frontend.semantic.Symbol;
import modist.antlrdemo.frontend.semantic.SemanticNamer;
import modist.antlrdemo.frontend.semantic.Type;
import modist.antlrdemo.frontend.ast.node.DefinitionAst;
import modist.antlrdemo.frontend.ast.node.StatementAst;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ChildScope extends Scope {
    private final Scope parent; // the direct parent scope
    private final GlobalScope globalScope; // the global scope

    private ChildScope(Scope parent, Symbol.TypeName classType, SemanticNamer namer, Type returnType, String loopLabelName) {
        super(classType, namer, returnType, loopLabelName);
        this.parent = parent;
        this.globalScope = parent.getGlobalScope();
    }

    public ChildScope(Scope parent, DefinitionAst.Class classNode) {
        this(parent, classNode.symbol, parent.namer, parent.returnType, parent.loopLabelName);
        classNode.functions.forEach(this::declareFunction);
    }

    public ChildScope(Scope parent, DefinitionAst.Function functionNode) {
        this(parent, parent.classType, new SemanticNamer(), new Type(parent, functionNode.returnType), parent.loopLabelName);
    }

    // you should add local variables manually
    public ChildScope(Scope parent, StatementAst.For forNode) {
        this(parent, parent.classType, parent.namer, parent.returnType, Objects.requireNonNull(parent.namer).loopLabel());
        forNode.labelName = loopLabelName;
    }

    public ChildScope(Scope parent, StatementAst.While whileNode) {
        this(parent, parent.classType, parent.namer, parent.returnType, Objects.requireNonNull(parent.namer).loopLabel());
        whileNode.labelName = loopLabelName;
    }

    public ChildScope(Scope parent, StatementAst.Block blockNode) {
        this(parent, parent.classType, parent.namer, parent.returnType, parent.loopLabelName);
    }

    public ChildScope(Scope parent, StatementAst.If ifNode) {
        this(parent, parent.classType, parent.namer, parent.returnType, parent.loopLabelName);
        ifNode.labelName = Objects.requireNonNull(parent.namer).ifLabel();
    }

    @Override
    protected GlobalScope getGlobalScope() {
        return globalScope;
    }

    @Override
    @Nullable
    protected Symbol.TypeName getTypeName(String name) {
        return globalScope.getTypeName(name);
    }

    @Override
    public Scope getParent() {
        return parent;
    }

    @Override
    public Symbol.TypeName resolveTypeName(String name) {
        return globalScope.resolveTypeName(name);
    }

    @Override
    public Symbol.Function resolveFunction(String name) {
        return functions.contains(name) ? functions.get(name) : parent.resolveFunction(name);
    }

    @Override
    public Symbol.Variable resolveVariable(String name) {
        return variables.contains(name) ? variables.get(name) : parent.resolveVariable(name);
    }
}
