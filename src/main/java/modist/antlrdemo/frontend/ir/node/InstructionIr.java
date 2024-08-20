package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.metadata.IrOperator;
import modist.antlrdemo.frontend.ir.metadata.IrType;
import modist.antlrdemo.frontend.ir.metadata.Register;
import modist.antlrdemo.frontend.ir.metadata.Variable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public sealed interface InstructionIr extends Ir {
    record Bin(IrOperator operator, Variable left, Variable right, Register result) implements InstructionIr {
    }

    record Br(@Nullable Variable condition, String trueLabel, @Nullable String falseLabel) implements InstructionIr {
    }

    record Ret(IrType type, @Nullable Variable value) implements InstructionIr {
        public Ret(IrType type) {
            this(type, type.defaultValue);
        }
    }

    record Alloc(Register result) implements InstructionIr {
    }

    record Load(Register pointer, Register result) implements InstructionIr {
    }

    record Store(Variable value, Register pointer) implements InstructionIr {
    }

    record Member(Register pointer, String type, int index, Register result) implements InstructionIr {
    }

    record Subscript(Register pointer, int index, Register result) implements InstructionIr {
    }

    record Call(String function, List<IrType> argumentTypes, List<Variable> arguments,
                Register result) implements InstructionIr {
    }
}
