package modist.antlrdemo.frontend.ir.node;

// program - definition - block - instruction
public sealed interface Ir permits BlockIr, ClassIr, ConstantStringIr, FunctionDeclarationIr, FunctionIr, GlobalVariableIr, InstructionIr, ProgramIr {
}
