package modist.antlrdemo.backend.asm;

import java.util.ArrayList;
import java.util.List;

public final class ProgramAsm implements Asm {
    public final List<GlobalVariableAsm> data = new ArrayList<>();
    public final List<ConstantStringAsm> roData = new ArrayList<>();
    public final List<FunctionAsm> text = new ArrayList<>();
}