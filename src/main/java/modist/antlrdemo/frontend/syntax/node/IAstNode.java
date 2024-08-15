package modist.antlrdemo.frontend.syntax.node;

import modist.antlrdemo.frontend.metadata.Position;

public sealed interface IAstNode permits AstNode, ExpressionOrArrayNode, ForInitializationNode {
    Position getPosition();

    void setPosition(Position position);
}
