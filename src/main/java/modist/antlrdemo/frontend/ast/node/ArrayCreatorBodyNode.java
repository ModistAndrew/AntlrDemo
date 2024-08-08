package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.AstVisitor;
import modist.antlrdemo.frontend.ast.metadata.Position;

public abstract class ArrayCreatorBodyNode extends AstNode implements CreatorBodyNode {
    public ArrayCreatorBodyNode(Position position) {
        super(position);
    }

    public static class Empty extends ArrayCreatorBodyNode {
        public Empty(Position position) {
            super(position);
        }

        @Override
        public void accept(AstVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class Expression extends ArrayCreatorBodyNode {
        public Expression(Position position) {
            super(position);
        }

        @Override
        public void accept(AstVisitor visitor) {
            visitor.visit(this);
        }
    }
}
