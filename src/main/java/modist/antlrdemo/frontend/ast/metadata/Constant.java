package modist.antlrdemo.frontend.ast.metadata;

public sealed interface Constant {
    record Int(int value) implements Constant {
    }

    record Bool(boolean value) implements Constant {
    }

    record Str(String value) implements Constant {
    }

    enum Null implements Constant {
        INSTANCE
    }
}
