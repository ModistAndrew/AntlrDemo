package modist.antlrdemo.frontend.syntax.node;

import java.util.List;

public final class ArrayNode extends AstNode implements ExpressionOrArrayNode {
    public List<ExpressionOrArrayNode> elements;
}
