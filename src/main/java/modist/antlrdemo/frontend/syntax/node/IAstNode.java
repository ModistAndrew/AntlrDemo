package modist.antlrdemo.frontend.syntax.node;

import modist.antlrdemo.frontend.metadata.Position;

public sealed interface IAstNode permits AstNode, ForInitializationNode {
    Position getPosition();

    void setPosition(Position position);
}
