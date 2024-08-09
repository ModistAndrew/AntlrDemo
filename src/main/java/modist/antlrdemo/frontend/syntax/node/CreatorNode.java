package modist.antlrdemo.frontend.syntax.node;

import org.jetbrains.annotations.Nullable;

public final class CreatorNode extends AstNode {
    public TypeNameNode typeName;
    @Nullable
    public ArrayCreatorNode arrayCreator;
}