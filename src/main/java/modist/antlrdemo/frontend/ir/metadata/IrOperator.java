package modist.antlrdemo.frontend.ir.metadata;

public enum IrOperator {
    ADD, SUB, MUL, SDIV, SREM, SHL, ASHR, AND, OR, XOR,
    EQ, NE, SLT, SGT, SLE, SGE;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
