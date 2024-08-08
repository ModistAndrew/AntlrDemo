package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.metadata.Position;

public abstract class DeclaratorNode extends BaseAstNode {
    public final String name;

    public DeclaratorNode(Position position, String name) {
        super(position);
        this.name = name;
    }
}