package modist.antlrdemo.frontend.semantic.error;

import modist.antlrdemo.frontend.ast.metadata.Position;

// TODO: store more information about compiling status
public class PositionRecord {
    private static Position currentPosition = Position.UNKNOWN;

    public static void set(Position position) {
        currentPosition = position;
    }

    public static Position get() {
        return currentPosition;
    }
}
