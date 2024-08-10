package modist.antlrdemo.frontend.syntax.node;

import java.util.List;
import java.util.Optional;

public abstract sealed class DeclarationNode extends AstNode {
    public String name;

    public static final class Class extends DeclarationNode {
        public Optional<Constructor> constructor;
        public List<Function> functions;
        public List<Variable> variables;
    }

    public static final class Function extends DeclarationNode {
        public Optional<TypeNode> returnType;
        public List<Parameter> parameters;
        public StatementNode.Block body;
    }

    public static final class Parameter extends DeclarationNode {
        public TypeNode type;
    }

    public static final class Variable extends DeclarationNode {
        public TypeNode type;
        public Optional<ExpressionNode> initializer;
    }

    public static final class Constructor extends DeclarationNode {
        public StatementNode.Block body;
    }
}