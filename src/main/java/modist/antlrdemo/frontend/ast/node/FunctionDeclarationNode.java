package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.AstVisitor;
import modist.antlrdemo.frontend.ast.metadata.Position;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FunctionDeclarationNode extends DeclaratorNode {
    @Nullable
    public TypeNode returnType;
    public List<ParameterDeclarationNode> parameters;
    public BlockNode body;
    public FunctionDeclarationNode(Position position, String name) {
        super(position, name);
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}