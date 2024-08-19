package modist.antlrdemo.frontend.ast.node;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract sealed class StatementAst extends Ast {
    public static final class Block extends StatementAst {
        public List<StatementAst> statements;
    }

    public static final class VariableDefinitions extends StatementAst implements ForInitializationAst {
        public List<DefinitionAst.Variable> variables;
    }

    public static final class If extends StatementAst {
        public ExpressionAst condition;
        public List<StatementAst> thenStatements;
        @Nullable
        public List<StatementAst> elseStatements;
    }

    public static final class For extends StatementAst {
        @Nullable
        public ForInitializationAst initialization;
        @Nullable
        public ExpressionAst condition;
        @Nullable
        public ExpressionAst update;
        public List<StatementAst> statements;
    }

    public static final class While extends StatementAst {
        public ExpressionAst condition;
        public List<StatementAst> statements;
    }

    public static final class Break extends StatementAst {
    }

    public static final class Continue extends StatementAst {
    }

    public static final class Return extends StatementAst {
        @Nullable
        public ExpressionOrArrayAst expression;
    }

    public static final class Expression extends StatementAst {
        public ExpressionAst expression;
    }

    public static final class Empty extends StatementAst {
    }
}
