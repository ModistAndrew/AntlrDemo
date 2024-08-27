package modist.antlrdemo.frontend.ir.metadata;

import modist.antlrdemo.frontend.ir.IrNamer;

public record IrRegister(String name) implements IrOperand {
    public static final IrRegister THIS = new IrRegister(IrNamer.THIS_VAR);

    public static IrRegister createConstantString() {
        return new IrRegister(IrNamer.constantString());
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isGlobal() {
        return IrNamer.isGlobal(name);
    }
}
