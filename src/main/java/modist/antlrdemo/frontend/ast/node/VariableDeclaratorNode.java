package modist.antlrdemo.frontend.ast.node;

import org.jetbrains.annotations.Nullable;

public final class VariableDeclaratorNode extends DeclaratorNode {
    @Nullable
    public VariableInitializerNode initializer;
}