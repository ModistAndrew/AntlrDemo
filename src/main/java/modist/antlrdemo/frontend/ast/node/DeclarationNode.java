package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.metadata.Position;

public abstract class DeclarationNode extends BaseAstNode {
    public String name;

    public DeclarationNode(Position position) {
        super(position);
    }
}