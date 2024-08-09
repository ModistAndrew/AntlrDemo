package modist.antlrdemo.frontend.syntax.node;

import org.jetbrains.annotations.Nullable;

public abstract sealed class StatementNode extends AstNode {
    public static final class Block extends StatementNode {
        public BlockNode block;
    }

    public static final class VariableDeclaration extends StatementNode {
        public VariableDeclarationNode variableDeclaration;
    }

    public static final class If extends StatementNode {
        public ExpressionNode condition;
        public StatementNode thenStatement;
        @Nullable
        public StatementNode elseStatement;
    }

    public static final class For extends StatementNode {
        @Nullable
        public ForInitializationNode initialization;
        @Nullable
        public ExpressionNode condition;
        @Nullable
        public ExpressionNode update;
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
        @Nullable
        public ExpressionNode expression;
    }

    public static final class Expression extends StatementNode {
        public ExpressionNode expression;
    }
}
