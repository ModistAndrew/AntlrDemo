package modist.antlrdemo.frontend.syntax.node;

import modist.antlrdemo.frontend.metadata.Position;

public abstract sealed class AstNode implements IAstNode permits ArrayCreatorNode, ArrayNode, DeclarationNode, ExpressionNode, ProgramNode, StatementNode, TypeNode {
    public Position position;

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }
}
