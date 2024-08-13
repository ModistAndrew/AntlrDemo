package modist.antlrdemo.frontend.syntax;

import modist.antlrdemo.frontend.metadata.LiteralEnum;
import modist.antlrdemo.frontend.metadata.Operator;
import modist.antlrdemo.frontend.metadata.Position;
import modist.antlrdemo.frontend.grammar.MxLexer;
import org.antlr.v4.runtime.Token;

// extract info from token
public class TokenUtil {
    public static Position getPosition(Token token) {
        return new Position(token.getLine(), token.getCharPositionInLine());
    }

    public static Operator getOperator(Token token) {
        return Operator.valueOf(MxLexer.VOCABULARY.getSymbolicName(token.getType()));
    }

    public static LiteralEnum getLiteralEnum(Token token) {
        return switch (token.getType()) {
            case MxLexer.IntegerLiteral ->
                    new LiteralEnum.Int(Integer.parseInt(token.getText()));
            case MxLexer.StringLiteral -> new LiteralEnum.Str(unesacpeString(token));
            case MxLexer.BooleanLiteral ->
                    new LiteralEnum.Bool(Boolean.parseBoolean(token.getText()));
            case MxLexer.NULL -> LiteralEnum.Null.INSTANCE;
            default ->
                    throw new IllegalStateException("Unexpected token type: " + MxLexer.VOCABULARY.getSymbolicName(token.getType()));
        };
    }

    // unescape string and remove quotes
    public static String unesacpeString(Token token) {
        String text = switch (token.getType()) {
            case MxLexer.StringLiteral, MxLexer.FormatStringMiddle, MxLexer.FormatStringEnd ->
                    token.getText().substring(1, token.getText().length() - 1);
            case MxLexer.FormatStringAtom, MxLexer.FormatStringBegin ->
                    token.getText().substring(2, token.getText().length() - 1);
            default -> throw new IllegalStateException("Unexpected value: " + token.getType());
        };
        return switch (token.getType()) {
            case MxLexer.StringLiteral -> text.replace("\\n", "\n").replace("\\\\", "\\").replace("\\\"", "\"");
            case MxLexer.FormatStringBegin, MxLexer.FormatStringMiddle, MxLexer.FormatStringEnd,
                 MxLexer.FormatStringAtom ->
                    text.replace("$$", "$").replace("\\n", "\n").replace("\\\\", "\\").replace("\\\"", "\"");
            default -> throw new IllegalStateException("Unexpected value: " + token.getType());
        };
    }
}
