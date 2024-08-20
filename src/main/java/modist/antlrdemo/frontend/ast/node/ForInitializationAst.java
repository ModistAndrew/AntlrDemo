package modist.antlrdemo.frontend.ast.node;

public sealed interface ForInitializationAst extends Ast permits ExpressionAst, StatementAst.VariableDefinitions {
}
