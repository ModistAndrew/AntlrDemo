package modist.antlrdemo.frontend.ast.node;

import java.util.List;

public final class VariableDeclarationNode extends BaseAstNode implements ForInitializationNode {
    public TypeNode type;
    public List<VariableDeclaratorNode> declarators;
}