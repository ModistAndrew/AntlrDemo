package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.metadata.Position;

public sealed interface IAstNode permits AstNode, ExpressionOrArrayNode, ForInitializationNode {
    Position getPosition();

    void setPosition(Position position);
}
