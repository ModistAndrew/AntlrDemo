package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.syntax.Position;

public interface Symbol {
    Position position();

    record Class(Position position) implements Symbol {
    }

    record Variable(Position position, TypeInfo type) implements Symbol {
    }

    record Function(Position position) implements Symbol {
    }
}
