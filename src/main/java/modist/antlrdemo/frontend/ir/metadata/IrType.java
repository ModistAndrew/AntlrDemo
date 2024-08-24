package modist.antlrdemo.frontend.ir.metadata;

// use string rather than this for custom type used in member and subscript
public enum IrType {
    I32(new Constant.Int(0)), I1(new Constant.Bool(false)), PTR(Constant.Null.INSTANCE), VOID(null);

    // we are on a 32-bit machine
    // assuming 4 bytes for every type (for simplicity, as we don't know how the memory is laid out)
    public static final int MAX_BYTE_SIZE = 4;
    public final Constant defaultValue;

    IrType(Constant defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}