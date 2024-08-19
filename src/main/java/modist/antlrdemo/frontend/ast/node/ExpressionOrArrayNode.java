package modist.antlrdemo.frontend.ast.node;

public sealed interface ExpressionOrArrayNode extends IAstNode permits ExpressionNode, ArrayNode {
}
