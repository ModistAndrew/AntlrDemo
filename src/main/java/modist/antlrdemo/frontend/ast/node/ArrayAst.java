package modist.antlrdemo.frontend.ast.node;

import java.util.List;

public final class ArrayAst extends ExpressionOrArrayAst {
    public List<ExpressionOrArrayAst> elements;
}
