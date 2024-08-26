package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.metadata.IrOperator;
import modist.antlrdemo.frontend.ir.metadata.IrType;
import modist.antlrdemo.frontend.ir.metadata.Operand;
import modist.antlrdemo.frontend.ir.metadata.Register;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public sealed interface InstructionIr extends Ir {
    sealed interface Result extends InstructionIr {
        Register result();
    }

    sealed interface End extends InstructionIr {
    }

    record Bin(Register result, IrOperator operator, IrType type, Operand left,
               Operand right) implements Result {
    }

    record Icmp(Register result, IrOperator operator, IrType type, Operand left,
                Operand right) implements Result {
    }

    record Br(Operand condition, String trueLabel, String falseLabel) implements End {
    }

    record Jump(String label) implements End {
    }

    record Ret(IrType type, @Nullable Operand value) implements End {
        public Ret(IrType type) {
            this(type, type.defaultValue);
        }
    }

    record Alloca(Register result, IrType type) implements Result {
    }

    record Load(Register result, IrType type, Register pointer) implements Result {
    }

    record Store(IrType type, Operand value, Register pointer) implements InstructionIr {
    }

    record MemberVariable(Register result, String type, Register pointer, int memberIndex) implements Result {
    }

    record Subscript(Register result, IrType type, Register pointer, Operand index) implements Result {
    }

    record Call(@Nullable Register result, IrType type, String function, List<IrType> argumentTypes,
                List<Operand> arguments) implements Result {
    }

    // used in builtin global functions
    record CallVarargs(@Nullable Register result, IrType type, String function, List<IrType> functionArgumentTypes,
                       List<IrType> argumentTypes, List<Operand> arguments) implements Result {
    }

    record Phi(Register result, IrType type, List<Operand> values, List<String> labels) implements Result {
        public Phi(Register result, IrType type, Operand value1, String label1, Operand value2, String label2) {
            this(result, type, List.of(value1, value2), List.of(label1, label2));
        }
    }
}
