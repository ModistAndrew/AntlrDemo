package modist.antlrdemo.optimize;

import modist.antlrdemo.frontend.ir.metadata.IrOperand;
import modist.antlrdemo.frontend.ir.metadata.IrRegister;
import modist.antlrdemo.frontend.ir.node.BlockIr;
import modist.antlrdemo.frontend.ir.node.FunctionIr;
import modist.antlrdemo.frontend.ir.node.InstructionIr;
import modist.antlrdemo.frontend.ir.node.ProgramIr;

import java.util.HashSet;

public class LiveAnalysis {
    private FunctionIr function;
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

    private void spreadUse() {
        for (; index >= 0; index--) {
            InstructionIr instruction = block.instructions.get(index);
            if (!block.instructionLiveOut.get(instruction).add(currentRegister)) {
                return; // already spread
            }
            if (instruction.defs().contains(currentRegister)) {
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
