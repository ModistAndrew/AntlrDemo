package modist.antlrdemo;

import modist.antlrdemo.frontend.error.CompileException;
import modist.antlrdemo.frontend.ir.IrBuilder;
import modist.antlrdemo.frontend.ir.node.ProgramIr;
import modist.antlrdemo.frontend.semantic.SemanticChecker;
import modist.antlrdemo.frontend.ast.AstBuilder;
import modist.antlrdemo.frontend.ast.node.ProgramAst;
import modist.antlrdemo.frontend.error.FastFailErrorListener;
import modist.antlrdemo.frontend.grammar.MxLexer;
import modist.antlrdemo.frontend.grammar.MxParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Recognizer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class Compiler {
    public static void main(String[] args) throws IOException {
        List<String> argList = Arrays.asList(args);
        try {
            frontend();
        } catch (CompileException e) {
            if (argList.contains("--debug")) {
                System.out.printf("%s at [%s]: %s%n", e.getErrorType(), e.getPosition(), e.getMessage());
            } else {
                System.out.println(e.getErrorType());
            }
            System.exit(-1);
        }
    }

    private static void frontend() throws IOException {
        InputStream input = System.in;
        MxLexer lexer = new MxLexer(CharStreams.fromStream(input));
        setFastFailErrorListener(lexer);
        MxParser parser = new MxParser(new CommonTokenStream(lexer));
        setFastFailErrorListener(parser);
        AstBuilder astBuilder = new AstBuilder();
        ProgramAst ast = astBuilder.visitProgram(parser.program());
        SemanticChecker semanticChecker = new SemanticChecker();
        semanticChecker.check(ast);
        IrBuilder irBuilder = new IrBuilder();
        ProgramIr ir = irBuilder.visitProgram(ast);
        System.out.println(ir);
    }

    private static void setFastFailErrorListener(Recognizer<?, ?> recognizer) {
        recognizer.removeErrorListeners();
        recognizer.addErrorListener(new FastFailErrorListener());
    }
}
