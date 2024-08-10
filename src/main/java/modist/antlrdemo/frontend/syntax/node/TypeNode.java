package modist.antlrdemo.frontend.syntax.node;

public final class TypeNode extends AstNode {
    public TypeEnum type;
    public int dimension;

    public sealed interface TypeEnum {
        enum Primitive implements TypeEnum {
            INT, BOOL, STRING
        }

        record Reference(String name) implements TypeEnum {
        }
    }
}