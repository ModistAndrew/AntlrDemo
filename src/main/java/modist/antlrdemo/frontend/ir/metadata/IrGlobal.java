package modist.antlrdemo.frontend.ir.metadata;

import modist.antlrdemo.frontend.ir.IrNamer;

public record IrGlobal(String name) implements IrConcrete, IrDynamic, IrOperand {
    public static IrGlobal createConstantString() {
        return new IrGlobal(IrNamer.constantString());
    }

    @Override
    public String toString() {
        return name;
    }
}
