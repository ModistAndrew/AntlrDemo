package modist.antlrdemo.frontend.syntax.node;

import java.util.List;

public final class ProgramNode extends AstNode {
    public List<ClassDeclarationNode> classes;
    public List<VariableDeclarationNode> variables;
    public List<FunctionDeclarationNode> functions;
}