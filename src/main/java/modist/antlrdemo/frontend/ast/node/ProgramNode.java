package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.AstVisitor;
import modist.antlrdemo.frontend.ast.metadata.Position;

import java.util.ArrayList;
import java.util.List;

public class ProgramNode extends AstNode {
    public final List<ClassDeclarationNode> classes = new ArrayList<>();
    public final List<VariableDeclarationNode> variables = new ArrayList<>();
    public final List<FunctionDeclarationNode> functions = new ArrayList<>();

    public ProgramNode(Position position) {
        super(position);
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}