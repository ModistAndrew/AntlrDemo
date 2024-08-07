package modist.antlrdemo;

import modist.antlrdemo.frontend.ast.AstBuilder;
import modist.antlrdemo.frontend.grammar.MxLexer;
import modist.antlrdemo.frontend.grammar.MxParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.io.InputStream;

public class Compiler {

    public static void main(String[] args) throws IOException {
        InputStream input = System.in;
        MxLexer lexer = new MxLexer(CharStreams.fromStream(input));
        MxParser parser = new MxParser(new CommonTokenStream(lexer));
        AstBuilder astBuilder = new AstBuilder();
    }
}
