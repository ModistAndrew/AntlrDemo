package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.Position;

public class TypeNode extends BaseAstNode {
    public TypeNameNode typeName;
    public int dimension;

    public TypeNode(Position position) {
        super(position);
    }
}