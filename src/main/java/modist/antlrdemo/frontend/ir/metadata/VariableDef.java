package modist.antlrdemo.frontend.ir.metadata;

// value may be a reference to another variable
public record VariableDef(String name, IrType type, IrOperand value) implements VariableReference {
}
