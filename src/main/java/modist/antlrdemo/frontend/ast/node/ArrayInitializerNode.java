package modist.antlrdemo.frontend.ast.node;

import java.util.List;

public final class ArrayInitializerNode extends BaseAstNode implements VariableInitializerNode {
    public List<VariableInitializerNode> variableInitializers;
}