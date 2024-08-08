package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.AstVisitor;
import modist.antlrdemo.frontend.ast.metadata.Position;

public abstract class AstNode {
    public final Position position;

    public AstNode(Position position) {
        this.position = position;
    }

    public abstract <T> T accept(AstVisitor<T> visitor);
}
