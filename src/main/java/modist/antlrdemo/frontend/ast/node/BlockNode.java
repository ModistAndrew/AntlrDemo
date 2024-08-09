package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.Position;

import java.util.List;

public class BlockNode extends BaseAstNode {
    public List<StatementNode> statements;

    public BlockNode(Position position) {
        super(position);
    }
}