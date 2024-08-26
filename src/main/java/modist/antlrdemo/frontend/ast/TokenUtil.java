package modist.antlrdemo.frontend.ast;

import modist.antlrdemo.frontend.ast.metadata.Constant;
import modist.antlrdemo.frontend.ast.metadata.Operator;
import modist.antlrdemo.frontend.ast.metadata.Position;
import modist.antlrdemo.frontend.grammar.MxLexer;
import org.antlr.v4.runtime.Token;

// extract info from token
public class TokenUtil {
    public static String getLiteralName(int tokenType) {
        String str = MxLexer.VOCABULARY.getLiteralName(tokenType);
        return str.substring(1, str.length() - 1); // it's strange that the literal name is surrounded by single quotes
    }

    public static Position getPosition(Token token) {
        return new Position(token.getLine(), token.getCharPositionInLine());
    }

    public static Operator getOperator(Token token) {
        return Operator.valueOf(MxLexer.VOCABULARY.getSymbolicName(token.getType()));
    }

    public static Constant getConstant(Token token) {
        return switch (token.getType()) {
            case MxLexer.IntegerLiteral -> new Constant.Int(Integer.parseInt(token.getText()));
            case MxLexer.StringLiteral -> new Constant.Str(unesacpeString(token));
            case MxLexer.BooleanLiteral -> new Constant.Bool(Boolean.parseBoolean(token.getText()));
            case MxLexer.NULL -> Constant.Null.INSTANCE;
            default -> throw new IllegalArgumentException();
        };
    }

    // unescape string and remove quotes
    public static String unesacpeString(Token token) {
        String text = switch (token.getType()) {
            case MxLexer.StringLiteral, MxLexer.FormatStringMiddle, MxLexer.FormatStringEnd ->
                    token.getText().substring(1, token.getText().length() - 1);
            case MxLexer.FormatStringAtom, MxLexer.FormatStringBegin ->
                    token.getText().substring(2, token.getText().length() - 1);
            default -> throw new IllegalArgumentException();
        };
        return switch (token.getType()) {
            case MxLexer.StringLiteral -> text.replace("\\n", "\n").replace("\\\"", "\"").replace("\\\\", "\\");
            case MxLexer.FormatStringBegin, MxLexer.FormatStringMiddle, MxLexer.FormatStringEnd,
                 MxLexer.FormatStringAtom ->
                    text.replace("$$", "$").replace("\\n", "\n").replace("\\\"", "\"").replace("\\\\", "\\");
            default -> throw new IllegalArgumentException();
        };
    }
}
