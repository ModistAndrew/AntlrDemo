package modist.antlrdemo.frontend.ir.node;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public final class ProgramIr implements Ir {
    public final List<ClassIr> classes = new ArrayList<>();
    public final List<GlobalVariableIr> globalVariables = new ArrayList<>();
    public final List<ConstantStringIr> constantStrings = new ArrayList<>();
    public final List<FunctionDeclarationIr> functionDeclarations = new ArrayList<>();
    public final List<FunctionVarargsDeclarationIr> functionVarargsDeclarations = new ArrayList<>();
    public final List<FunctionIr> functions = new ArrayList<>();

    @Override
    public String toString() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        if (!classes.isEmpty()) {
            classes.forEach(writer::println);
            writer.println();
        }
        if (!globalVariables.isEmpty() || !constantStrings.isEmpty()) {
            globalVariables.forEach(writer::println);
            constantStrings.forEach(writer::println);
            writer.println();
        }
        if (!functionDeclarations.isEmpty() || !functionVarargsDeclarations.isEmpty()) {
            functionDeclarations.forEach(writer::println);
            functionVarargsDeclarations.forEach(writer::println);
            writer.println();
        }
        functions.forEach(writer::println);
        return stringWriter.toString();
    }
}
