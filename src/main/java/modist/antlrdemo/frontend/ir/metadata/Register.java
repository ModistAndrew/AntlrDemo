package modist.antlrdemo.frontend.ir.metadata;

import modist.antlrdemo.frontend.ir.NamingUtil;

public record Register(String name) implements Variable {
    public static final Register THIS = new Register(NamingUtil.THIS_VAR);

    public static Register createConstantString() {
        return new Register(NamingUtil.constantString());
    }
}
