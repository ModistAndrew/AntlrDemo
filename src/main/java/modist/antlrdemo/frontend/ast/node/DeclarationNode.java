package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.semantic.Symbol;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract sealed class DeclarationNode extends AstNode {
    public String name;

    // store symbol here for convenience
    public static final class Class extends DeclarationNode {
        public List<Variable> variables;
        public List<Function> constructors;
        public List<Function> functions;
        public Symbol.Class symbol;
        public Symbol.TypeName typeName;
    }

    public static final class Function extends DeclarationNode {
        public TypeNode returnType;
        public List<Variable> parameters;
        public List<StatementNode> body;
        public Symbol.Function symbol;
    }

    public static final class Variable extends DeclarationNode {
        public TypeNode type;
        @Nullable
        public ExpressionOrArrayNode initializer;
        public Symbol.Variable symbol;
    }
}