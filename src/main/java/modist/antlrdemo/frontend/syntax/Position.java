package modist.antlrdemo.frontend.syntax;

public record Position(int line, int column) {
    public static final Position BUILTIN = new Position(-1, 0);

    @Override
    public String toString() {
        return line < 0 ? "builtin" : line + ":" + column;
    }
}
