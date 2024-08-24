package modist.antlrdemo.frontend.ir.node;

// program - definition - block - instruction
public sealed interface Ir permits BlockIr, ClassDefinitionIr, ConstantStringIr, FunctionDeclarationIr, FunctionIr, FunctionVarargsDeclarationIr, GlobalVariableIr, InstructionIr, ProgramIr {
}
