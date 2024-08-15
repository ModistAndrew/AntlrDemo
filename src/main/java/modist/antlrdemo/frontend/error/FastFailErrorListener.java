package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.metadata.Position;
import org.antlr.v4.runtime.*;

public class FastFailErrorListener extends BaseErrorListener {
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        PositionRecorder.set(new Position(line, charPositionInLine));
        throw new InvalidIdentifierException(msg);
    }
}