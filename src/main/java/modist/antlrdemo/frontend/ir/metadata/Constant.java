package modist.antlrdemo.frontend.ir.metadata;

public sealed interface Constant extends Variable {
    record Int(int value) implements Constant {
    }

    record Bool(boolean value) implements Constant {
    }

    enum Null implements Constant {
        INSTANCE
    }
}