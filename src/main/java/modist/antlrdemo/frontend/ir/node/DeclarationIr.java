package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.semantic.Type;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract sealed class DeclarationIr extends Ir {
    public String name;

    public static final class Class extends DeclarationIr {
        public List<Type> members;
    }

    public static final class Function extends DeclarationIr {
        public Type returnType;
        public List<Type> parameters;
        @Nullable
        public List<BlockIr> body;
    }
}
