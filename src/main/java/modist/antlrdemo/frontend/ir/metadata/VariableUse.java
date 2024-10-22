package modist.antlrdemo.frontend.ir.metadata;

public final class VariableUse implements IrOperand, VariableReference {
    public final String name;
    // to be filled in Mem2Reg
    public IrConcrete value;

    public VariableUse(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name; // TODO: change to value.toString() after Mem2Reg
    }
}