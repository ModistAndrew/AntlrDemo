package modist.antlrdemo.frontend.ast.metadata;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public record FunctionInfo(@Nullable TypeInfo returnType, List<TypeInfo> parameterTypes) implements ExpressionInfo {
    @Override
    public boolean isLvalue() {
        return false;
    }
}
