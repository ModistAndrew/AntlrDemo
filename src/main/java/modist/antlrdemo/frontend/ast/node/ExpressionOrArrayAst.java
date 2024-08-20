package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.semantic.Type;

public sealed interface ExpressionOrArrayAst extends Ast permits ExpressionAst, ArrayAst {
    Type getType();
    void setType(Type type);
}
