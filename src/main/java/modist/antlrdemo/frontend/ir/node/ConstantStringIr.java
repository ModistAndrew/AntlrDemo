package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.metadata.IrGlobal;

public record ConstantStringIr(IrGlobal result, String value) implements Ir {
}
