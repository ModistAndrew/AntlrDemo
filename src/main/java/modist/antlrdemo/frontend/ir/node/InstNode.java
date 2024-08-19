package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ast.metadata.Operator;
import modist.antlrdemo.frontend.ir.metadata.Var;
import modist.antlrdemo.frontend.semantic.Type;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract sealed class InstNode extends IrNode {
    public static final class Bin extends InstNode {
        public Operator op;
        public Var left;
        public Var right;
        public Var.Register result;
    }

    public static final class Br extends InstNode {
        @Nullable
        public Var condition;
        public String trueLabel;
        @Nullable
        public String falseLabel;
    }

    public static final class Ret extends InstNode {
        public Type type; // according to function signature
        @Nullable
        public Var value;
    }

    public static final class Alloc extends InstNode {
        public Var.Register result;
    }

    public static final class Load extends InstNode {
        public Var.Register ptr;
        public Var.Register result;
    }

    public static final class Store extends InstNode {
        public Var value;
        public Var.Register ptr;
    }

    public static final class Member extends InstNode {
        public Var.Register ptr; // should be class type
        public int index; // index of member in class
        public Var.Register result;
    }

    public static final class Subscript extends InstNode {
        public Var.Register ptr; // should be array type
        public int index;
        public Var.Register result;
    }

    public static final class Call extends InstNode {
        public String func;
        public List<Type> argTypes;
        public List<Var> args;
        public Var.Register result;
    }
}
