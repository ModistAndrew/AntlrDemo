package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.NamingUtil;
import modist.antlrdemo.frontend.ir.metadata.IrType;
import modist.antlrdemo.frontend.semantic.SymbolRenamer;

import java.util.ArrayList;
import java.util.List;

public final class FunctionIr extends DefinitionIr {
    public final IrType returnType;
    public final List<IrType> parameters;
    public final List<BlockIr> body = new ArrayList<>();

    private FunctionIr(String name, IrType returnType, List<IrType> parameters) {
        super(name);
        this.returnType = returnType;
        this.parameters = parameters;
    }

    // a builder which can build multiple instances of FunctionIr
    public static class Builder {
        private FunctionIr current;
        private final BlockIr.Builder currentBlock = new BlockIr.Builder();

        // begin -> add* -> (newBlock -> add* ->)* build
        public void begin(String name, IrType returnType, List<IrType> parameters) {
            current = new FunctionIr(name, returnType, parameters);
            currentBlock.begin(NamingUtil.FUNCTION_ENTRY);
        }

        public void add(InstructionIr instruction) {
            currentBlock.add(instruction);
        }

        public void newBlock(String name) {
            current.body.add(currentBlock.build());
            currentBlock.begin(name);
        }

        public FunctionIr build() {
            add(new InstructionIr.Ret(current.returnType));
            current.body.add(currentBlock.build());
            return current;
        }
    }
}
