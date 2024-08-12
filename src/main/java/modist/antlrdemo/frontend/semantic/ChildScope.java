package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.Position;
import modist.antlrdemo.frontend.syntax.node.DeclarationNode;
import modist.antlrdemo.frontend.syntax.node.StatementNode;
import org.jetbrains.annotations.Nullable;

public sealed class ChildScope extends Scope {
    private final Scope parent; // the direct parent scope
    private final GlobalScope globalScope; // the global scope

    public ChildScope(Scope parent) { // create an empty child scope
        this.parent = parent;
        this.globalScope = parent.getGlobalScope();
    }

    @Override
    protected GlobalScope getGlobalScope() {
        return globalScope;
    }

    @Override
    public Symbol.Class resolveClass(String name, Position position) {
        return globalScope.resolveClass(name, position);
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

    public static final class Class extends ChildScope {
        public final Type classType;

        public Class(Scope parent, DeclarationNode.Class classNode) {
            super(parent);
            classNode.functions.forEach(function -> functions.declare(new Symbol.Function(this, function)));
            classNode.variables.forEach(variable -> variables.declare(new Symbol.Variable(this, variable)));
            classType = new Type(this, classNode);
        }
    }

    public static final class Function extends ChildScope {
        @Nullable
        public final Type returnType;

        public Function(Scope parent, DeclarationNode.Function functionNode) {
            super(parent);
            functionNode.parameters.forEach(parameter -> variables.declare(new Symbol.Variable(this, parameter)));
            returnType = functionNode.returnType != null ? new Type(this, functionNode.returnType) : null;
        }

        public Function(Scope parent, DeclarationNode.Constructor constructorNode) {
            super(parent);
            returnType = null;
        }
    }

    // only in loop can we use break and continue
    public static final class Loop extends ChildScope {
        public Loop(Scope parent, StatementNode.For forNode) {
            super(parent);
            if (forNode.initialization instanceof StatementNode.VariableDeclarations variableDeclarations) {
                declareLocalVariables(variableDeclarations);
            }
        }

        public Loop(Scope parent, StatementNode.While whileNode) {
            super(parent);
        }
    }
}
