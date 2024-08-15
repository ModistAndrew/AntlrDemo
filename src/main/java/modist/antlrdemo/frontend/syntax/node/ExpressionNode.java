package modist.antlrdemo.frontend.syntax.node;

import modist.antlrdemo.frontend.metadata.LiteralEnum;
import modist.antlrdemo.frontend.metadata.Operator;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract sealed class ExpressionNode extends AstNode implements ForInitializationNode, ExpressionOrArrayNode {
    public static final class This extends ExpressionNode {
    }

    public static final class Literal extends ExpressionNode {
        public LiteralEnum value;
    }

    public static final class FormatString extends ExpressionNode {
        public List<String> texts;
        public List<ExpressionNode> expressions;
    }

    public static final class Creator extends ExpressionNode {
        public String typeName;
        @Nullable
        public ArrayCreatorNode arrayCreator;
    }

    public static final class Subscript extends ExpressionNode {
        public ExpressionNode expression;
        public ExpressionNode index;
    }

    public static final class Variable extends ExpressionNode {
        @Nullable
        public ExpressionNode expression;
        public String name;
    }

    public static final class Function extends ExpressionNode {
        @Nullable
        public ExpressionNode expression;
        public String name;
        public List<ExpressionOrArrayNode> arguments;
    }

    public static final class PostUnaryAssign extends ExpressionNode {
        public ExpressionNode expression;
        public Operator operator;
    }

    public static final class PreUnaryAssign extends ExpressionNode {
        public ExpressionNode expression;
        public Operator operator;
    }

    public static final class PreUnary extends ExpressionNode {
        public ExpressionNode expression;
        public Operator operator;
    }

    public static final class Binary extends ExpressionNode {
        public ExpressionNode leftExpression;
        public ExpressionNode rightExpression;
        public Operator operator;
    }

    public static final class Conditional extends ExpressionNode {
        public ExpressionNode condition;
        public ExpressionNode trueExpression;
        public ExpressionNode falseExpression;
    }

    public static final class Assign extends ExpressionNode {
        public ExpressionNode leftExpression;
        public ExpressionOrArrayNode rightExpression;
    }
}
