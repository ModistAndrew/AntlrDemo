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
            definition.symbol = this;
        }

        // for built-in
        public Function(String name, Type returnType, List<Variable> parameters) {
            super(name);
            this.returnType = returnType;
            parameters.forEach(this.parameters::declare);
        }

        public boolean shouldReturn() {
            return !returnType.isVoid() && !isMain;
        }
    }

    public static class Variable extends Symbol {
        public final Type type;
        public boolean isGlobal;
        public boolean isMember;

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
    // the virtual class array does not have a corresponding typeName
    public static class TypeName extends Symbol {
        public final boolean builtin;
        public Class definition;

        // for custom
        public TypeName(DefinitionAst.Class definition) {
            super(definition);
            this.builtin = false;
            definition.symbol = this;
        }

        // for built-in
        public TypeName(String name) {
            super(name);
            this.builtin = true;
        }

        public void setClass(Class definition) {
            this.definition = definition;
        }
    }

    public static class Class {
        @Nullable
        public final Function constructor;
        // for reference
        public final SymbolTable<Function> functions = new SymbolTable<>();
        public final OrderedSymbolTable<Variable> variables = new OrderedSymbolTable<>();

        public Class(Scope scope, DefinitionAst.Class definition) {
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
        }

        // for built-in
        public Class(@Nullable Function constructor, List<Function> functions, List<Variable> variables) {
            this.constructor = constructor;
            functions.forEach(this.functions::declare);
            variables.forEach(this.variables::declare);
        }
    }
}
