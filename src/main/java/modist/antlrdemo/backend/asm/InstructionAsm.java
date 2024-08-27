package modist.antlrdemo.backend.asm;

import modist.antlrdemo.backend.metadata.Opcode;
import modist.antlrdemo.backend.metadata.Register;

public sealed interface InstructionAsm extends Asm {
    record Bin(Register result, Opcode opcode, Register left, Register right) implements InstructionAsm {
    }

    record BinImm(Register result, Opcode opcode, Register left, int immediate) implements InstructionAsm {
    }

    record Li(Register result, int immediate) implements InstructionAsm {
    }

    record Lw(Register result, int offset, Register base) implements InstructionAsm {
    }

    record LwLabel(Register result, String label) implements InstructionAsm {
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

    record Mv(Register result, Register value) implements InstructionAsm {
    }
}
