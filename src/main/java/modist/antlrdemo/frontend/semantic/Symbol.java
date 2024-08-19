package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.error.CompileException;
import modist.antlrdemo.frontend.error.InvalidTypeException;
import modist.antlrdemo.frontend.error.MultipleDefinitionsException;
import modist.antlrdemo.frontend.ast.metadata.Position;
import modist.antlrdemo.frontend.semantic.scope.Scope;
import modist.antlrdemo.frontend.ast.node.DefinitionAst;
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

    public Symbol(DefinitionAst definition) {
        this(definition.name, definition.position);
    }

    public static class Class extends Symbol {
        @Nullable
        public final Function constructor;
        // for reference
        public final SymbolTable<Function> functions = new SymbolTable<>();
        public final SymbolTable<Variable> variables = new SymbolTable<>();

        public Class(Scope scope, DefinitionAst.Class definition) {
            super(definition);
            Function constructorTemp = null;
            for (DefinitionAst.Function constructorNode : definition.constructors) {
                if (!constructorNode.name.equals(definition.name)) {
                    throw new CompileException("Constructor name must be the same as the class name", constructorNode.position);
                }
                Function constructorSymbol = new Function(scope, constructorNode);
                if (constructorTemp != null) {
                    throw new MultipleDefinitionsException(constructorSymbol, constructorTemp);
                }
                constructorTemp = constructorSymbol;
            }
            this.constructor = constructorTemp;
            definition.functions.forEach(function -> functions.declare(new Function(scope, function)));
            definition.variables.forEach(variable -> variables.declare(new Variable(scope, variable)));
            definition.symbol = this;
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

        public Function(Scope scope, DefinitionAst.Function definition) {
            super(definition);
            this.returnType = new Type(scope, definition.returnType);
            definition.parameters.forEach(parameter -> parameters.declare(new Variable(scope, parameter)));
            definition.symbol = this;
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

        public Variable(Scope scope, DefinitionAst.Variable definition) {
            super(definition);
            this.type = new Type(scope, definition.type);
            if (this.type.isVoid()) {
                throw CompileException.withPosition(new InvalidTypeException(this.type, "Variable type cannot be void"), definition.type.position);
            }
            definition.symbol = this;
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
        public TypeName(DefinitionAst.Class definition) {
            super(definition);
            this.primitive = false;
            definition.typeName = this;
        }

        // for built-in
        public TypeName(String name) {
            super(name);
            this.primitive = true;
        }
    }
}
