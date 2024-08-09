package modist.antlrdemo.frontend.ast.node;

public final class TypeNameNode extends BaseAstNode {
    public TypeNameEnum value;

    public sealed interface TypeNameEnum {
        enum Primitive implements TypeNameEnum {
            INT, BOOL, STRING
        }

        record Reference(String name) implements TypeNameEnum {
        }
    }
}