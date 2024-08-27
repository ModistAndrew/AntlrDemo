package modist.antlrdemo.backend;

import modist.antlrdemo.backend.metadata.Register;
import modist.antlrdemo.frontend.ir.metadata.IrOperand;
import modist.antlrdemo.frontend.ir.metadata.IrRegister;

import java.util.HashMap;
import java.util.Map;

public class AsmBuilder {
    private final Map<IrRegister, String> globalVariables = new HashMap<>();
    private FunctionBuilder currentFunction;

    // may load from global variables, local variables or constants
    private Register load(IrOperand operand) {
        return null;
    }

    // may store to global variables or local variables. alloc is not dealt with here
    private void store(IrRegister register, Register value) {
    }

}
