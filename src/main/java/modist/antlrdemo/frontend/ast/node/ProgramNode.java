package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.Position;

import java.util.List;

public class ProgramNode extends BaseAstNode {
    public List<ClassDeclarationNode> classes;
    public List<VariableDeclarationNode> variables;
    public List<FunctionDeclarationNode> functions;

    public ProgramNode(Position position) {
        super(position);
    }
}