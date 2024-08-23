package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.IrStringUtil;
import modist.antlrdemo.frontend.ir.metadata.IrOperator;
import modist.antlrdemo.frontend.ir.metadata.IrType;
import modist.antlrdemo.frontend.ir.metadata.Register;
import modist.antlrdemo.frontend.ir.metadata.Variable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public sealed interface InstructionIr extends Ir {
    sealed interface Result extends InstructionIr {
        Register result();
    }

    sealed interface End extends InstructionIr {
    }

    record Bin(Register result, IrOperator operator, IrType type, Variable left,
               Variable right) implements Result {
        @Override
        public String toString() {
            return String.format("%s = %s %s %s, %s", result, operator, type, left, right);
        }
    }

    record Icmp(Register result, IrOperator operator, IrType type, Variable left,
                Variable right) implements Result {
        @Override
        public String toString() {
            return String.format("%s = icmp %s %s %s, %s", result, operator, type, left, right);
        }
    }

    record Br(Variable condition, String trueLabel, String falseLabel) implements End {
        @Override
        public String toString() {
            return String.format("br %s %s, label %s, label %s", IrType.I1, condition, trueLabel, falseLabel);
        }
    }

    record Jump(String label) implements End {
        @Override
        public String toString() {
            return String.format("br label %s", label);
        }
    }

    record Ret(IrType type, @Nullable Variable value) implements End {
        public Ret(IrType type) {
            this(type, type.defaultValue);
        }

        @Override
        public String toString() {
            return value == null ? String.format("ret %s", type) : String.format("ret %s %s", type, value);
        }
    }

    record Alloca(Register result, IrType type) implements Result {
        @Override
        public String toString() {
            return String.format("%s = alloca %s", result, type);
        }
    }

    record Load(Register result, IrType type, Register pointer) implements Result {
        @Override
        public String toString() {
            return String.format("%s = load %s, %s %s", result, type, IrType.PTR, pointer);
        }
    }

    record Store(IrType type, Variable value, Register pointer) implements InstructionIr {
        @Override
        public String toString() {
            return String.format("store %s %s, %s %s", type, value, IrType.PTR, pointer);
        }
    }

    record MemberVariable(Register result, String type, Register pointer, int memberIndex) implements Result {
        @Override
        public String toString() {
            return String.format("%s = getelementptr %s, %s %s, %s %d, %s %d",
                    result, type, IrType.PTR, pointer, IrType.I32, 0, IrType.I32, memberIndex);
        }
    }

    record Subscript(Register result, IrType type, Register pointer, Variable index) implements Result {
        @Override
        public String toString() {
            return String.format("%s = getelementptr %s, %s %s, %s %s", result, type, IrType.PTR, pointer, IrType.I32, index);
        }
    }

    record Call(@Nullable Register result, IrType type, String function, List<IrType> argumentTypes,
                List<Variable> arguments) implements Result {
        @Override
        public String toString() {
            return result == null ? String.format("call %s %s(%s)", type, function, IrStringUtil.toStringArguments(argumentTypes, arguments)) :
                    String.format("%s = call %s %s(%s)", result, type, function, IrStringUtil.toStringArguments(argumentTypes, arguments));
        }
    }

    // used in builtin global functions
    record CallVarargs(@Nullable Register result, IrType type, String function, List<IrType> functionArgumentTypes,
                       List<IrType> argumentTypes, List<Variable> arguments) implements Result {
        @Override
        public String toString() {
            return result == null ? String.format("call %s (%s) %s(%s)", type, function, IrStringUtil.toStringTypesVarargs(functionArgumentTypes), IrStringUtil.toStringArguments(argumentTypes, arguments)) :
                    String.format("%s = call %s (%s) %s(%s)", result, type, function, IrStringUtil.toStringTypesVarargs(functionArgumentTypes), IrStringUtil.toStringArguments(argumentTypes, arguments));
        }
    }

    record Phi(Register result, IrType type, List<Variable> values, List<String> labels) implements Result {
        public Phi(Register result, IrType type, Variable value1, String label1, Variable value2, String label2) {
            this(result, type, List.of(value1, value2), List.of(label1, label2));
        }

        @Override
        public String toString() {
            return String.format("%s = phi %s %s", result, type, IrStringUtil.toStringPhiPairs(values, labels));
        }
    }
}
