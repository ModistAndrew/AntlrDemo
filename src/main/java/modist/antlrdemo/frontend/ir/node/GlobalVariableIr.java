package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.metadata.IrType;

public final class GlobalVariableIr extends DefinitionIr {
    public final IrType type;

    public GlobalVariableIr(String name, IrType type) {
        super(name);
        this.type = type;
    }
    // global variables have default value according to type
    // they should be initialized in init function
}
