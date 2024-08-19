package modist.antlrdemo.frontend.ir.metadata;

import modist.antlrdemo.frontend.semantic.Type;

public sealed interface Variable {

    record Register(String name, boolean isGlobal, Type type) implements Variable {
    }

    record Int(int value) implements Variable {
    }

    record Bool(boolean value) implements Variable {
    }

    enum Null implements Variable {
        INSTANCE
    }
}