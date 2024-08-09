package modist.antlrdemo.frontend.syntax.node;

public final class TypeNameNode extends AstNode {
    public TypeNameEnum value;

    public sealed interface TypeNameEnum {
        enum Primitive implements TypeNameEnum {
            INT, BOOL, STRING
        }

        record Reference(String name) implements TypeNameEnum {
        }
    }
}