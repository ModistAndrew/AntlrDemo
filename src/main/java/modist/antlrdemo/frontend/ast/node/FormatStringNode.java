package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.AstVisitor;
import modist.antlrdemo.frontend.ast.metadata.Position;

import java.util.List;

public abstract class FormatStringNode extends BaseAstNode {
    public FormatStringNode(Position position) {
        super(position);
    }

    public static class Atom extends FormatStringNode {
        public String text;
        public Atom(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    public static class Complex extends FormatStringNode {
        public List<String> texts;
        public List<ExpressionNode> expressions;
        public Complex(Position position) {
            super(position);
        }

        @Override
        public <T> T accept(AstVisitor<T> visitor) {
            return visitor.visit(this);
        }
    }
}
