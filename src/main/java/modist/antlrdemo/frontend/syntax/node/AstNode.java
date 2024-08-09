package modist.antlrdemo.frontend.syntax.node;

import modist.antlrdemo.frontend.syntax.Position;

public abstract sealed class AstNode implements IAstNode permits ArgumentListNode, ArrayCreatorNode, ArrayInitializerNode, BlockNode, CreatorNode, DeclaratorNode, ExpressionNode, FormatStringNode, ProgramNode, StatementNode, TypeNameNode, TypeNode, VariableDeclarationNode {
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
