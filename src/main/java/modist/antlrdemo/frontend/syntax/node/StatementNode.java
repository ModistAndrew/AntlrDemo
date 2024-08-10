package modist.antlrdemo.frontend.syntax.node;

import java.util.List;
import java.util.Optional;

public abstract sealed class StatementNode extends AstNode {
    public static final class Block extends StatementNode {
        public List<StatementNode> statements;
    }

    public static final class VariableDeclarations extends StatementNode implements ForInitializationNode {
        public List<DeclarationNode.Variable> variables;
    }

    public static final class If extends StatementNode {
        public ExpressionNode condition;
        public StatementNode thenStatement;
        public Optional<StatementNode> elseStatement;
    }

    public static final class For extends StatementNode {
        public Optional<ForInitializationNode> initialization;
        public Optional<ExpressionNode> condition;
        public Optional<ExpressionNode> update;
        public StatementNode statement;
    }

    public static final class While extends StatementNode {
        public ExpressionNode condition;
        public StatementNode statement;
    }

    public static final class Break extends StatementNode {
    }

    public static final class Continue extends StatementNode {
    }

    public static final class Return extends StatementNode {
        public Optional<ExpressionNode> expression;
    }

    public static final class Expression extends StatementNode {
        public ExpressionNode expression;
    }
}
