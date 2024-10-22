package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.metadata.VariableDef;
import modist.antlrdemo.frontend.ir.metadata.VariableReference;
import modist.antlrdemo.frontend.ir.metadata.VariableUse;

import java.util.*;

public final class BlockIr implements Ir {
    public final String label;
    public final Deque<InstructionIr> instructions = new ArrayDeque<>();
    // for Mem2Reg
    public final List<VariableReference> variableReferences = new ArrayList<>();
    public final List<VariableDef> variableDefs = new ArrayList<>();
    public final List<VariableUse> variableUses = new ArrayList<>();
    public final Map<String, InstructionIr.Phi> phiMap = new HashMap<>();
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
                if (instruction instanceof InstructionIr.End end) {
                    current.end = end;
                    finished = true;
                }
            }
        }

        public void addVariableReference(VariableReference variableReference) {
            if (!finished) {
                current.variableReferences.add(variableReference);
                switch (variableReference) {
                    case VariableDef def -> current.variableDefs.add(def);
                    case VariableUse use -> current.variableUses.add(use);
                }
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