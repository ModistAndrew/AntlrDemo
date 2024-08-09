package modist.antlrdemo.frontend.ast.node;

import java.util.List;

public abstract sealed class ArrayCreatorNode extends BaseAstNode {
    public static final class Empty extends ArrayCreatorNode {
        public List<ExpressionNode> initializedLengths;
        public int emptyDimension;
    }

    public static final class Literal extends ArrayCreatorNode {
        public int dimension;
        public ArrayInitializerNode initializer;
    }
}
