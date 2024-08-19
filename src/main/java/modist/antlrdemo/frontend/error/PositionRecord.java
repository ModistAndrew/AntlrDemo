package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.ast.metadata.Position;

public class PositionRecord {
    private static Position CURRENT_POSITION = Position.UNKNOWN;

    public static void set(Position position) {
        CURRENT_POSITION = position;
    }

    public static Position get() {
        return CURRENT_POSITION;
    }
}
