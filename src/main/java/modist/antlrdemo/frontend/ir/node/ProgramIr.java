package modist.antlrdemo.frontend.ir.node;

import java.util.ArrayList;
import java.util.List;

public final class ProgramIr implements Ir {
    public final List<ConstantStringIr> constantStrings = new ArrayList<>();
    public final List<ClassIr> classes = new ArrayList<>();
    public final List<GlobalVariableIr> globalVariables = new ArrayList<>();
    public final List<FunctionDeclarationIr> functionDeclarations = new ArrayList<>();
    public final List<FunctionIr> functions = new ArrayList<>();
}
