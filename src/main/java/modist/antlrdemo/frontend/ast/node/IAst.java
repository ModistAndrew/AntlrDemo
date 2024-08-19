package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.metadata.Position;

public sealed interface IAst permits Ast, ExpressionOrArrayAst, ForInitializationAst {
    Position getPosition();

    void setPosition(Position position);
}
