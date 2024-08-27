package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.semantic.error.CompileException;
import modist.antlrdemo.frontend.semantic.error.InvalidTypeException;
import modist.antlrdemo.frontend.semantic.error.MultipleDefinitionsException;
import modist.antlrdemo.frontend.ast.metadata.Position;
import modist.antlrdemo.frontend.semantic.scope.Scope;
import modist.antlrdemo.frontend.ast.node.DefinitionAst;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

// symbol extract some information from ast nodes.
// some symbols are built-in and do not have corresponding ast nodes
public abstract class Symbol {
    public final String name;
    public final Position position;
    public final String irName;

    private Symbol(String name, Position position, String irName) {
        this.name = name;
        this.position = position;
        this.irName = irName;
    }

    public static class Variable extends Symbol {
        public final Type type;
        @Nullable
        public final TypeName classType;
        public final int memberIndex;

        private Variable(String name, Position position, String irName, Type type, @Nullable TypeName classType, int memberIndex) {
            super(name, position, irName);
            this.type = type;
            this.classType = classType;
            this.memberIndex = memberIndex;
            if (this.type.isVoid()) {
                throw CompileException.withPosition(new InvalidTypeException(this.type, "Variable type cannot be void"), position);
            }
        }

        private Variable(Scope scope, DefinitionAst.Variable definition, String irName, @Nullable TypeName classType, int memberIndex) {
            this(definition.name, definition.position, irName, new Type(scope, definition.type), classType, memberIndex);
            if (definition.initializer != null) {
                new Type.Builder(scope).matchExpression(definition.initializer, type);
            }
            definition.symbol = this;
        }

        // builtin anonymous parameter variable
        private Variable(Type type, Function function) {
            this(null, Position.BUILTIN, null, type, null, -1);
            function.parameters.declare(this);
        }

        // global variable
        public Variable(Scope scope, DefinitionAst.Variable definition) {
            this(scope, definition, SemanticNamer.globalVariable(definition.name), null, -1);
        }

        // member variable
        private Variable(Scope scope, DefinitionAst.Variable definition, TypeName classType) {
            this(scope, definition, null, classType, classType.variables.size());
            if (definition.initializer != null) {
                throw new CompileException("class variable cannot have initializer", position);
            }
            classType.variables.declare(this);
        }

        // local variable
        public Variable(Scope scope, DefinitionAst.Variable definition, SemanticNamer namer) {
            this(scope, definition, namer.localVariable(definition.name), null, -1);
        }

        // parameter variable
        private Variable(Scope scope, DefinitionAst.Variable definition, Function function) {
            this(scope, definition, SemanticNamer.parameterVariable(definition.name), null, -1);
            function.parameters.declare(this);
        }
    }

    public static class Function extends Symbol {
        public final Type returnType;
        @Nullable
        public final TypeName classType;
        // to be filled in Parameter Constructor
        public final OrderedSymbolTable<Variable> parameters = new OrderedSymbolTable<>();
        // to be filled in GlobalScope
        public boolean isMain;

        private Function(String name, Position position, String irName, Type returnType, @Nullable TypeName classType) {
            super(name, position, irName);
            this.returnType = returnType;
            this.classType = classType;
        }

        private Function(Scope scope, DefinitionAst.Function definition, String irName, @Nullable TypeName classType) {
            this(definition.name, definition.position, irName, new Type(scope, definition.returnType), classType);
            definition.symbol = this;
        }

        // builtin global function
        public Function(String name, Type returnType, Type... parameterTypes) {
            this(name, Position.BUILTIN, SemanticNamer.globalFunction(name), returnType, null);
            Arrays.stream(parameterTypes).forEach(type -> new Variable(type, this));
        }

        // builtin member function
        public Function(String name, TypeName classType, Type returnType, Type... parameterTypes) {
            this(name, Position.BUILTIN, SemanticNamer.memberFunction(name, classType.name), returnType, classType);
            classType.functions.declare(this);
            Arrays.stream(parameterTypes).forEach(type -> new Variable(type, this));
        }

        // global function
        public Function(Scope scope, DefinitionAst.Function definition) {
            this(scope, definition, SemanticNamer.globalFunction(definition.name), null);
            definition.parameters.forEach(parameter -> new Variable(scope, parameter, this));
        }

        // member function
        private Function(Scope scope, DefinitionAst.Function definition, TypeName classType, boolean constructor) {
            this(scope, definition, SemanticNamer.memberFunction(definition.name, classType.name), classType);
            if (constructor) {
                if (!name.equals(classType.name)) {
                    throw new CompileException("Constructor name must be the same as the class name", position);
                }
                if (classType.constructor != null) {
                    throw new MultipleDefinitionsException(classType.constructor, this);
                }
                classType.constructor = this;
            } else {
                classType.functions.declare(this);
            }
            definition.parameters.forEach(parameter -> new Variable(scope, parameter, this));
        }

        public boolean shouldReturn() {
            return !returnType.isVoid() && !isMain;
        }
    }

    // we need to first declare the type before we can use it in other symbols
    // as a result, children won't be created automatically
    public static class TypeName extends Symbol {
        public final boolean builtin;
        // to be filled in Variable and Function constructors
        @Nullable
        public Function constructor;
        public final SymbolTable<Function> functions = new SymbolTable<>();
        public final OrderedSymbolTable<Variable> variables = new OrderedSymbolTable<>();

        public TypeName(String name) {
            super(name, Position.BUILTIN, SemanticNamer.typeName(name));
            this.builtin = true;
        }

        public TypeName(DefinitionAst.Class definition) {
            super(definition.name, definition.position, SemanticNamer.typeName(definition.name));
            this.builtin = false;
            definition.symbol = this;
        }

        // call it manually after creating all type names to declare all children
        public void createClass(Scope scope, DefinitionAst.Class definition) {
            definition.variables.forEach(variable -> new Variable(scope, variable, this));
            definition.functions.forEach(function -> new Function(scope, function, this, false));
            definition.constructors.forEach(constructor -> new Function(scope, constructor, this, true));
        }
    }
}
