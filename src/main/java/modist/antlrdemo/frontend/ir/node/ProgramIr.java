package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.metadata.IrGlobal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ProgramIr implements Ir {
    public final List<ClassDefinitionIr> classDefinitions = new ArrayList<>();
    public final List<GlobalVariableIr> globalVariables = new ArrayList<>();
    public final List<ConstantStringIr> constantStrings = new ArrayList<>();
    public final List<FunctionDeclarationIr> functionDeclarations = new ArrayList<>();
    public final List<FunctionVarargsDeclarationIr> functionVarargsDeclarations = new ArrayList<>();
    public final List<FunctionIr> functions = new ArrayList<>();
    // for LiveAnalysis
    public final HashMap<IrGlobal, Integer> globalRegisterMap = new HashMap<>();
}