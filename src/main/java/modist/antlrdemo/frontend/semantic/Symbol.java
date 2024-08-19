package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.error.CompileException;
import modist.antlrdemo.frontend.error.InvalidTypeException;
import modist.antlrdemo.frontend.error.MultipleDefinitionsException;
import modist.antlrdemo.frontend.ast.metadata.Position;
import modist.antlrdemo.frontend.semantic.scope.Scope;
import modist.antlrdemo.frontend.ast.node.DeclarationNode;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public abstract class Symbol {
    public final String name;
    public final Position position;

    public Symbol(String name, Position position) {
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

    public static class Class extends Symbol {
        @Nullable
        public final Function constructor;
        // for reference
        public final SymbolTable<Function> functions = new SymbolTable<>();
        public final SymbolTable<Variable> variables = new SymbolTable<>();

        public Class(Scope scope, DeclarationNode.Class declaration) {
            super(declaration);
            Function constructorTemp = null;
            for (DeclarationNode.Function constructorNode : declaration.constructors) {
                if (!constructorNode.name.equals(declaration.name)) {
                    throw new CompileException("Constructor name must be the same as the class name", constructorNode.position);
                }
                Function constructorSymbol = new Function(scope, constructorNode);
                if (constructorTemp != null) {
                    throw new MultipleDefinitionsException(constructorSymbol, constructorTemp);
                }
                constructorTemp = constructorSymbol;
            }
            this.constructor = constructorTemp;
            declaration.functions.forEach(function -> functions.declare(new Function(scope, function)));
            declaration.variables.forEach(variable -> variables.declare(new Variable(scope, variable)));
            declaration.symbol = this;
        }

        // for built-in
        public Class(String name, @Nullable Function constructor, Function[] functions, Variable[] variables) {
            super(name);
            this.constructor = constructor;
            Arrays.stream(functions).forEach(this.functions::declare);
            Arrays.stream(variables).forEach(this.variables::declare);
        }
    }

    public static class Function extends Symbol {
        public final Type returnType;
        public final SymbolTable.Ordered<Variable> parameters = new SymbolTable.Ordered<>();
        public boolean isMain;

        public Function(Scope scope, DeclarationNode.Function declaration) {
            super(declaration);
            this.returnType = new Type(scope, declaration.returnType);
            declaration.parameters.forEach(parameter -> parameters.declare(new Variable(scope, parameter)));
            declaration.symbol = this;
        }

        // for built-in
        public Function(String name, Type returnType, Variable[] parameters) {
            super(name);
            this.returnType = returnType;
            Arrays.stream(parameters).forEach(this.parameters::declare);
        }

        public boolean shouldReturn() {
            return !returnType.isVoid() && !isMain;
        }
    }

    public static class Variable extends Symbol {
        public final Type type;

        public Variable(Scope scope, DeclarationNode.Variable declaration) {
            super(declaration);
            this.type = new Type(scope, declaration.type);
            if (this.type.isVoid()) {
                throw CompileException.withPosition(new InvalidTypeException(this.type, "Variable type cannot be void"), declaration.type.position);
            }
            declaration.symbol = this;
        }

        // for built-in
        public Variable(String name, Type type) {
            super(name);
            this.type = type;
        }
    }

    // we need to first declare the type before we can use it in other symbols. every type corresponds to a class
    // a virtual class array does not have a corresponding typeName and is not stored in symbol table
    public static class TypeName extends Symbol {
        // primitive types are built-in and are not pointers
        public final boolean primitive;

        // for custom
        public TypeName(DeclarationNode.Class declaration) {
            super(declaration);
            this.primitive = false;
            declaration.typeName = this;
        }

        // for built-in
        public TypeName(String name) {
            super(name);
            this.primitive = true;
        }
    }
}
