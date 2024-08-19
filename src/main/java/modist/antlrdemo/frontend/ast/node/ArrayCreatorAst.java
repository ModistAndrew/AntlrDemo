package modist.antlrdemo.frontend.ast.node;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ArrayCreatorAst extends Ast {
    // not empty, may contain null
    public List<ExpressionAst> dimensions;
    @Nullable
    public ArrayAst initializer;
}
