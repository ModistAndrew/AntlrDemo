package modist.antlrdemo.frontend.ast.node;

import java.util.List;

public final class ProgramAst extends BaseAst {
    public List<DefinitionAst> definitions; // keep the order of definitions
    public List<DefinitionAst.Class> classes; // store references to classes
    public List<DefinitionAst.Function> functions; // store references to functions
    public List<DefinitionAst.Variable> variables; // store references to variables
}