package modist.antlrdemo.frontend.ast;

import modist.antlrdemo.frontend.ast.node.ExpressionNode;
import modist.antlrdemo.frontend.ast.node.TypeNameNode;
import modist.antlrdemo.frontend.grammar.MxLexer;
import org.antlr.v4.runtime.Token;
import org.jetbrains.annotations.Nullable;

// extract info from token
public class TokenUtil {
    public static Position getPosition(Token token) {
        return new Position(token.getLine(), token.getCharPositionInLine());
    }

    public static ExpressionNode.PostUnary.Operator getPostUnaryOperator(Token token) {
        return ExpressionNode.PostUnary.Operator.valueOf(MxLexer.VOCABULARY.getSymbolicName(token.getType()));
    }

    public static ExpressionNode.PreUnary.Operator getPreUnaryOperator(Token token) {
        return ExpressionNode.PreUnary.Operator.valueOf(MxLexer.VOCABULARY.getSymbolicName(token.getType()));
    }

    public static ExpressionNode.Binary.Operator getBinaryOperator(Token token) {
        return ExpressionNode.Binary.Operator.valueOf(MxLexer.VOCABULARY.getSymbolicName(token.getType()));
    }

    public static ExpressionNode.Assign.Operator getAssignOperator(Token token) {
        return ExpressionNode.Assign.Operator.valueOf(MxLexer.VOCABULARY.getSymbolicName(token.getType()));
    }

    public static TypeNameNode.TypeNameEnum getTypeNameEnum(Token token) {
        return token.getType() == MxLexer.Identifier ? new TypeNameNode.TypeNameEnum.Reference(token.getText())
                : TypeNameNode.TypeNameEnum.Primitive.valueOf(MxLexer.VOCABULARY.getSymbolicName(token.getType()));
    }

    @Nullable
    public static ExpressionNode.Literal.LiteralEnum getLiteralEnum(Token token) {
        return switch (token.getType()) {
            case MxLexer.IntegerLiteral ->
                    new ExpressionNode.Literal.LiteralEnum.Int(Integer.parseInt(token.getText()));
            case MxLexer.StringLiteral -> new ExpressionNode.Literal.LiteralEnum.Str(unesacpeString(token));
            case MxLexer.BooleanLiteral ->
                    new ExpressionNode.Literal.LiteralEnum.Bool(Boolean.parseBoolean(token.getText()));
            case MxLexer.NULL -> null;
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
