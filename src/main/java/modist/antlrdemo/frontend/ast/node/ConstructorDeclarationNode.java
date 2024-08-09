package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.Position;

public class ConstructorDeclarationNode extends DeclaratorNode {
    public BlockNode body;

    public ConstructorDeclarationNode(Position position, String name) {
        super(position, name);
    }
}
