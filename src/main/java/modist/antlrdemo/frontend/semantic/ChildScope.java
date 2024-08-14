package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.metadata.Position;
import modist.antlrdemo.frontend.syntax.node.DeclarationNode;
import modist.antlrdemo.frontend.syntax.node.StatementNode;

public class ChildScope extends Scope {
    private final Scope parent; // the direct parent scope
    private final GlobalScope globalScope; // the global scope

    private ChildScope(Scope parent) { // create an empty child scope
        this.parent = parent;
        this.globalScope = parent.getGlobalScope();
        this.inLoop = parent.inLoop;
        this.inFunction = parent.inFunction;
        this.returnType = parent.returnType;
        this.inClass = parent.inClass;
        this.thisType = parent.thisType;
    }

    public ChildScope(Scope parent, DeclarationNode.Class classNode) {
        this(parent);
        classNode.functions.forEach(function -> functions.declare(new Symbol.Function(this, function)));
        inClass = true;
        thisType = new Type(this, classNode);
    }

    public ChildScope(Scope parent, DeclarationNode.Function functionNode) {
        this(parent);
        inFunction = true;
        returnType = functionNode.returnType != null ? new Type(this, functionNode.returnType) : null;
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

    @Override
    protected GlobalScope getGlobalScope() {
        return globalScope;
    }

    @Override
    public Scope getParent() {
        return parent;
    }

    @Override
    public Symbol.Class getClass(Type type) {
        return globalScope.getClass(type);
    }

    @Override
    public Symbol.TypeName resolveTypeName(String name, Position position) {
        return globalScope.resolveTypeName(name, position);
    }

    @Override
    public Symbol.Function resolveFunction(String name, Position position) {
        return functions.contains(name) ? functions.get(name) : parent.resolveFunction(name, position);
    }

    @Override
    public Symbol.Variable resolveVariable(String name, Position position) {
        return variables.contains(name) ? variables.get(name) : parent.resolveVariable(name, position);
    }
}
