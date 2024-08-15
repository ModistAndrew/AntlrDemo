package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.metadata.Position;

import java.util.Stack;

public class PositionRecorder {
    private static final Stack<Position> POSITION_STACK = new Stack<>();

    public static void push(Position position) {
        POSITION_STACK.push(position);
    }

    public static void pop() {
        POSITION_STACK.pop();
    }

    public static Position peek() {
        return POSITION_STACK.isEmpty() ? Position.UNKNOWN : POSITION_STACK.peek();
    }
}
