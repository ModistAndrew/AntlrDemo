package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.AstVisitor;
import modist.antlrdemo.frontend.ast.metadata.Position;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ClassDeclarationNode extends DeclarationNode {
    @Nullable
    public ConstructorDeclarationNode constructor;
    public final List<FunctionDeclarationNode> functions = new ArrayList<>();
    public final List<VariableDeclarationNode> variables = new ArrayList<>();

    public ClassDeclarationNode(Position position) {
        super(position);
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
