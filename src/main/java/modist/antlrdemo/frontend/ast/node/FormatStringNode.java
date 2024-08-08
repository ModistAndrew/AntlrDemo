package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.AstVisitor;
import modist.antlrdemo.frontend.ast.metadata.Position;

public abstract class FormatStringNode extends AstNode {
    public FormatStringNode(Position position) {
        super(position);
    }

    public static class Atom extends FormatStringNode {
        public Atom(Position position) {
            super(position);
        }

        @Override
        public void accept(AstVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class Complex extends FormatStringNode {
        public Complex(Position position) {
            super(position);
        }

        @Override
        public void accept(AstVisitor visitor) {
            visitor.visit(this);
        }
    }
}
