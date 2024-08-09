package modist.antlrdemo.frontend.ast.node;

import org.jetbrains.annotations.Nullable;

public final class CreatorNode extends BaseAstNode {
    public TypeNameNode typeName;
    @Nullable
    public ArrayCreatorNode arrayCreator;
}