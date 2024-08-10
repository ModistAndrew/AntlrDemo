package modist.antlrdemo.frontend.syntax.node;

import java.util.List;

public final class ProgramNode extends AstNode {
    public List<DeclarationNode.Class> classes;
    public List<DeclarationNode.Variable> variables;
    public List<DeclarationNode.Function> functions;
}