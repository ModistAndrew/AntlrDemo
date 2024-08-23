package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.semantic.Type;

public abstract sealed class ExpressionOrArrayAst extends BaseAst permits ExpressionAst, ArrayAst {
    // in Type
    public Type type;
}
