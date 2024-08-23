package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.ast.metadata.Position;

public class PositionRecord {
    private static Position currentPosition = Position.UNKNOWN;

    public static void set(Position position) {
        currentPosition = position;
    }

    public static Position get() {
        return currentPosition;
    }
}
