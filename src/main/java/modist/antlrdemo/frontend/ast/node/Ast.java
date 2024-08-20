package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.metadata.Position;

public sealed interface Ast permits BaseAst, ExpressionOrArrayAst, ForInitializationAst {
    Position getPosition();

    void setPosition(Position position);
}
