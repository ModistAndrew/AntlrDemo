package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.ast.metadata.Position;

public class SyntaxException extends CompileException {

    protected SyntaxException(String message, Position position) {
        super(message, position);
    }
}