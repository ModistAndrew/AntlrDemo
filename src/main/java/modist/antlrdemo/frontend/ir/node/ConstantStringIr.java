package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.IrPrinter;
import modist.antlrdemo.frontend.ir.metadata.Register;

public record ConstantStringIr(Register result, String value) implements Ir {
    @Override
    public String toString() {
        // c strings are null terminated
        return String.format("%s = constant [%d x i8] c\"%s\"",
                result, value.length() + 1, IrPrinter.escape(value) + "\\00");
    }
}
