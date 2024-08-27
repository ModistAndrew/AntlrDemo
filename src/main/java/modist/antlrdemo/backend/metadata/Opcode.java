package modist.antlrdemo.backend.metadata;

public enum Opcode {
    ADD, SUB, MUL, DIV, REM,
    SLL, SRA,
    AND, OR, XOR,
    SLT, SEQZ,
    ADDI,
    SLLI, SRAI,
    ANDI, ORI, XORI,
    SLTI;

    @Override
    public String toString(){
        return name().toLowerCase();
    }
}
