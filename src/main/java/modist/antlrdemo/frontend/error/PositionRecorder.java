package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.metadata.Position;

public class PositionRecorder {
    private static Position CURRENT_POSITION = Position.BUILTIN;

    public static void set(Position position) {
        CURRENT_POSITION = position;
    }

    public static Position get() {
        return CURRENT_POSITION;
    }
}
