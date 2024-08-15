package modist.antlrdemo.frontend.syntax.node;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract sealed class DeclarationNode extends AstNode {
    public String name;

    public static final class Class extends DeclarationNode {
        public List<Variable> variables;
        @Nullable
        public Function constructor;
        public List<Function> functions;
    }

    public static final class Function extends DeclarationNode {
        public TypeNode returnType;
        public List<Variable> parameters;
        public List<StatementNode> body;
    }

    public static final class Variable extends DeclarationNode {
        public TypeNode type;
        @Nullable
        public ExpressionOrArrayNode initializer;
    }
}