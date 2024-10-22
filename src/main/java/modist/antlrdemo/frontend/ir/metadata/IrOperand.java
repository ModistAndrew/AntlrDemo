package modist.antlrdemo.frontend.ir.metadata;

public sealed interface IrOperand permits IrConcrete, VariableUse {
    IrConcrete asConcrete();
}
