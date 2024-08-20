package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.metadata.IrType;

import java.util.ArrayList;
import java.util.List;

public final class ClassIr extends DefinitionIr {
    public final List<IrType> members = new ArrayList<>();

    public ClassIr(String name) {
        super(name);
    }
}
