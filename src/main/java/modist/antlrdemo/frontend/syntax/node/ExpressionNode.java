package modist.antlrdemo.frontend.syntax.node;

import org.jetbrains.annotations.Nullable;

public abstract sealed class ExpressionNode extends AstNode implements ForInitializationNode, VariableInitializerNode {
    public static final class Paren extends ExpressionNode {
        public ExpressionNode expression;
    }

    public static final class This extends ExpressionNode {
    }

    public static final class Literal extends ExpressionNode {
        @Nullable
        public LiteralEnum value;

        public sealed interface LiteralEnum {
            record Int(int value) implements LiteralEnum {
            }

            record Bool(boolean value) implements LiteralEnum {
            }

            record Str(String value) implements LiteralEnum {
            }
        }
    }

    public static final class FormatString extends ExpressionNode {
        public FormatStringNode formatString;
    }

    public static final class New extends ExpressionNode {
        public CreatorNode creator;
    }

    public static final class ArrayAccess extends ExpressionNode {
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
        public ArgumentListNode arguments;
    }

    public static final class PostUnary extends ExpressionNode {
        public ExpressionNode expression;
        public Operator operator;

        public enum Operator {
            INC, DEC
        }
    }

    public static final class PreUnary extends ExpressionNode {
        public ExpressionNode expression;
        public Operator operator;

        public enum Operator {
            INC, DEC, ADD, SUB, NOT, LOGICAL_NOT
        }
    }

    public static final class Binary extends ExpressionNode {
        public ExpressionNode leftExpression;
        public ExpressionNode rightExpression;
        public Operator operator;

        public enum Operator {
            MUL, DIV, MOD, ADD, SUB, SHL, SHR, GT, LT, GE, LE, NE, EQ, AND, XOR, OR, LOGICAL_AND, LOGICAL_OR
        }
    }

    public static final class Conditional extends ExpressionNode {
        public ExpressionNode condition;
        public ExpressionNode trueExpression;
        public ExpressionNode falseExpression;
    }

    public static final class Assign extends ExpressionNode {
        public ExpressionNode leftExpression;
        public ExpressionNode rightExpression;
        public Operator operator;

        public enum Operator {
            ASSIGN, ADD_ASSIGN, SUB_ASSIGN, MUL_ASSIGN, DIV_ASSIGN, MOD_ASSIGN, AND_ASSIGN, OR_ASSIGN, XOR_ASSIGN, SHL_ASSIGN, SHR_ASSIGN
        }
    }
}
