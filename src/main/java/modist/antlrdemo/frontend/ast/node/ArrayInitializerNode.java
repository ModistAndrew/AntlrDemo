package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.Position;

import java.util.List;

public class ArrayInitializerNode extends BaseAstNode implements VariableInitializerNode {
    public List<VariableInitializerNode> variableInitializers;

    public ArrayInitializerNode(Position position) {
        super(position);
    }
}