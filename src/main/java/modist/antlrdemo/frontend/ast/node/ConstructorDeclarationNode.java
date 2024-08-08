package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.AstVisitor;
import modist.antlrdemo.frontend.ast.metadata.Position;

public class ConstructorDeclarationNode extends DeclarationNode {
    public ConstructorDeclarationNode(Position position) {
        super(position);
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
