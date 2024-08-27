package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.metadata.IrRegister;

public record ConstantStringIr(IrRegister result, String value) implements Ir {
}
