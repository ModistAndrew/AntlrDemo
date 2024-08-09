package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.Position;

import java.util.List;

public class ArgumentListNode extends BaseAstNode implements AstNode {
    public List<ExpressionNode> arguments;

    public ArgumentListNode(Position position) {
        super(position);
    }
}