package modist.antlrdemo.frontend.ir.metadata;

public sealed interface IrConcrete extends IrOperand permits IrRegister, IrConstant {
    @Override
    default IrConcrete asConcrete() {
        return this;
    }
}