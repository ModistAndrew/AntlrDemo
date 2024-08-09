package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.Position;

public class ParameterDeclarationNode extends DeclaratorNode {
    public TypeNode type;

    public ParameterDeclarationNode(Position position, String name) {
        super(position, name);
    }
}