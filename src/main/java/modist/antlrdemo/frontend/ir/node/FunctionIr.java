package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.semantic.SymbolRenamer;
import modist.antlrdemo.frontend.semantic.Type;

import java.util.ArrayList;
import java.util.List;

public final class FunctionIr extends DefinitionIr {
    public final Type returnType;
    public final List<Type> parameters;
    public final List<BlockIr> body = new ArrayList<>();

    private FunctionIr(String name, Type returnType, List<Type> parameters) {
        super(name);
        this.returnType = returnType;
        this.parameters = parameters;
    }

    // a builder which can build multiple instances of FunctionIr
    public static class Builder {
        private FunctionIr current;
        private final BlockIr.Builder currentBlock = new BlockIr.Builder();

        // begin -> add* -> (newBlock -> add* ->)* build
        public void begin(String name, Type returnType, List<Type> parameters) {
            current = new FunctionIr(name, returnType, parameters);
            currentBlock.begin(SymbolRenamer.FUNCTION_ENTRY);
        }

        public void add(InstructionIr instruction) {
            currentBlock.add(instruction);
        }

        public void newBlock(String name) {
            current.body.add(currentBlock.build());
            currentBlock.begin(name);
        }

        public FunctionIr build() {
            // TODO: add(new InstructionIr.Ret());
            current.body.add(currentBlock.build());
            return current;
        }
    }
}
