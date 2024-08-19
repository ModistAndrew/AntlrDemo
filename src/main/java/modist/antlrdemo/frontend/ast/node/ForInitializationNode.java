package modist.antlrdemo.frontend.ast.node;

public sealed interface ForInitializationNode extends IAstNode permits ExpressionNode, StatementNode.VariableDeclarations {
}
