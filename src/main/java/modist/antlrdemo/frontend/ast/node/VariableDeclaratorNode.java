package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.AstVisitor;
import modist.antlrdemo.frontend.ast.metadata.Position;
import org.jetbrains.annotations.Nullable;

public class VariableDeclaratorNode extends DeclaratorNode {
    @Nullable
    public VariableInitializerNode initializer;

    public VariableDeclaratorNode(Position position, String name) {
        super(position, name);
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}