package modist.antlrdemo.frontend.ast.node;

import java.util.List;

public final class ArrayNode extends AstNode implements ExpressionOrArrayNode {
    public List<ExpressionOrArrayNode> elements;
}
