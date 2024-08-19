package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.metadata.Position;

public abstract sealed class Ast implements IAst permits ArrayCreatorAst, ArrayAst, DefinitionAst, ExpressionAst, ProgramAst, StatementAst, TypeAst {
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
