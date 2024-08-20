package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.semantic.Type;

import java.util.List;

public final class ArrayAst extends BaseAst implements ExpressionOrArrayAst {
    public Type type;
    public List<ExpressionOrArrayAst> elements;

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public void setType(Type type) {
        this.type = type;
    }
}
