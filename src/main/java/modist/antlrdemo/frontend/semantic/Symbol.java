package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.error.CompileException;
import modist.antlrdemo.frontend.error.InvalidTypeException;
import modist.antlrdemo.frontend.error.MultipleDefinitionsException;
import modist.antlrdemo.frontend.ast.metadata.Position;
import modist.antlrdemo.frontend.semantic.scope.Scope;
import modist.antlrdemo.frontend.ast.node.DefinitionAst;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// symbol extract some information from ast nodes.
// some symbols are built-in and do not have corresponding ast nodes
public abstract class Symbol {
    public final String name;
    public final Position position;
    public String irName;

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

    public static class Function extends Symbol {
        public final Type returnType;
        public final OrderedSymbolTable<Variable> parameters = new OrderedSymbolTable<>();
        // set in GlobalScope
        public boolean isMain;
        // if present, thisType is the class that the function belongs to
        @Nullable
        public TypeName thisType;

        public Function(Scope scope, DefinitionAst.Function definition) {
            super(definition);
            this.returnType = new Type(scope, definition.returnType);
            definition.parameters.forEach(parameter -> parameters.declare(new Variable(scope, parameter)));
            parameters.forEach(SymbolRenamer::setParamVariable);
            definition.symbol = this;
            SymbolRenamer.setFunction(this, null);
        }

        // for built-in
        public Function(String name, Type returnType, List<Variable> parameters) {
            super(name);
            this.returnType = returnType;
            parameters.forEach(this.parameters::declare);
            SymbolRenamer.setFunction(this, null);
        }

        public boolean shouldReturn() {
            return !returnType.isVoid() && !isMain;
        }
    }

    public static class Variable extends Symbol {
        public final Type type;
        public boolean isMember; // irName is null if isMember

        public Variable(Scope scope, DefinitionAst.Variable definition) {
            super(definition);
            this.type = new Type(scope, definition.type);
            if (this.type.isVoid()) {
                throw CompileException.withPosition(new InvalidTypeException(this.type, "Variable type cannot be void"), definition.type.position);
            }
            definition.symbol = this;
            SymbolRenamer.setGlobalVariable(this);
        }

        // for built-in
        public Variable(String name, Type type) {
            super(name);
            this.type = type;
            SymbolRenamer.setGlobalVariable(this);
        }
    }

    // we need to first declare the type before we can use it in other symbols. every type corresponds to a class
    public static class TypeName extends Symbol {
        public final boolean builtin;
        public Class definition;

        // for custom
        public TypeName(DefinitionAst.Class definition) {
            super(definition);
            this.builtin = false;
            definition.symbol = this;
            SymbolRenamer.setClass(this);
        }

        // for built-in
        public TypeName(String name) {
            super(name);
            this.builtin = true;
            SymbolRenamer.setClass(this);
        }

        public void setClass(Class definition) {
            this.definition = definition;
            if (definition.constructor != null) {
                SymbolRenamer.setFunction(definition.constructor, this);
            }
            definition.functions.forEach(function -> SymbolRenamer.setFunction(function, this));
            definition.variables.forEach(SymbolRenamer::setMemberVariable);
        }
    }

    public static class Class {
        @Nullable
        public final Function constructor;
        // for reference
        public final SymbolTable<Function> functions = new SymbolTable<>();
        public final OrderedSymbolTable<Variable> variables = new OrderedSymbolTable<>();

        public Class(Scope scope, DefinitionAst.Class definition) {
            this.constructor = getConstructor(scope, definition);
            definition.functions.forEach(function -> functions.declare(new Function(scope, function)));
            definition.variables.forEach(variable -> {
                if (variable.initializer != null) {
                    throw new CompileException("class variable cannot have initializer", variable.position);
                }
                variables.declare(new Variable(scope, variable));
            });
        }

        @Nullable
        private Function getConstructor(Scope scope, DefinitionAst.Class definition) {
            Function currentConstructor = null;
            for (DefinitionAst.Function constructorNode : definition.constructors) {
                if (!constructorNode.name.equals(definition.name)) {
                    throw new CompileException("Constructor name must be the same as the class name", constructorNode.position);
                }
                Function constructorSymbol = new Function(scope, constructorNode);
                if (currentConstructor != null) {
                    throw new MultipleDefinitionsException(constructorSymbol, currentConstructor);
                }
                currentConstructor = constructorSymbol;
            }
            return currentConstructor;
        }

        // for built-in
        public Class(@Nullable Function constructor, List<Function> functions, List<Variable> variables) {
            this.constructor = constructor;
            functions.forEach(this.functions::declare);
            variables.forEach(this.variables::declare);
        }
    }
}
