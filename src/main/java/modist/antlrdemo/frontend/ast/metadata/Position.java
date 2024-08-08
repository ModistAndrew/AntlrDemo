package modist.antlrdemo.frontend.ast.metadata;

import org.antlr.v4.runtime.Token;

public record Position(int line, int column) {
    public Position(Token token) {
        this(token.getLine(), token.getCharPositionInLine());
    }
}
