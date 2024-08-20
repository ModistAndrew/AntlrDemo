package modist.antlrdemo.frontend.ir.node;

public abstract sealed class DefinitionIr implements Ir permits ClassIr, FunctionIr, GlobalVariableIr {
    public final String name;

    public DefinitionIr(String name) {
        this.name = name;
    }
}
