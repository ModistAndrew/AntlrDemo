package modist.antlrdemo.optimize;

import modist.antlrdemo.frontend.ir.metadata.IrOperand;
import modist.antlrdemo.frontend.ir.metadata.IrRegister;
import modist.antlrdemo.frontend.ir.node.BlockIr;
import modist.antlrdemo.frontend.ir.node.FunctionIr;
import modist.antlrdemo.frontend.ir.node.InstructionIr;
import modist.antlrdemo.frontend.ir.node.ProgramIr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LiveAnalysis {
    private FunctionIr function;
    private final Map<IrRegister, Set<InstructionIr>> defUses = new HashMap<>();
    private final Map<InstructionIr, Set<InstructionIr>> useDefs = new HashMap<>();
    private final Set<InstructionIr> useful = new HashSet<>();
    private BlockIr block;
    private int index;
    private IrRegister currentRegister;

    public void visitProgram(ProgramIr program) {
        program.functions.forEach(function -> {
            this.function = function;
            visitFunction();
        });
    }

    private void visitFunction() {
        dce();
        function.body.forEach(block -> block.instructions.forEach(instruction -> block.instructionLiveOut.put(instruction, new HashSet<>())));
        function.body.forEach(block -> {
            for (int i = 0; i < block.instructions.size(); i++) {
                InstructionIr instruction = block.instructions.get(i);
                switch (instruction) {
                    case InstructionIr.Phi phi -> {
                        for (int j = 0; j < phi.labels().size(); j++) {
                            if (phi.values().get(j) instanceof IrRegister register) {
                                BlockIr predecessor = function.blockMap.get(phi.labels().get(j));
                                this.block = predecessor;
                                this.index = predecessor.instructions.size() - 1;
                                this.currentRegister = register;
                                // add phi value to predecessor's live out but not current block's live in
                                spreadUse();
                            }
                        }
                    }
                    default -> {
                        for (IrOperand operand : instruction.uses()) {
                            if (operand.asConcrete() instanceof IrRegister register) {
                                this.block = block;
                                this.index = i - 1; // specially, if -1, then spread to predecessors
                                this.currentRegister = register;
                                spreadUse();
                            }
                        }
                    }
                }
            }
        });
    }

    private void dce() {
        defUses.clear();
        useDefs.clear();
        useful.clear();
        function.body.forEach(block -> block.instructions.forEach(defInst -> {
            if (defInst.def() != null) {
                defUses.put(defInst.def(), new HashSet<>());
            }
        }));
        function.body.forEach(block -> block.instructions.forEach(useInst -> {
            useDefs.put(useInst, new HashSet<>());
            useInst.uses().forEach(operand -> {
                if (operand.asConcrete() instanceof IrRegister register) {
                    defUses.get(register).add(useInst);
                }
            });
        }));
        function.body.forEach(block -> block.instructions.forEach(defInst -> {
            if (defInst.def() != null) {
                defUses.get(defInst.def()).forEach(use -> useDefs.get(use).add(defInst));
            }
        }));
        function.body.forEach(block -> block.instructions.forEach(instruction -> {
            if (instruction instanceof InstructionIr.Effect) {
                dfs(instruction);
            }
        }));
        function.body.forEach(block -> block.instructions.removeIf(instruction -> !useful.contains(instruction)));
        function.body.forEach(block -> block.phiMap.values().removeIf(instruction -> !useful.contains(instruction)));
    }

    private void dfs(InstructionIr instruction) {
        if (!useful.add(instruction)) {
            return;
        }
        useDefs.get(instruction).forEach(this::dfs);
    }

    private void spreadUse() {
        for (; index >= 0; index--) {
            InstructionIr instruction = block.instructions.get(index);
            if (!block.instructionLiveOut.get(instruction).add(currentRegister)) {
                return; // already spread
            }
            if (currentRegister.equals(instruction.def())) {
                return; // reach a definition
            }
        }
        block.liveIn.add(currentRegister);
        for (BlockIr predecessor : block.predecessors) {
            this.block = predecessor;
            this.index = predecessor.instructions.size() - 1;
            spreadUse();
        }
    }
}
