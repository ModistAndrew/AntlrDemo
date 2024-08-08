package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.AstVisitor;
import modist.antlrdemo.frontend.ast.metadata.Position;
import org.jetbrains.annotations.Nullable;

public abstract class StatementNode extends BaseAstNode {
    public StatementNode(Position position) {
        super(position);
    }

    public static class Block extends StatementNode {
        public BlockNode block;

        public Block(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    public static class VariableDeclaration extends StatementNode {
        public VariableDeclarationNode variableDeclaration;

        public VariableDeclaration(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    public static class If extends StatementNode {
        public ExpressionNode condition;
        public StatementNode thenStatement;
        @Nullable
        public StatementNode elseStatement;

        public If(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    public static class For extends StatementNode {
        @Nullable
        public ForInitializationNode initialization;
        @Nullable
        public ExpressionNode condition;
        @Nullable
        public ExpressionNode update;
        public StatementNode statement;

        public For(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    public static class While extends StatementNode {
        public ExpressionNode condition;
        public StatementNode statement;

        public While(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    public static class Break extends StatementNode {
        public Break(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    public static class Continue extends StatementNode {
        public Continue(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    public static class Return extends StatementNode {
        @Nullable
        public ExpressionNode expression;

        public Return(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    public static class Expression extends StatementNode {
        public ExpressionNode expression;

        public Expression(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }
    }
}
