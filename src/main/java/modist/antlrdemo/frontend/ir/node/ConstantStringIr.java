package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.metadata.Register;

public record ConstantStringIr(Register result, String value) implements Ir {
}
