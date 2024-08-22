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
        // if present, classType is the class that the function belongs to
        @Nullable
        public TypeName classType;
        // set in GlobalScope
        public boolean isMain;

        public Function(Scope scope, DefinitionAst.Function definition) {
            super(definition);
            this.returnType = new Type(scope, definition.returnType);
            definition.parameters.forEach(parameter -> parameters.declare(new Variable(scope, parameter)));
            parameters.forEach(SymbolNamer::setParamVariable);
            definition.symbol = this;
            SymbolNamer.setFunction(this, null);
        }

        // for built-in
        public Function(String name, Type returnType, List<Variable> parameters) {
            super(name);
            this.returnType = returnType;
            parameters.forEach(this.parameters::declare);
            parameters.forEach(SymbolNamer::setParamVariable);
            SymbolNamer.setFunction(this, null);
        }

        public boolean shouldReturn() {
            return !returnType.isVoid() && !isMain;
        }
    }

    public static class Variable extends Symbol {
        public final Type type;
        // if present, classType is the class that the variable belongs to
        @Nullable
        public TypeName classType;
        public int memberIndex; // irName is null if memberIndex >= 0

        public Variable(Scope scope, DefinitionAst.Variable definition) {
            super(definition);
            this.type = new Type(scope, definition.type);
            if (this.type.isVoid()) {
                throw CompileException.withPosition(new InvalidTypeException(this.type, "Variable type cannot be void"), definition.type.position);
            }
            definition.symbol = this;
            SymbolNamer.setGlobalVariable(this);
        }

        // for built-in
        public Variable(String name, Type type) {
            super(name);
            this.type = type;
            SymbolNamer.setGlobalVariable(this);
        }
    }

    // we need to first declare the type before we can use it in other symbols. constructor, functions, and variables are declared later
    public static class TypeName extends Symbol {
        public final boolean builtin;

        @Nullable
        public Function constructor;
        // for reference
        public final SymbolTable<Function> functions = new SymbolTable<>();
        public final OrderedSymbolTable<Variable> variables = new OrderedSymbolTable<>();

        // for custom
        public TypeName(DefinitionAst.Class definition) {
            super(definition);
            this.builtin = false;
            definition.symbol = this;
            SymbolNamer.setClass(this);
        }

        // for built-in
        public TypeName(String name) {
            super(name);
            this.builtin = true;
            SymbolNamer.setClass(this);
        }

        public void setClass(Scope scope, DefinitionAst.Class definition) {
            constructor = getConstructor(scope, definition);
            definition.functions.forEach(function -> functions.declare(new Function(scope, function)));
            for (int i = 0; i < definition.variables.size(); i++) {
                DefinitionAst.Variable variable = definition.variables.get(i);
                if (variable.initializer != null) {
                    throw new CompileException("class variable cannot have initializer", variable.position);
                }
                Variable symbol = new Variable(scope, variable);
                SymbolNamer.setMemberVariable(symbol, this, i);
                variables.declare(symbol);
            }
            if (constructor != null) {
                SymbolNamer.setFunction(constructor, this);
            }
            functions.forEach(function -> SymbolNamer.setFunction(function, this));
        }

        // for built-in types which have no member variables or constructors
        public void setClass(List<Function> functions) {
            constructor = null;
            functions.forEach(this.functions::declare);
            functions.forEach(function -> SymbolNamer.setFunction(function, this));
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
    }
}
