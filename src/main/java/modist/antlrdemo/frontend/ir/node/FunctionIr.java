package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.NamingUtil;
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
    // automatically add %this as the first parameter if isMember is true
    public final boolean isMember;

    private FunctionIr(String name, IrType returnType, List<Register> parameters, List<IrType> parameterTypes, boolean isMember) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.parameterTypes = parameterTypes;
        this.isMember = isMember;
    }

    // a builder which can build multiple instances of FunctionIr
    public static class Builder {
        private FunctionIr current;
        private final BlockIr.Builder currentBlock = new BlockIr.Builder();
        public NamingUtil namingUtil;

        // (begin -> add* -> (newBlock -> add* ->)* build)*
        public void begin(String name, IrType returnType, List<Register> parameters, List<IrType> parameterTypes, boolean isMember) {
            current = new FunctionIr(name, returnType, parameters, parameterTypes, isMember);
            currentBlock.begin(NamingUtil.FUNCTION_ENTRY);
            namingUtil = new NamingUtil();
        }

        public void add(InstructionIr instruction) {
            currentBlock.add(instruction);
        }

        public Register add(InstructionIr.Result instruction) {
            currentBlock.add(instruction);
            return instruction.result();
        }

        public void newBlock(String name) {
            current.body.add(currentBlock.build());
            currentBlock.begin(name);
        }

        public FunctionIr build() {
            add(new InstructionIr.Ret(current.returnType, current.returnType.defaultValue));
            current.body.add(currentBlock.build());
            return current;
        }

        public Register createTemporary() {
            return new Register(namingUtil.temporaryVariable());
        }
    }
}
