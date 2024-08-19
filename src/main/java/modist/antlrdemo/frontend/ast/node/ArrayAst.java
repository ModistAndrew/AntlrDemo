package modist.antlrdemo.frontend.ast.node;

import java.util.List;

public final class ArrayAst extends Ast implements ExpressionOrArrayAst {
    public List<ExpressionOrArrayAst> elements;
}
