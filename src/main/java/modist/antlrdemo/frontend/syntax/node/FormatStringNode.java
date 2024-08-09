package modist.antlrdemo.frontend.syntax.node;

import java.util.List;

public final class FormatStringNode extends AstNode {
    public List<String> texts;
    public List<ExpressionNode> expressions;
}
