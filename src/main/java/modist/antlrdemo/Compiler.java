package modist.antlrdemo;

import modist.antlrdemo.backend.asm.AsmBuilder;
import modist.antlrdemo.backend.asm.AsmPrinter;
import modist.antlrdemo.backend.asm.node.ProgramAsm;
import modist.antlrdemo.optimize.*;
import modist.antlrdemo.frontend.ir.IrPrinter;
import modist.antlrdemo.frontend.semantic.error.CompileException;
import modist.antlrdemo.frontend.ir.IrBuilder;
import modist.antlrdemo.frontend.ir.node.ProgramIr;
import modist.antlrdemo.frontend.semantic.SemanticChecker;
import modist.antlrdemo.frontend.ast.AstBuilder;
import modist.antlrdemo.frontend.ast.node.ProgramAst;
import modist.antlrdemo.frontend.semantic.error.FastFailErrorListener;
import modist.antlrdemo.frontend.grammar.MxLexer;
import modist.antlrdemo.frontend.grammar.MxParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Recognizer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Compiler {
    public static void main(String[] args) throws IOException {
        List<String> argList = Arrays.asList(args);
        try {
            ProgramIr ir = frontend();
            if (argList.contains("--ir")) {
                new IrPrinter(System.out).print(ir);
            } else {
                ProgramAsm asm = backend(ir);
                new AsmPrinter(System.out).print(asm);
            }
        } catch (CompileException e) {
            if (argList.contains("--debug")) {
                System.err.printf("%s at [%s]: %s%n", e.getErrorType(), e.getPosition(), e.getMessage());
                throw e;
            } else {
                System.out.println(e.getErrorType());
                System.exit(-1);
            }
        }
    }

    private static ProgramIr frontend() throws IOException {
        MxLexer lexer = withFastFailErrorListener(new MxLexer(CharStreams.fromStream(System.in)));
        MxParser parser = withFastFailErrorListener(new MxParser(new CommonTokenStream(lexer)));
        ProgramAst ast = new AstBuilder().visitProgram(parser.program());
        new SemanticChecker().visit(ast);
        ProgramIr ir = new IrBuilder().visitProgram(ast);
        new ControlFlowGraphBuilder().visitProgram(ir);
        new DominatorTreeBuilder().visitProgram(ir);
        new Mem2Reg().visitProgram(ir);
        new LiveAnalysis().visitProgram(ir);
        new RegAlloc().visitProgram(ir);
        return ir;
    }

    private static ProgramAsm backend(ProgramIr ir) {
        return new AsmBuilder().visitProgram(ir);
    }

    private static <T extends Recognizer<?, ?>> T withFastFailErrorListener(T recognizer) {
        recognizer.removeErrorListeners();
        recognizer.addErrorListener(new FastFailErrorListener());
        return recognizer;
    }
}
