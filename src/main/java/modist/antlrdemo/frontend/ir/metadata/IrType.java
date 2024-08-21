package modist.antlrdemo.frontend.ir.metadata;

public enum IrType {
    I32(new Constant.Int(0)), I1(new Constant.Bool(false)), PTR(Constant.Null.INSTANCE), VOID(null);

    public final Constant defaultValue;

    IrType(Constant defaultValue) {
        this.defaultValue = defaultValue;
    }
}
