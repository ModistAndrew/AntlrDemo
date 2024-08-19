package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.semantic.Symbol;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract sealed class DefinitionAst extends Ast {
    public String name;

    // store symbol here for convenience
    public static final class Class extends DefinitionAst {
        public List<Variable> variables;
        public List<Function> constructors;
        public List<Function> functions;
        public Symbol.Class symbol;
        public Symbol.TypeName typeName;
    }

    public static final class Function extends DefinitionAst {
        public TypeAst returnType;
        public List<Variable> parameters;
        public List<StatementAst> body;
        public Symbol.Function symbol;
    }

    public static final class Variable extends DefinitionAst {
        public TypeAst type;
        @Nullable
        public ExpressionOrArrayAst initializer;
        public Symbol.Variable symbol;
    }
}