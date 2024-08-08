package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.AstVisitor;
import modist.antlrdemo.frontend.ast.metadata.Position;

public class ParameterDeclarationNode extends DeclaratorNode {
    public TypeNode type;

    public ParameterDeclarationNode(Position position, String name) {
        super(position, name);
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}