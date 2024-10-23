package modist.antlrdemo.frontend.ir.metadata;

// notice that this can be used both as a "pointer" and as a concrete value
// should always be used in loadPointer and storePointer
public final class VariableUse implements IrDynamic, IrOperand, VariableReference {
    public final String name;
    // to be filled in Mem2Reg
    public IrConcrete value;

    public VariableUse(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
