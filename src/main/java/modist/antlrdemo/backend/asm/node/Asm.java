package modist.antlrdemo.backend.asm.node;

public sealed interface Asm permits BlockAsm, ConstantStringAsm, FunctionAsm, GlobalVariableAsm, InstructionAsm, ProgramAsm {
}
