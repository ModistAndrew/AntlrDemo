package modist.antlrdemo.frontend.ir.metadata;

import modist.antlrdemo.frontend.semantic.Type;

public enum IrType {
    I32(new Constant.Int(0)), I1(new Constant.Bool(false)), PTR(Constant.Null.INSTANCE), VOID(null);

    public final Constant defaultValue;

    IrType(Constant defaultValue) {
        this.defaultValue = defaultValue;
    }

    public static IrType fromType(Type type) {
        if (type.isInt()) {
            return I32;
        } else if (type.isBool()) {
            return I1;
        } else if (type.isVoid()) {
            return VOID;
        } else {
            return PTR;
        }
    }
}
