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

    public static Opcode imm2reg(Opcode opcode) {
        return switch (opcode) {
            case ADDI -> ADD;
            case SLLI -> SLL;
            case SRAI -> SRA;
            case ANDI -> AND;
            case ORI -> OR;
            case XORI -> XOR;
            case SLTI -> SLT;
            default -> throw new IllegalArgumentException();
        };
    }

    public static Opcode reg2imm(Opcode opcode) {
        return switch (opcode) {
            case ADD -> ADDI;
            case SLL -> SLLI;
            case SRA -> SRAI;
            case AND -> ANDI;
            case OR -> ORI;
            case XOR -> XORI;
            case SLT -> SLTI;
            default -> throw new IllegalArgumentException();
        };
    }
}
