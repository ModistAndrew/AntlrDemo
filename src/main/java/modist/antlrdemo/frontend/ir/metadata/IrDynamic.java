package modist.antlrdemo.frontend.ir.metadata;

// not constant. pointers should always be dynamic
public sealed interface IrDynamic extends IrOperand permits IrGlobal, IrRegister, VariableUse {
}