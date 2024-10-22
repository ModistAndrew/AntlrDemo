package modist.antlrdemo.frontend.ir.metadata;

public record VariableDef(String name, IrType type, IrOperand value) implements VariableReference {
}
