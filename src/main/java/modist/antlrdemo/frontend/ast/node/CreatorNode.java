package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.AstVisitor;
import modist.antlrdemo.frontend.ast.metadata.Position;

public class CreatorNode extends AstNode {
    public CreatorNode(Position position) {
        super(position);
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}