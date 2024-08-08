package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.AstVisitor;
import modist.antlrdemo.frontend.ast.metadata.Position;

public class LiteralNode extends AstNode {
    public LiteralNode(Position position) {
        super(position);
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}