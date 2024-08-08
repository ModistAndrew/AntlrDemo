package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.AstVisitor;
import modist.antlrdemo.frontend.ast.metadata.Position;

import java.util.List;

public class ArrayInitializerNode extends BaseAstNode implements VariableInitializerNode {
    public List<VariableInitializerNode> variableInitializers;
    public ArrayInitializerNode(Position position) {
        super(position);
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}