package modist.antlrdemo.frontend.ast.node;

import java.util.List;

public final class ProgramNode extends BaseAstNode {
    public List<ClassDeclarationNode> classes;
    public List<VariableDeclarationNode> variables;
    public List<FunctionDeclarationNode> functions;
}