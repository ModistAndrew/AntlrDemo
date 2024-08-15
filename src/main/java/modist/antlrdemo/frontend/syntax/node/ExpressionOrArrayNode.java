package modist.antlrdemo.frontend.syntax.node;

public sealed interface ExpressionOrArrayNode extends IAstNode permits ExpressionNode, ArrayNode {
}
