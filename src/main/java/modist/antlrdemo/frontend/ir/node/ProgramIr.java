package modist.antlrdemo.frontend.ir.node;

import java.util.ArrayList;
import java.util.List;

public final class ProgramIr implements Ir {
    public final List<DefinitionIr> definitions = new ArrayList<>();
    public final List<FunctionDeclarationIr> functionDeclarations = new ArrayList<>();
}
