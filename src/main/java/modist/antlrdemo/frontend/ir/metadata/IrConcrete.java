package modist.antlrdemo.frontend.ir.metadata;

public sealed interface IrConcrete extends IrOperand permits IrConstant, IrGlobal, IrRegister, IrUndefined {
    @Override
    default IrConcrete asConcrete() {
        return this;
    }
}