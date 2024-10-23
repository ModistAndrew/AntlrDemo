package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.metadata.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public sealed interface InstructionIr extends Ir {
    sealed interface Result extends InstructionIr {
        IrRegister result();
    }

    sealed interface End extends InstructionIr {
    }

    record Bin(IrRegister result, IrOperator operator, IrType type, IrOperand left,
               IrOperand right) implements Result {
    }

    record Icmp(IrRegister result, IrOperator operator, IrType type, IrOperand left,
                IrOperand right) implements Result {
    }

    // nearTrue to avoid beqz and bnez immediate out of range
    record Br(IrOperand condition, String trueLabel, String falseLabel, boolean nearTrue) implements End {
    }

    record Jump(String label) implements End {
    }

    record Ret(IrType type, @Nullable IrOperand value) implements End {
        public Ret(IrType type) {
            this(type, type.defaultValue);
        }
    }

    record Load(IrRegister result, IrType type, IrRegister pointer) implements Result {
    }

    record Store(IrType type, IrOperand value, IrRegister pointer) implements InstructionIr {
    }

    record MemberVariable(IrRegister result, String type, IrDynamic pointer, int memberIndex) implements Result {
    }

    record Subscript(IrRegister result, IrType type, IrDynamic pointer, IrOperand index) implements Result {
    }

    sealed interface FunctionCall extends Result {
        IrType type();

        String function();

        List<IrType> argumentTypes();

        List<IrOperand> arguments();
    }

    record Call(@Nullable IrRegister result, IrType type, String function, List<IrType> argumentTypes,
                List<IrOperand> arguments) implements FunctionCall {
    }

    // used in builtin global functions
    record CallVarargs(@Nullable IrRegister result, IrType type, String function, List<IrType> functionArgumentTypes,
                       List<IrType> argumentTypes, List<IrOperand> arguments) implements FunctionCall {
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
    }

    record Mv(IrRegister result, IrOperand value) implements Result {
    }
}
