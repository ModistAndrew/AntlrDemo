package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.Position;

public abstract class CompileException extends RuntimeException {

    protected CompileException(String message, Position position) {
        super(String.format("%s: %s", position, message));
    }
}