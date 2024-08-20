package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ast.metadata.Operator;
import modist.antlrdemo.frontend.ir.metadata.Register;
import modist.antlrdemo.frontend.ir.metadata.Variable;
import modist.antlrdemo.frontend.semantic.Type;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public sealed interface InstructionIr extends Ir {
    record Bin(Operator operator, Variable left, Variable right, Register result) implements InstructionIr {
    }

    record Br(@Nullable Variable condition, String trueLabel, @Nullable String falseLabel) implements InstructionIr {
    }

    record Ret(Type type, @Nullable Variable value) implements InstructionIr {
    }

    record Alloc(Register result) implements InstructionIr {
    }

    record Load(Register pointer, Register result) implements InstructionIr {
    }

    record Store(Variable value, Register pointer) implements InstructionIr {
    }

    record Member(Register pointer, int index, Register result) implements InstructionIr {
    }

    record Subscript(Register pointer, int index, Register result) implements InstructionIr {
    }

    record Call(String function, List<Type> argumentTypes, List<Variable> arguments,
                Register result) implements InstructionIr {
    }
}
