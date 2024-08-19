package modist.antlrdemo.frontend.ir.metadata;

import modist.antlrdemo.frontend.semantic.Type;

public sealed interface Var {

    record Register(String name, boolean isGlobal, Type type) implements Var {
    }

    record Int(int value) implements Var {
    }

    record Bool(boolean value) implements Var {
    }

    enum Null implements Var {
        INSTANCE
    }
}