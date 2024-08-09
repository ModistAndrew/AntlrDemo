package modist.antlrdemo.frontend.syntax.node;

import java.util.List;

public final class ArrayInitializerNode extends AstNode implements VariableInitializerNode {
    public List<VariableInitializerNode> variableInitializers;
}