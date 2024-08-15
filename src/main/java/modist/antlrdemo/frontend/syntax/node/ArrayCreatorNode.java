package modist.antlrdemo.frontend.syntax.node;

import java.util.List;

public abstract sealed class ArrayCreatorNode extends AstNode {
    public static final class Empty extends ArrayCreatorNode {
        public List<ExpressionNode> dimensionLengths;
        public int emptyDimension;
    }

    public static final class Init extends ArrayCreatorNode {
        public int dimension;
        public ArrayNode initializer;
    }
}
