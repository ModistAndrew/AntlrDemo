package modist.antlrdemo.frontend.ir.metadata;

public sealed interface IrConstant extends IrConcrete, IrOperand {
    int asImmediate();

    record Int(int value) implements IrConstant {
        @Override
        public String toString() {
            return Integer.toString(value);
        }

        @Override
        public int asImmediate() {
            return value;
        }
    }

    record Bool(boolean value) implements IrConstant {
        @Override
        public String toString() {
            return Boolean.toString(value);
        }

        @Override
        public int asImmediate() {
            return value ? 1 : 0;
        }
    }

    enum Null implements IrConstant {
        INSTANCE;

        @Override
        public String toString() {
            return "null";
        }

        @Override
        public int asImmediate() {
            return 0;
        }
    }
}