package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.Position;

public class TypeNameNode extends BaseAstNode {
    public TypeNameEnum value;

    public TypeNameNode(Position position) {
        super(position);
    }

    public interface TypeNameEnum {
        enum Primitive implements TypeNameEnum {
            INT, BOOL, STRING
        }

        record Reference(String name) implements TypeNameEnum {
        }
    }
}