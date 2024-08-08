package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.CompileException;
import modist.antlrdemo.frontend.ast.AstVisitor;
import modist.antlrdemo.frontend.ast.metadata.Position;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClassDeclarationNode extends DeclaratorNode {
    @Nullable
    public ConstructorDeclarationNode constructor;
    public List<FunctionDeclarationNode> functions;
    public List<VariableDeclarationNode> variables;

    public ClassDeclarationNode(Position position, String name) {
        super(position, name);
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public void trySetConstructor(List<ConstructorDeclarationNode> constructors) {
        if (constructors.size() > 1) {
            throw new CompileException(position, "Multiple constructors in class " + name);
        }
        constructor = constructors.isEmpty() ? null : constructors.get(0);
    }
}
