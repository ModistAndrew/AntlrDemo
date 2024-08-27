package modist.antlrdemo.frontend.ir.metadata;

import modist.antlrdemo.backend.asm.metadata.Opcode;

public enum IrOperator {
    ADD(Opcode.ADD), SUB(Opcode.SUB), MUL(Opcode.MUL), SDIV(Opcode.DIV), SREM(Opcode.REM),
    SHL(Opcode.SLL), ASHR(Opcode.SRA),
    AND(Opcode.AND), OR(Opcode.OR), XOR(Opcode.XOR),
    EQ, NE, SLT, SGT, SLE, SGE;

    public final Opcode opcode;

    IrOperator() {
        this.opcode = null;
    }

    IrOperator(Opcode opcode) {
        this.opcode = opcode;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
