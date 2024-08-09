package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.Position;

public abstract sealed class BaseAstNode implements AstNode permits ArgumentListNode, ArrayCreatorNode, ArrayInitializerNode, BlockNode, CreatorNode, DeclaratorNode, ExpressionNode, FormatStringNode, ProgramNode, StatementNode, TypeNameNode, TypeNode, VariableDeclarationNode {
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
