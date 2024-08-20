package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.semantic.Type;

import java.util.ArrayList;
import java.util.List;

public final class ClassIr extends DefinitionIr {
    public final List<Type> members = new ArrayList<>();

    public ClassIr(String name) {
        super(name);
    }
}
