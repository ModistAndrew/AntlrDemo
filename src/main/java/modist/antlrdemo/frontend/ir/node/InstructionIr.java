package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ast.metadata.Operator;
import modist.antlrdemo.frontend.ir.metadata.Variable;
import modist.antlrdemo.frontend.semantic.Type;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract sealed class InstructionIr extends Ir {
    public static final class Bin extends InstructionIr {
        public Operator operator;
        public Variable left;
        public Variable right;
        public Variable.Register result;
    }

    public static final class Br extends InstructionIr {
        @Nullable
        public Variable condition;
        public String trueLabel;
        @Nullable
        public String falseLabel;
    }

    public static final class Ret extends InstructionIr {
        public Type type; // according to function signature
        @Nullable
        public Variable value;
    }

    public static final class Alloc extends InstructionIr {
        public Variable.Register result;
    }

    public static final class Load extends InstructionIr {
        public Variable.Register pointer;
        public Variable.Register result;
    }

    public static final class Store extends InstructionIr {
        public Variable value;
        public Variable.Register pointer;
    }

    public static final class Member extends InstructionIr {
        public Variable.Register pointer; // should be class type
        public int index; // index of member in class
        public Variable.Register result;
    }

    public static final class Subscript extends InstructionIr {
        public Variable.Register pointer; // should be array type
        public int index;
        public Variable.Register result;
    }

    public static final class Call extends InstructionIr {
        public String function;
        public List<Type> argumentTypes;
        public List<Variable> arguments;
        public Variable.Register result;
    }
}
