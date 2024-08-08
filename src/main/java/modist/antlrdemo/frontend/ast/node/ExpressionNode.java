package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.AstVisitor;
import modist.antlrdemo.frontend.ast.metadata.ExpressionInfo;
import modist.antlrdemo.frontend.ast.metadata.Position;

public abstract class ExpressionNode extends BaseAstNode implements ForInitializationNode, VariableInitializerNode {
    public ExpressionInfo info;

    public ExpressionNode(Position position) {
        super(position);
    }

    public static class Paren extends ExpressionNode {
        public ExpressionNode expression;

        public Paren(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    public static class This extends ExpressionNode {
        public This(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    public static class Literal extends ExpressionNode {
        public LiteralEnum value;

        public Literal(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }

        public interface LiteralEnum {
            record Int(int value) implements LiteralEnum {
            }

            record Bool(boolean value) implements LiteralEnum {
            }

            record Str(String value) implements LiteralEnum {
            }

            enum Null implements LiteralEnum {
                INSTANCE
            }
        }
    }

    public static class FormatString extends ExpressionNode {
        public FormatStringNode formatString;

        public FormatString(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    public static class Identifier extends ExpressionNode {
        public String name;

        public Identifier(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    public static class New extends ExpressionNode {
        public CreatorNode creator;

        public New(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    public static class ArrayAccess extends ExpressionNode {
        public ExpressionNode expression;
        public ExpressionNode index;

        public ArrayAccess(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    public static class MemberAccess extends ExpressionNode {
        public ExpressionNode expression;
        public String member;

        public MemberAccess(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    public static class FunctionCall extends ExpressionNode {
        public ExpressionNode expression;
        public ArgumentListNode arguments;

        public FunctionCall(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    public static class PostUnary extends ExpressionNode {
        public ExpressionNode expression;
        public Operator operator;

        public PostUnary(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }

        public enum Operator {
            INC, DEC
        }
    }

    public static class PreUnary extends ExpressionNode {
        public ExpressionNode expression;
        public Operator operator;

        public PreUnary(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }

        public enum Operator {
            INC, DEC, ADD, SUB,
            NOT, LOGICAL_NOT
        }
    }

    public static class Binary extends ExpressionNode {
        public ExpressionNode leftExpression;
        public ExpressionNode rightExpression;
        public Operator operator;

        public Binary(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }

        public enum Operator {
            MUL, DIV, MOD,
            ADD, SUB,
            SHL, SHR,
            GT, LT, GE, LE,
            NE, EQ,
            AND,
            XOR,
            OR,
            LOGICAL_AND,
            LOGICAL_OR
        }
    }

    public static class Conditional extends ExpressionNode {
        public ExpressionNode condition;
        public ExpressionNode trueExpression;
        public ExpressionNode falseExpression;

        public Conditional(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    public static class Assign extends ExpressionNode {
        public ExpressionNode leftExpression;
        public ExpressionNode rightExpression;
        public Operator operator;

        public Assign(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }

        public enum Operator {
            ASSIGN, ADD_ASSIGN, SUB_ASSIGN, MUL_ASSIGN, DIV_ASSIGN, MOD_ASSIGN, AND_ASSIGN, OR_ASSIGN, XOR_ASSIGN, SHL_ASSIGN, SHR_ASSIGN
        }
    }
}
