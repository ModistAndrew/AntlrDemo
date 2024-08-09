package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.Position;
import org.jetbrains.annotations.Nullable;

public class VariableDeclaratorNode extends DeclaratorNode {
    @Nullable
    public VariableInitializerNode initializer;

    public VariableDeclaratorNode(Position position, String name) {
        super(position, name);
    }
}