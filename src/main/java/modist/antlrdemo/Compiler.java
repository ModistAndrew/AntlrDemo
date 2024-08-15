package modist.antlrdemo;

import modist.antlrdemo.frontend.error.CompileException;
import modist.antlrdemo.frontend.semantic.SemanticChecker;
import modist.antlrdemo.frontend.syntax.AstBuilder;
import modist.antlrdemo.frontend.syntax.node.ProgramNode;
import modist.antlrdemo.frontend.error.FastFailErrorListener;
import modist.antlrdemo.frontend.grammar.MxLexer;
import modist.antlrdemo.frontend.grammar.MxParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Recognizer;

import java.io.IOException;
import java.io.InputStream;

public class Compiler {
    public static void main(String[] args) throws IOException {
        try {
            frontend();
        } catch (CompileException e) {
            System.out.println(e.errorType.name);
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
        ProgramNode node = astBuilder.visitProgram(parser.program());
        SemanticChecker semanticChecker = new SemanticChecker();
        semanticChecker.check(node);
    }

    private static void setFastFailErrorListener(Recognizer<?, ?> recognizer) {
        recognizer.removeErrorListeners();
        recognizer.addErrorListener(new FastFailErrorListener());
    }
}
