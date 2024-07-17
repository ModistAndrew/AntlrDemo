package modist.antlrdemo;

import modist.antlrdemo.antlrparser.ExprLexer;
import modist.antlrdemo.antlrparser.ExprParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class Main {

    public static void main(String[] args) {
        // Create a new instance of the ExprLexer
        ExprLexer lexer = new ExprLexer(CharStreams.fromString("1 + 2 * 3"));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprParser parser = new ExprParser(tokens);
        System.out.println(parser.expr());
    }
}
