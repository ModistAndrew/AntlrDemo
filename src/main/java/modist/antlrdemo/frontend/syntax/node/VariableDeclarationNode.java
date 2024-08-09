package modist.antlrdemo.frontend.syntax.node;

import java.util.List;

public final class VariableDeclarationNode extends AstNode implements ForInitializationNode {
    public TypeNode type;
    public List<VariableDeclaratorNode> declarators;
}