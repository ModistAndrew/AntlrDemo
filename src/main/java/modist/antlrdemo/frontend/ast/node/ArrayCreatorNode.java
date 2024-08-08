package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.AstVisitor;
import modist.antlrdemo.frontend.ast.metadata.Position;

import java.util.List;

public abstract class ArrayCreatorNode extends BaseAstNode implements AstNode {
    public ArrayCreatorNode(Position position) {
        super(position);
    }

    public static class Empty extends ArrayCreatorNode {
        public List<ExpressionNode> initializedLengths;
        public int emptyDimension;

        public Empty(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    public static class Literal extends ArrayCreatorNode {
        public int dimension;
        public ArrayInitializerNode initializer;

        public Literal(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }
    }
}
