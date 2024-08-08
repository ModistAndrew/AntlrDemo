package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.AstVisitor;
import modist.antlrdemo.frontend.ast.metadata.Position;

public class VariableDeclarationNode extends DeclarationNode implements ForInitializationNode {
    public VariableDeclarationNode(Position position) {
        super(position);
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}