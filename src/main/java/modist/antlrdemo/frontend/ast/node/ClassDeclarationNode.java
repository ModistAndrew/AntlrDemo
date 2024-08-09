package modist.antlrdemo.frontend.ast.node;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ClassDeclarationNode extends DeclaratorNode {
    @Nullable
    public ConstructorDeclarationNode constructor;
    public List<FunctionDeclarationNode> functions;
    public List<VariableDeclarationNode> variables;
}
