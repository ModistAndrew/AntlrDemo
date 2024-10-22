package modist.antlrdemo.frontend.ir.metadata;

public record VariableDef(String name, IrOperand value) implements VariableReference {
}
