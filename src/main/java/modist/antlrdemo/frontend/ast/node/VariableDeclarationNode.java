package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.Position;

import java.util.List;

public class VariableDeclarationNode extends BaseAstNode implements ForInitializationNode {
    public TypeNode type;
    public List<VariableDeclaratorNode> declarators;

    public VariableDeclarationNode(Position position) {
        super(position);
    }
}