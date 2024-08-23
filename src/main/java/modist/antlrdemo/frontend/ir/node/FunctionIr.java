package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.IrNamer;
import modist.antlrdemo.frontend.ir.metadata.IrType;
import modist.antlrdemo.frontend.ir.metadata.Register;

import java.util.ArrayList;
import java.util.List;

public final class FunctionIr implements Ir {
    public final String name;
    public final IrType returnType;
    public final List<Register> parameters;
    public final List<IrType> parameterTypes;
    public final List<BlockIr> body = new ArrayList<>();

    private FunctionIr(String name, IrType returnType, List<Register> parameters, List<IrType> parameterTypes) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.parameterTypes = parameterTypes;
    }

    // a builder which can build multiple instances of FunctionIr
    public static class Builder {
        private FunctionIr current;
        private final BlockIr.Builder currentBlock = new BlockIr.Builder();
        public IrNamer irNamer;

        // (begin -> add* -> (newBlock -> add* ->)* build)*
        public void begin(String name, IrType returnType, List<Register> parameters, List<IrType> parameterTypes) {
            current = new FunctionIr(name, returnType, parameters, parameterTypes);
            currentBlock.begin(IrNamer.FUNCTION_ENTRY);
            irNamer = new IrNamer();
        }

        public void add(InstructionIr instruction) {
            currentBlock.add(instruction);
        }

        public Register add(InstructionIr.Result instruction) {
            currentBlock.add(instruction);
            return instruction.result();
        }

        public void newBlock(InstructionIr.End end, String name) {
            current.body.add(currentBlock.build(end));
            currentBlock.begin(name);
        }

        public String currentLabel() {
            return currentBlock.currentLabel();
        }

        public FunctionIr build() {
            current.body.add(currentBlock.build(new InstructionIr.Ret(current.returnType, current.returnType.defaultValue)));
            return current;
        }

        public Register createTemporary() {
            return new Register(irNamer.temporaryVariable());
        }
    }
}
