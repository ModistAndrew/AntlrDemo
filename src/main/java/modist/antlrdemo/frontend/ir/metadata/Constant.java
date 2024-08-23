package modist.antlrdemo.frontend.ir.metadata;

public sealed interface Constant extends Variable {
    record Int(int value) implements Constant {
        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }

    record Bool(boolean value) implements Constant {
        @Override
        public String toString() {
            return Boolean.toString(value);
        }
    }

    enum Null implements Constant {
        INSTANCE;

        @Override
        public String toString() {
            return "null";
        }
    }
}