package modist.antlrdemo.frontend.ir.metadata;

import modist.antlrdemo.frontend.ir.IrNamer;

public record Register(String name) implements Variable {
    public static final Register THIS = new Register(IrNamer.THIS_VAR);

    public static Register createConstantString() {
        return new Register(IrNamer.constantString());
    }
}
