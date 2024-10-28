package modist.antlrdemo.frontend.ir.metadata;

public sealed interface IrOperand permits IrConcrete, IrConstant, IrDynamic, IrGlobal, IrRegister, IrUndefined, VariableUse {
    IrConcrete asConcrete();
}
