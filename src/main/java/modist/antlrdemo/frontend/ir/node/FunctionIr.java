package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.IrNamer;
import modist.antlrdemo.frontend.ir.metadata.IrType;
import modist.antlrdemo.frontend.ir.metadata.IrRegister;
import modist.antlrdemo.frontend.ir.metadata.VariableReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class FunctionIr implements Ir {
    public final String name;
    public final IrType returnType;
    public final List<IrRegister> parameters;
    public final List<IrType> parameterTypes;
    public final List<BlockIr> body = new ArrayList<>();
    // for ControlFlowGraphBuilder
    public final HashMap<String, BlockIr> blockMap = new HashMap<>();
    // for DominatorTreeBuilder
    public final List<BlockIr> bfsOrder = new ArrayList<>();

    private FunctionIr(String name, IrType returnType, List<IrRegister> parameters, List<IrType> parameterTypes) {
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
        public void begin(String name, IrType returnType, List<IrRegister> parameters, List<IrType> parameterTypes) {
            current = new FunctionIr(name, returnType, parameters, parameterTypes);
            currentBlock.begin(IrNamer.FUNCTION_ENTRY);
            irNamer = new IrNamer();
        }

        public void add(InstructionIr instruction) {
            currentBlock.add(instruction);
        }

        public void addVariableReference(VariableReference variableReference) {
            currentBlock.addVariableReference(variableReference);
        }

        // returns the label of the previous block
        public String newBlock(InstructionIr.End end, String name) {
            String currentLabel = currentBlock.currentLabel();
            current.body.add(currentBlock.build(end));
            currentBlock.begin(name);
            return currentLabel;
        }

        public FunctionIr build() {
            current.body.add(currentBlock.build(new InstructionIr.Ret(current.returnType)));
            return current;
        }

        public IrRegister createTemporary(String prefix) {
            return new IrRegister(irNamer.temporaryVariable(prefix));
        }
    }
}
