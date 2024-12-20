package modist.antlrdemo.frontend.semantic.error;

import modist.antlrdemo.frontend.ast.metadata.Position;
import org.antlr.v4.runtime.*;

public class FastFailErrorListener extends BaseErrorListener {
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        throw CompileException.withPosition(new InvalidIdentifierException(msg), new Position(line, charPositionInLine));
    }
}