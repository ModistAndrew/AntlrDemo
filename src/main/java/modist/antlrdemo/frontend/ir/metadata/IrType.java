package modist.antlrdemo.frontend.ir.metadata;

// use string rather than this for custom type used in member and subscript
public enum IrType {
    I32(new IrConstant.Int(0)), I1(new IrConstant.Bool(false)), PTR(IrConstant.Null.NULL), VOID(null);

    // we are on a 32-bit machine
    // assuming 4 bytes for every type (for simplicity, as we don't know how the memory is laid out)
    public static final int MAX_BYTE_SIZE = 4;
    public static final int LOG_MAX_BYTE_SIZE = 2;
    public final IrConstant defaultValue;

    IrType(IrConstant defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}