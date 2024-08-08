package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.AstVisitor;
import modist.antlrdemo.frontend.ast.metadata.Position;

import java.util.List;

public class FormatStringNode extends BaseAstNode {
    public List<String> texts;
    public List<ExpressionNode> expressions;

    public FormatStringNode(Position position) {
        super(position);
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
