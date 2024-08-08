package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.ast.metadata.Position;

public class SemanticException extends CompileException {

    public SemanticException(String message, Position position) {
        super(message, position);
    }
}