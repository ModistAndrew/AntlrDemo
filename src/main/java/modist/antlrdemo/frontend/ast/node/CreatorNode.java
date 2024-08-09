package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.Position;
import org.jetbrains.annotations.Nullable;

public class CreatorNode extends BaseAstNode {
    public TypeNameNode typeName;
    @Nullable
    public ArrayCreatorNode arrayCreator;

    public CreatorNode(Position position) {
        super(position);
    }
}