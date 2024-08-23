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

    private ChildScope(Scope parent) { // create an empty child scope
        this.parent = parent;
        this.globalScope = parent.getGlobalScope();
        this.loopLabelName = parent.loopLabelName;
        this.returnType = parent.returnType;
        this.classType = parent.classType;
        this.namer = parent.namer;
    }

    public ChildScope(Scope parent, DefinitionAst.Class classNode) {
        this(parent);
        classNode.functions.forEach(this::declareFunction);
        classType = classNode.symbol;
    }

    public ChildScope(Scope parent, DefinitionAst.Function functionNode) {
        this(parent);
        returnType = new Type(this, functionNode.returnType);
        namer = new SemanticNamer();
    }

    // you should add local variables manually
    public ChildScope(Scope parent, StatementAst.For forNode) {
        this(parent);
        Objects.requireNonNull(namer); // namer should be set in function scope
        namer.setFor(forNode);
        loopLabelName = forNode.labelName;
    }

    public ChildScope(Scope parent, StatementAst.While whileNode) {
        this(parent);
        Objects.requireNonNull(namer); // namer should be set in function scope
        namer.setWhile(whileNode);
        loopLabelName = whileNode.labelName;
    }

    public ChildScope(Scope parent, StatementAst.Block blockNode) {
        this(parent);
    }

    public ChildScope(Scope parent, StatementAst.If ifNode) {
        this(parent);
        Objects.requireNonNull(namer); // namer should be set in function scope
        namer.setIf(ifNode);
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
