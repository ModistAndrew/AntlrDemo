package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.Position;

public sealed interface AstNode permits BaseAstNode, ForInitializationNode, VariableInitializerNode {
    Position getPosition();

    void setPosition(Position position);
}
