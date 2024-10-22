package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.metadata.IrOperator;
import modist.antlrdemo.frontend.ir.metadata.IrType;
import modist.antlrdemo.frontend.ir.metadata.IrOperand;
import modist.antlrdemo.frontend.ir.metadata.IrRegister;
import org.jetbrains.annotations.Nullable;

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
    // TODO: make a simple Br command with only one label? how to calculate the distance?
    record Br(IrOperand condition, String trueLabel, String falseLabel, boolean nearTrue) implements End {
    }

    record Jump(String label) implements End {
    }

    record Ret(IrType type, @Nullable IrOperand value) implements End {
        public Ret(IrType type) {
            this(type, type.defaultValue);
        }
    }

    @Deprecated
    record Alloc(IrRegister result, IrType type) implements Result {
    }

    record Load(IrRegister result, IrType type, IrRegister pointer) implements Result {
    }

    record Store(IrType type, IrOperand value, IrRegister pointer) implements InstructionIr {
    }

    record MemberVariable(IrRegister result, String type, IrRegister pointer, int memberIndex) implements Result {
    }

    record Subscript(IrRegister result, IrType type, IrRegister pointer, IrOperand index) implements Result {
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

    record Phi(IrRegister result, IrType type, List<IrOperand> values, List<String> labels) implements Result {
        public Phi(IrRegister result, IrType type, IrOperand value1, String label1, IrOperand value2, String label2) {
            this(result, type, List.of(value1, value2), List.of(label1, label2));
        }
    }
}
