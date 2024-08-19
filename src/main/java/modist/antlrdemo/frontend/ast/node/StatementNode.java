package modist.antlrdemo.frontend.ast.node;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract sealed class StatementNode extends AstNode {
    public static final class Block extends StatementNode {
        public List<StatementNode> statements;
    }

    public static final class VariableDeclarations extends StatementNode implements ForInitializationNode {
        public List<DeclarationNode.Variable> variables;
    }

    public static final class If extends StatementNode {
        public ExpressionNode condition;
        public List<StatementNode> thenStatements;
        @Nullable
        public List<StatementNode> elseStatements;
    }

    public static final class For extends StatementNode {
        @Nullable
        public ForInitializationNode initialization;
        @Nullable
        public ExpressionNode condition;
        @Nullable
        public ExpressionNode update;
        public List<StatementNode> statements;
    }

    public static final class While extends StatementNode {
        public ExpressionNode condition;
        public List<StatementNode> statements;
    }

    public static final class Break extends StatementNode {
    }

    public static final class Continue extends StatementNode {
    }

    public static final class Return extends StatementNode {
        @Nullable
        public ExpressionOrArrayNode expression;
    }

    public static final class Expression extends StatementNode {
        public ExpressionNode expression;
    }

    public static final class Empty extends StatementNode {
    }
}
