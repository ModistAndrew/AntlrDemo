package modist.antlrdemo.frontend.ast.node;

import java.util.List;

public final class FormatStringNode extends BaseAstNode {
    public List<String> texts;
    public List<ExpressionNode> expressions;
}
