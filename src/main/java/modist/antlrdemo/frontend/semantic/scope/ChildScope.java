package modist.antlrdemo.frontend.semantic.scope;

import modist.antlrdemo.frontend.semantic.Symbol;
import modist.antlrdemo.frontend.semantic.Type;
import modist.antlrdemo.frontend.syntax.node.DeclarationNode;
import modist.antlrdemo.frontend.syntax.node.StatementNode;
import org.jetbrains.annotations.Nullable;

public class ChildScope extends Scope {
    private final Scope parent; // the direct parent scope
    private final GlobalScope globalScope; // the global scope

    private ChildScope(Scope parent) { // create an empty child scope
        this.parent = parent;
        this.globalScope = parent.getGlobalScope();
        this.inLoop = parent.inLoop;
        this.returnType = parent.returnType;
        this.thisType = parent.thisType;
    }

    public ChildScope(Scope parent, DeclarationNode.Class classNode) {
        this(parent);
        classNode.functions.forEach(this::declareFunction);
        thisType = new Type(classNode.typeNameSymbol);
    }

    public ChildScope(Scope parent, DeclarationNode.Function functionNode) {
        this(parent);
        returnType = new Type(this, functionNode.returnType);
    }

    // you should add local variables manually
    public ChildScope(Scope parent, StatementNode.For forNode) {
        this(parent);
        inLoop = true;
    }

    public ChildScope(Scope parent, StatementNode.While whileNode) {
        this(parent);
        inLoop = true;
    }

    public ChildScope(Scope parent, StatementNode.Block blockNode) {
        this(parent);
    }

    public ChildScope(Scope parent, StatementNode.If ifNode) {
        this(parent);
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
    public Symbol.Class resolveClass(Type type) {
        return globalScope.resolveClass(type);
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
