package modist.antlrdemo.frontend.metadata;

public sealed interface LiteralEnum {
    record Int(int value) implements LiteralEnum {
    }

    record Bool(boolean value) implements LiteralEnum {
    }

    record Str(String value) implements LiteralEnum {
    }

    enum Null implements LiteralEnum {
        INSTANCE
    }
}
