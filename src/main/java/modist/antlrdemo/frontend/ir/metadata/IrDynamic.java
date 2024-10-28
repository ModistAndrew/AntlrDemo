package modist.antlrdemo.frontend.ir.metadata;

public sealed interface IrDynamic extends IrOperand permits IrGlobal, IrRegister, VariableUse {
}