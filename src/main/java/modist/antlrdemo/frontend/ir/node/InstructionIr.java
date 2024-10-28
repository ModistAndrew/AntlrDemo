package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.metadata.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public sealed interface InstructionIr extends Ir {
    @Nullable
    default IrRegister def() {
        return null;
    }

    List<IrOperand> uses();

    sealed interface Result extends InstructionIr {
        @Nullable
        IrRegister result();

        @Override
        default IrRegister def() {
            return result();
        }
    }

    sealed interface Effect extends InstructionIr {
    }

    sealed interface End extends InstructionIr, Effect {
    }

    record Bin(IrRegister result, IrOperator operator, IrType type, IrOperand left,
               IrOperand right) implements Result {
        @Override
        public List<IrOperand> uses() {
            return List.of(left, right);
        }
    }

    record Icmp(IrRegister result, IrOperator operator, IrType type, IrOperand left,
                IrOperand right) implements Result {
        @Override
        public List<IrOperand> uses() {
            return List.of(left, right);
        }
    }

    // nearTrue to avoid beqz and bnez immediate out of range
    record Br(IrOperand condition, String trueLabel, String falseLabel, boolean nearTrue) implements End {
        @Override
        public List<IrOperand> uses() {
            return List.of(condition);
        }
    }

    record Jump(String label) implements End {
        @Override
        public List<IrOperand> uses() {
            return List.of();
        }
    }

    record Ret(IrType type, @Nullable IrOperand value) implements End, Effect {
        public Ret(IrType type) {
            this(type, type.defaultValue);
        }

        @Override
        public List<IrOperand> uses() {
            return value == null ? List.of() : List.of(value);
        }
    }

    record Load(IrRegister result, IrType type, IrDynamic pointer) implements Result {
        @Override
        public List<IrOperand> uses() {
            return List.of(pointer);
        }
    }

    record Store(IrType type, IrOperand value, IrDynamic pointer) implements InstructionIr, Effect {
        @Override
        public List<IrOperand> uses() {
            return List.of(value, pointer);
        }
    }

    record MemberVariable(IrRegister result, String type, IrDynamic pointer, int memberIndex) implements Result {
        @Override
        public List<IrOperand> uses() {
            return List.of(pointer);
        }
    }

    record Subscript(IrRegister result, IrType type, IrDynamic pointer, IrOperand index) implements Result {
        @Override
        public List<IrOperand> uses() {
            return List.of(pointer, index);
        }
    }

    sealed interface FunctionCall extends Result, Effect {
        IrType type();

        String function();

        List<IrType> argumentTypes();

        List<IrOperand> arguments();
    }

    record Call(@Nullable IrRegister result, IrType type, String function, List<IrType> argumentTypes,
                List<IrOperand> arguments) implements FunctionCall {
        @Override
        public List<IrOperand> uses() {
            return arguments();
        }
    }

    // used in builtin global functions
    record CallVarargs(@Nullable IrRegister result, IrType type, String function, List<IrType> functionArgumentTypes,
                       List<IrType> argumentTypes, List<IrOperand> arguments) implements FunctionCall {
        @Override
        public List<IrOperand> uses() {
            return arguments();
        }
    }

    // elements in values may be null which represents an undefined value
    record Phi(IrRegister result, IrType type, List<IrOperand> values, List<String> labels) implements Result {
        public Phi(IrRegister result, IrType type, IrOperand value1, String label1, IrOperand value2, String label2) {
            this(result, type, List.of(value1, value2), List.of(label1, label2));
        }

        public Phi(IrRegister result, IrType type) {
            this(result, type, new ArrayList<>(), new ArrayList<>());
        }

        public void add(BlockIr block, IrConcrete value) {
            values.add(value);
            labels.add(block.label);
        }

        @Override
        public List<IrOperand> uses() {
            return values;
        }
    }
}
