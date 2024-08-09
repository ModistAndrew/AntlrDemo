package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.ast.Position;
import org.antlr.v4.runtime.*;

public class FastFailErrorListener extends BaseErrorListener {
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        throw new SyntaxException(msg, new Position(line, charPositionInLine));
    }
}