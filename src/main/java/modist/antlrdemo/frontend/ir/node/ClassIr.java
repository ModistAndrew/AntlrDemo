package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.metadata.IrType;

import java.util.List;

public record ClassIr(String name, List<IrType> members) implements Ir {
}
