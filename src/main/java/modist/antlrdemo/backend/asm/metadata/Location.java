package modist.antlrdemo.backend.asm.metadata;

public sealed interface Location {
    record Reg(Register register) implements Location {
    }

    record Stack(int offset) implements Location {
    }
}
