package modist.antlrdemo.frontend.syntax.node;

public final class TypeNode extends AstNode {
    public TypeNameEnum typeName;
    public int dimension;

    public sealed interface TypeNameEnum {
        enum Primitive implements TypeNameEnum {
            INT, BOOL, STRING
        }

        record Reference(String name) implements TypeNameEnum {
        }
    }
}