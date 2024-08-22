package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.metadata.Position;

public sealed interface Ast permits BaseAst, ForInitializationAst {
    Position getPosition();

    void setPosition(Position position);
}
