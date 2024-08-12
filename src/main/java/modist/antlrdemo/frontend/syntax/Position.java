package modist.antlrdemo.frontend.syntax;

public record Position(int line, int column) {
    public Position() {
        this(-1, 0);
    }

    @Override
    public String toString() {
        return line < 0 ? "builtin" : line + ":" + column;
    }
}
