package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.metadata.LiteralEnum;
import modist.antlrdemo.frontend.ast.metadata.Operator;
import modist.antlrdemo.frontend.semantic.Symbol;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract sealed class ExpressionAst extends ExpressionOrArrayAst implements ForInitializationAst {
    public static final class This extends ExpressionAst {
    }

    public static final class Literal extends ExpressionAst {
        public LiteralEnum value;
    }

    public static final class FormatString extends ExpressionAst {
        public List<String> texts;
        public List<ExpressionAst> expressions;
    }

    public static final class Creator extends ExpressionAst {
        public String typeName;
        @Nullable
        public ArrayCreatorAst arrayCreator;
    }

    public static final class Subscript extends ExpressionAst {
        public ExpressionAst expression;
        public ExpressionAst index;
    }

    public static final class Variable extends ExpressionAst {
        @Nullable
        public ExpressionAst expression;
        public String name;
        public Symbol.Variable symbol;
    }

    public static final class Function extends ExpressionAst {
        @Nullable
        public ExpressionAst expression;
        public String name;
        public List<ExpressionOrArrayAst> arguments;
        public Symbol.Function symbol;
    }

    public static final class PostUnaryAssign extends ExpressionAst {
        public ExpressionAst expression;
        public Operator operator;
    }

    public static final class PreUnaryAssign extends ExpressionAst {
        public ExpressionAst expression;
        public Operator operator;
    }

    public static final class PreUnary extends ExpressionAst {
        public ExpressionAst expression;
        public Operator operator;
    }

    public static final class Binary extends ExpressionAst {
        public ExpressionAst left;
        public ExpressionAst right;
        public Operator operator;
    }

    public static final class Conditional extends ExpressionAst {
        public ExpressionAst condition;
        public ExpressionAst trueExpression;
        public ExpressionAst falseExpression;
    }

    public static final class Assign extends ExpressionAst {
        public ExpressionAst left;
        public ExpressionOrArrayAst right;
    }
}
