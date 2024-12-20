package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.IrNamer;
import modist.antlrdemo.frontend.ir.metadata.IrGlobal;
import modist.antlrdemo.frontend.ir.metadata.IrType;
import modist.antlrdemo.frontend.ir.metadata.IrRegister;
import modist.antlrdemo.frontend.ir.metadata.VariableReference;

import java.util.*;

public final class FunctionIr implements Ir {
    public final String name;
    public final IrType returnType;
    public final List<IrRegister> parameters;
    public final List<IrType> parameterTypes;
    public final List<BlockIr> body = new ArrayList<>();
    // for ControlFlowGraphBuilder
    public final Map<String, BlockIr> blockMap = new HashMap<>();
    public final List<BlockIr> bfsOrder = new ArrayList<>();
    // for RegAlloc
    public final Map<IrRegister, Integer> registerMap = new HashMap<>();
    public int persistentRegisterCount;
    public Set<InstructionIr.Param> usefulParams = new HashSet<>();
    public final Set<IrRegister> persistentRegisters = new HashSet<>();
    public final HashMap<IrRegister, IrGlobal> globalLoadRegisterMap = new HashMap<>();

    private FunctionIr(String name, IrType returnType, List<IrRegister> parameters, List<IrType> parameterTypes) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.parameterTypes = parameterTypes;
    }

    public BlockIr getEntry() {
        return body.getFirst();
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

        public void newBlock(InstructionIr.End end, String name) {
            current.body.add(currentBlock.build(end));
            currentBlock.begin(name);
        }

        public FunctionIr build() {
            current.body.add(currentBlock.build(new InstructionIr.Ret(current.returnType)));
            BlockIr entry = current.body.getFirst();
            for (int i = 0; i < current.parameters.size(); i++) {
                entry.instructions.addFirst(new InstructionIr.Param(current.parameters.get(i), i));
            }
            return current;
        }

        public IrRegister createTemporary(String prefix) {
            return new IrRegister(irNamer.temporaryVariable(prefix));
        }
    }
}
