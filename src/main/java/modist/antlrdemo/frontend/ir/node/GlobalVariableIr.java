package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.semantic.Type;

public final class GlobalVariableIr extends DefinitionIr {
    public final Type type;

    public GlobalVariableIr(String name, Type type) {
        super(name);
        this.type = type;
    }
    // global variables have default value according to type
    // they should be initialized in init function
}
