package modist.antlrdemo.frontend.syntax.node;

public abstract sealed class DeclaratorNode extends AstNode permits ClassDeclarationNode, ConstructorDeclarationNode, FunctionDeclarationNode, ParameterDeclarationNode, VariableDeclaratorNode {
    public String name;
}