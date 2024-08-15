package modist.antlrdemo.frontend.syntax.node;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ArrayCreatorNode extends AstNode {
    // not empty, may contain null
    public List<ExpressionNode> dimensions;
    @Nullable
    public ArrayNode initializer;
}
