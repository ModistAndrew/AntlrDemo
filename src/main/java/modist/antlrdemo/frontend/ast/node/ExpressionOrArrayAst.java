package modist.antlrdemo.frontend.ast.node;

public sealed interface ExpressionOrArrayAst extends IAst permits ExpressionAst, ArrayAst {
}
