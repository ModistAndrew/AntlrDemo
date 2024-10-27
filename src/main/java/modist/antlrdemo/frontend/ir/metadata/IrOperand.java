package modist.antlrdemo.frontend.ir.metadata;

public sealed interface IrOperand permits IrConcrete, IrDynamic, IrConstant, IrRegister, VariableUse {
    IrConcrete asConcrete();
}
