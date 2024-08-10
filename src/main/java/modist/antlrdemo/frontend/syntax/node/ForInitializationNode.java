package modist.antlrdemo.frontend.syntax.node;

public sealed interface ForInitializationNode extends IAstNode permits ExpressionNode, StatementNode.VariableDeclarations {
}
