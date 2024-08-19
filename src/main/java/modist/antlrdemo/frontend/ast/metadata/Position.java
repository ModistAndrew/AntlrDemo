package modist.antlrdemo.frontend.ast.metadata;

public record Position(int line, int column) {
    // used when the position is built-in
    public static final Position BUILTIN = new Position(-1, 0);
    // used when the position is unknown
    public static final Position UNKNOWN = new Position(-2, 0);

    @Override
    public String toString() {
        return line >= 0 ? String.format("%d:%d", line, column) : line == -1 ? "builtin" : "unknown";
    }
}
