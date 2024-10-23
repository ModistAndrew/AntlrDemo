package modist.antlrdemo.frontend.ir.metadata;

import org.jetbrains.annotations.Nullable;

public record VariableDef(String name, IrType type, @Nullable IrOperand value) implements VariableReference {
}
