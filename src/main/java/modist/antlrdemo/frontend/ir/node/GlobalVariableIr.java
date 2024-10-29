package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.metadata.IrGlobal;
import modist.antlrdemo.frontend.ir.metadata.IrType;

public record GlobalVariableIr(IrGlobal result, IrType type) implements Ir {
}
