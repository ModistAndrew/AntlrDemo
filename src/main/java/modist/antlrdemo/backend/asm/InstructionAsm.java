package modist.antlrdemo.backend.asm;

public sealed interface InstructionAsm extends Asm {
    record BinaryInstructionAsm(String opcode, String lhs, String rhs, String result) implements InstructionAsm {
    }
}
