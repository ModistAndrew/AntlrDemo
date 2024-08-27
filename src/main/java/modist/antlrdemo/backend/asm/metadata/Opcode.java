package modist.antlrdemo.backend.asm.metadata;

public enum Opcode {
    ADD, SUB, MUL, DIV, REM,
    SLL, SRA,
    AND, OR, XOR,
    SLT,
    ADDI,
    SLLI, SRAI,
    ANDI, ORI, XORI,
    SLTI,
    SEQZ, SNEZ;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}