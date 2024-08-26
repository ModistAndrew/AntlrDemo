package modist.antlrdemo.backend.asm;

public sealed interface Asm permits BlockAsm, ConstantStringAsm, FunctionAsm, GlobalVariableAsm, InstructionAsm, ProgramAsm {
}
