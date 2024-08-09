package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.ast.Position;

public abstract class CompileException extends RuntimeException {

    protected CompileException(String message, Position position) {
        super("Compile Error at " + position + ": " + message);
    }
}