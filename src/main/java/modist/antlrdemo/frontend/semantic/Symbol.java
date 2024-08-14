package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.error.SemanticException;
import modist.antlrdemo.frontend.metadata.Position;
import modist.antlrdemo.frontend.syntax.node.DeclarationNode;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public abstract sealed class Symbol {
    public final String name;
    public final Position position;

    private Symbol(String name, Position position) {
        this.name = name;
        this.position = position;
    }

    // for built-in
    public Symbol(String name) {
        this.name = name;
        this.position = Position.BUILTIN;
    }

    public Symbol(DeclarationNode declaration) {
        this(declaration.name, declaration.position);
    }

    public static final class Class extends Symbol {
        @Nullable
        public final Function constructor;
        public final SymbolTable<Function> functions = new SymbolTable<>();
        public final SymbolTable<Variable> variables = new SymbolTable<>();

        public Class(Scope scope, DeclarationNode.Class declaration) {
            super(declaration);
            this.constructor = declaration.constructor != null ? new Function(scope, declaration.constructor) : null;
            if (this.constructor != null && !this.constructor.name.equals(this.name)) {
                throw new SemanticException("Constructor name must be the same as the class name", this.constructor.position);
            }
            declaration.functions.forEach(function -> functions.declare(new Function(scope, function)));
            declaration.variables.forEach(variable -> variables.declare(new Variable(scope, variable)));
        }

        // for built-in
        public Class(String name, @Nullable Function constructor, Function[] functions, Variable[] variables) {
            super(name);
            this.constructor = constructor;
            Arrays.stream(functions).forEach(this.functions::declare);
            Arrays.stream(variables).forEach(this.variables::declare);
        }
    }

    public static final class Function extends Symbol {
        // we shouldn't use Type.NULL here because we need to distinguish between a function that returns nothing and a function that returns null
        @Nullable
        public final Type returnType;
        public final SymbolTable.Ordered<Variable> parameters = new SymbolTable.Ordered<>();

        public Function(Scope scope, DeclarationNode.Function declaration) {
            super(declaration);
            this.returnType = declaration.returnType != null ? new Type(scope, declaration.returnType) : null;
            declaration.parameters.forEach(parameter -> parameters.declare(new Variable(scope, parameter)));
        }

        // for built-in
        public Function(String name, @Nullable Type returnType, Variable[] parameters) {
            super(name);
            this.returnType = returnType;
            Arrays.stream(parameters).forEach(this.parameters::declare);
        }
    }

    public static final class Variable extends Symbol {
        public final Type type;

        public Variable(Scope scope, DeclarationNode.Variable declaration) {
            super(declaration);
            this.type = new Type(scope, declaration.type);
        }

        // for built-in
        public Variable(String name, Type type) {
            super(name);
            this.type = type;
        }
    }

    // we need to first declare the type before we can use it in other symbols. every type corresponds to a class
    public static final class TypeName extends Symbol {
        // primitive types are built-in and are not pointers
        public final boolean primitive;
        public TypeName(DeclarationNode.Class declaration) {
            super(declaration);
            this.primitive = false;
        }

        // for built-in
        public TypeName(String name) {
            super(name);
            this.primitive = true;
        }
    }
}
