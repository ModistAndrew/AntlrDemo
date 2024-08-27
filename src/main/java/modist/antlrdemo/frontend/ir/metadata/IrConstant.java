package modist.antlrdemo.frontend.ir.metadata;

public sealed interface IrConstant extends IrOperand {
    record Int(int value) implements IrConstant {
        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }

    record Bool(boolean value) implements IrConstant {
        @Override
        public String toString() {
            return Boolean.toString(value);
        }
    }

    enum Null implements IrConstant {
        INSTANCE;

        @Override
        public String toString() {
            return "null";
        }
    }
}