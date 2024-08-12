package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.syntax.Position;
import modist.antlrdemo.frontend.syntax.node.DeclarationNode;
import org.jetbrains.annotations.Nullable;

public abstract sealed class Symbol {
    public final String name;
    public final Position position;

    public Symbol(String name, Position position) {
        this.name = name;
        this.position = position;
    }

    public Symbol(DeclarationNode declaration) {
        this(declaration.name, declaration.position);
    }

    public static final class TypeName extends Symbol {
        // we need to first declare the type before we can use it in other symbols
        public TypeName(DeclarationNode.Class declaration) {
            super(declaration);
        }
    }

    public static final class Class extends Symbol {
        @Nullable
        public final Function constructor;
        public final SymbolTable<Function> functions = new SymbolTable<>();
        public final SymbolTable<Variable> variables = new SymbolTable<>();

        public Class(Scope scope, DeclarationNode.Class declaration) {
            super(declaration);
            this.constructor = declaration.constructor != null ? new Function(scope, declaration.constructor) : null;
            declaration.functions.forEach(function -> functions.declare(function.name, new Function(scope, function)));
            declaration.variables.forEach(variable -> variables.declare(variable.name, new Variable(scope, variable)));
        }
    }

    public static final class Function extends Symbol {
        @Nullable
        public final Type returnType;
        public final SymbolTable<Variable> parameters = new SymbolTable<>();

        public Function(Scope scope, DeclarationNode.Function declaration) {
            super(declaration);
            this.returnType = declaration.returnType != null ? new Type(scope, declaration.returnType) : null;
            declaration.parameters.forEach(parameter -> parameters.declare(parameter.name, new Variable(scope, parameter)));
        }

        public Function(Scope scope, DeclarationNode.Constructor declaration) {
            super(declaration);
            this.returnType = null;
        }
    }

    public static final class Variable extends Symbol {
        public final Type type;

        public Variable(Scope scope, DeclarationNode.Variable declaration) {
            super(declaration);
            this.type = new Type(scope, declaration.type);
        }

        public Variable(Scope scope, DeclarationNode.Parameter declaration) {
            super(declaration);
            this.type = new Type(scope, declaration.type);
        }
    }
}
