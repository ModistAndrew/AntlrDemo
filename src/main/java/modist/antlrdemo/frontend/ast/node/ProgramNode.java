package modist.antlrdemo.frontend.ast.node;

import java.util.List;

public final class ProgramNode extends AstNode {
    public List<DeclarationNode> declarations; // keep the order of declarations
    public List<DeclarationNode.Class> classes; // store references to classes
    public List<DeclarationNode.Function> functions; // store references to functions
    // variables are dealt in order, so no need to store them
}