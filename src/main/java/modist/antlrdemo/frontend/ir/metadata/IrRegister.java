package modist.antlrdemo.frontend.ir.metadata;

import modist.antlrdemo.frontend.ir.IrNamer;

public record IrRegister(String name) implements IrConcrete, IrDynamic, IrOperand {
    public static final IrRegister THIS = new IrRegister(IrNamer.THIS_VAR);

    @Override
    public String toString() {
        return name;
    }
}
