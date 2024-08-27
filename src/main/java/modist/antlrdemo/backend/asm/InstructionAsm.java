package modist.antlrdemo.backend.asm;

import modist.antlrdemo.backend.metadata.Opcode;
import modist.antlrdemo.backend.metadata.Register;

public sealed interface InstructionAsm extends Asm {
    sealed interface Result extends InstructionAsm {
        Register result();
    }

    record Un(Register result, Opcode opcode, Register operand) implements Result {
    }

    record Bin(Register result, Opcode opcode, Register left, Register right) implements Result {
    }

    record BinImm(Register result, Opcode opcode, Register left, int immediate) implements Result {
    }

    record Li(Register result, int immediate) implements Result {
    }

    record Lw(Register result, int offset, Register base) implements Result {
    }

    record LwLabel(Register result, String label) implements Result {
    }

    record Sw(Register value, int offset, Register base) implements InstructionAsm {
    }

    record SwLabel(Register value, String label) implements InstructionAsm {
    }

    record Beqz(Register value, String label) implements InstructionAsm {
    }

    record J(String label) implements InstructionAsm {
    }

    record Call(String function) implements InstructionAsm {
    }

    record Ret() implements InstructionAsm {
    }

    record Mv(Register destination, Register value) implements InstructionAsm {
    }
}
