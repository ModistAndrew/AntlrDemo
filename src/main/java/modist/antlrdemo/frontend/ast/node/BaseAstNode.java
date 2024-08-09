package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.Position;

public abstract class BaseAstNode implements AstNode {
    public final Position position;

    public BaseAstNode(Position position) {
        this.position = position;
    }
}
