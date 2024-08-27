package modist.antlrdemo.backend.asm.node;

import modist.antlrdemo.backend.asm.metadata.Opcode;
import modist.antlrdemo.backend.asm.metadata.Register;

public sealed interface InstructionAsm extends Asm {
    sealed interface Result extends InstructionAsm {
        Register result();
    }

    sealed interface Immediate extends InstructionAsm {
        int immediate();

        default boolean isImmInRange() {
            return immediate() >= -2048 && immediate() < 2048;
        }
    }

    record Un(Register result, Opcode opcode, Register operand) implements Result {
    }

    record Bin(Register result, Opcode opcode, Register left, Register right) implements Result {
    }

    record BinImm(Register result, Opcode opcode, Register left, int immediate) implements Result, Immediate {
    }

    record La(Register result, String label) implements Result {
    }

    // doesn't implement Immediate because its immediate should never be out of range
    record Li(Register result, int immediate) implements Result {
    }

    record Lw(Register result, int immediate, Register base) implements Result, Immediate {
    }

    record Sw(Register value, int immediate, Register base) implements Immediate {
    }

    record SwLabel(Register value, String label) implements InstructionAsm {
    }

    record Beqz(Register value, String label) implements InstructionAsm {
    }

    record Bnez(Register value, String label) implements InstructionAsm {
    }

    record J(String label) implements InstructionAsm {
    }

    record Call(String function) implements InstructionAsm {
    }

    record Ret() implements InstructionAsm {
    }
}
