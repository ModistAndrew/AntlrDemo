package modist.antlrdemo.frontend.ast.metadata;

public record TypeInfo(String name, int dimension, boolean isLvalue) implements ExpressionInfo {
    @Override
    public boolean isLvalue() {
        return isLvalue;
    }
}
