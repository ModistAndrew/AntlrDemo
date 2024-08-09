package modist.antlrdemo.frontend.syntax.node;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class FunctionDeclarationNode extends DeclaratorNode {
    @Nullable
    public TypeNode returnType;
    public List<ParameterDeclarationNode> parameters;
    public BlockNode body;
}