package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.IrStringUtil;
import modist.antlrdemo.frontend.ir.metadata.IrType;

import java.util.List;

public record ClassIr(String name, List<IrType> members) implements Ir {
    @Override
    public String toString() {
        return String.format("%s = type { %s }", name, IrStringUtil.toStringTypes(members));
    }
}
