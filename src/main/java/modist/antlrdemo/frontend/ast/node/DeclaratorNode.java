package modist.antlrdemo.frontend.ast.node;

public abstract sealed class DeclaratorNode extends BaseAstNode permits ClassDeclarationNode, ConstructorDeclarationNode, FunctionDeclarationNode, ParameterDeclarationNode, VariableDeclaratorNode {
    public String name;
}