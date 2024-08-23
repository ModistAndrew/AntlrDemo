package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.semantic.error.CompileException;
import modist.antlrdemo.frontend.semantic.error.InvalidTypeException;
import modist.antlrdemo.frontend.semantic.error.MultipleDefinitionsException;
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
    // in SemanticNamer by Scope or Symbol statically when created
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
        // in Symbol.Class
        // if present, classType is the class that the function belongs to
        @Nullable
        public TypeName classType;
        // in GlobalScope
        public boolean isMain;

        public Function(Scope scope, DefinitionAst.Function definition) {
            super(definition);
            this.returnType = new Type(scope, definition.returnType);
            definition.parameters.forEach(parameter -> parameters.declare(new Variable(scope, parameter)));
            definition.symbol = this;
            SemanticNamer.setFunction(this, null);
        }

        // for built-in
        public Function(String name, Type returnType, List<Variable> parameters) {
            super(name);
            this.returnType = returnType;
            parameters.forEach(this.parameters::declare);
            SemanticNamer.setFunction(this, null);
        }

        public boolean shouldReturn() {
            return !returnType.isVoid() && !isMain;
        }
    }

    public static class Variable extends Symbol {
        public final Type type;
        // if present, classType is the class that the variable belongs to, and memberIndex >= 0
        // in Symbol.Class
        @Nullable
        public TypeName classType;
        public int memberIndex = -1;

        public Variable(Scope scope, DefinitionAst.Variable definition) {
            super(definition);
            this.type = new Type(scope, definition.type);
            if (this.type.isVoid()) {
                throw CompileException.withPosition(new InvalidTypeException(this.type, "Variable type cannot be void"), definition.type.position);
            }
            definition.symbol = this;
            SemanticNamer.setGlobalVariable(this);
        }

        // for built-in
        public Variable(String name, Type type) {
            super(name);
            this.type = type;
            SemanticNamer.setGlobalVariable(this);
        }
    }

    // we need to first declare the type before we can use it in other symbols. constructor, functions, and variables are declared later
    public static class TypeName extends Symbol {
        public final boolean builtin;
        @Nullable
        public Function constructor;
        public final SymbolTable<Function> functions = new SymbolTable<>();
        public final OrderedSymbolTable<Variable> variables = new OrderedSymbolTable<>();

        // for custom
        public TypeName(DefinitionAst.Class definition) {
            super(definition);
            this.builtin = false;
            definition.symbol = this;
            SemanticNamer.setClass(this);
        }

        // for built-in
        public TypeName(String name) {
            super(name);
            this.builtin = true;
            SemanticNamer.setClass(this);
        }

        public void setClass(Scope scope, DefinitionAst.Class definition) {
            constructor = getConstructor(scope, definition);
            definition.functions.forEach(function -> functions.declare(new Function(scope, function)));
            definition.variables.forEach(variable -> {
                if (variable.initializer != null) {
                    throw new CompileException("class variable cannot have initializer", variable.position);
                }
                variables.declare(new Variable(scope, variable));
            });
            fillChildSymbols();
        }

        // for built-in types which have no member variables or constructors
        public void setClass(List<Function> functions) {
            constructor = null;
            functions.forEach(this.functions::declare);
            fillChildSymbols();
        }

        private void fillChildSymbols() {
            for (int i = 0; i < variables.size(); i++) {
                Variable symbol = variables.list.get(i);
                symbol.classType = this;
                symbol.memberIndex = i;
            }
            if (constructor != null) {
                SemanticNamer.setFunction(constructor, this);
                constructor.classType = this;
            }
            functions.forEach(function -> {
                SemanticNamer.setFunction(function, this);
                function.classType = this;
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
    }
}
