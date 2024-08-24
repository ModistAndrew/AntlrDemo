package modist.antlrdemo.frontend.ast.node;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class ArrayCreatorAst extends BaseAst {
    public List<ExpressionAst> dimensions; // not empty, may contain null
    @Nullable
    public ArrayAst initializer;
    // in Type
    public final List<ExpressionAst> presentDimensions = new ArrayList<>();
}
