package modist.antlrdemo.frontend.syntax.node;

import org.jetbrains.annotations.Nullable;

public final class VariableDeclaratorNode extends DeclaratorNode {
    @Nullable
    public VariableInitializerNode initializer;
}