package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.metadata.VariableReference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class BlockIr implements Ir {
    public final String label;
    public final List<InstructionIr> instructions = new ArrayList<>();
    public final List<VariableReference> variableReferences = new ArrayList<>();
    // for ControlFlowGraphBuilder
    public InstructionIr.End end;
    public final List<BlockIr> successors = new ArrayList<>();
    public final List<BlockIr> predecessors = new ArrayList<>();
    // for DominatorTreeBuilder
    public boolean bfsVisited;
    public Set<BlockIr> dominators;
    public BlockIr immediateDominator;
    public final Set<BlockIr> dominatorTreeChildren = new HashSet<>();
    public final Set<BlockIr> dominanceFrontiers = new HashSet<>();

    private BlockIr(String label) {
        this.label = label;
    }

    public static class Builder {
        private BlockIr current;
        private boolean finished;

        public void add(InstructionIr instruction) {
            if (!finished) {
                current.instructions.add(instruction);
            }
            if (instruction instanceof InstructionIr.End end) {
                current.end = end;
                finished = true;
            }
        }

        public void addVariableReference(VariableReference variableReference) {
            if (!finished) {
                current.variableReferences.add(variableReference);
            }
        }

        public BlockIr build(InstructionIr.End end) {
            add(end);
            return current;
        }

        public void begin(String label) {
            current = new BlockIr(label);
            finished = false;
        }

        public String currentLabel() {
            return current.label;
        }
    }
}