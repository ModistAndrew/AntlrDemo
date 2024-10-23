package modist.antlrdemo.frontend.ir.metadata;

import org.jetbrains.annotations.Nullable;

// value may be null if it is not initialized
// value may be a reference to another variable
public record VariableDef(String name, IrType type, @Nullable IrOperand value) implements VariableReference {
}
