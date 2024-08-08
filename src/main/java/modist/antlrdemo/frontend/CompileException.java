package modist.antlrdemo.frontend;

import modist.antlrdemo.frontend.ast.metadata.Position;

public class CompileException extends RuntimeException {
    public CompileException(Position position, String message) {
        super("Compile Error at " + position + ": " + message);
    }
}