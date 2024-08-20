package modist.antlrdemo.frontend.ir.node;

// program - definition - block - instruction
public sealed interface Ir permits BlockIr, DefinitionIr, FunctionDeclarationIr, InstructionIr, ProgramIr {
}
