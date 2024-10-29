package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.ir.metadata.IrRegister;
import modist.antlrdemo.frontend.ir.metadata.VariableDef;
import modist.antlrdemo.frontend.ir.metadata.VariableReference;
import modist.antlrdemo.frontend.ir.metadata.VariableUse;

import java.util.*;

public final class BlockIr implements Ir {
    public final String label;
    public final List<InstructionIr> instructions = new ArrayList<>();
    // for Mem2Reg
    public final List<VariableReference> variableReferences = new ArrayList<>();
    public final Set<VariableDef> variableDefs = new HashSet<>();
    public final Set<VariableUse> variableUses = new HashSet<>();
    public final Map<String, InstructionIr.Phi> phiMap = new HashMap<>();
    // for ControlFlowGraphBuilder
    public final Set<BlockIr> successors = new HashSet<>();
    public final Set<BlockIr> predecessors = new HashSet<>();
    // for DominatorTreeBuilder
    public boolean bfsVisited;
    public Set<BlockIr> dominators;
    public BlockIr immediateDominator;
    public final Set<BlockIr> dominatorTreeChildren = new HashSet<>();
    public final Set<BlockIr> dominanceFrontiers = new HashSet<>();
    // for LiveAnalysis
    public final Set<IrRegister> liveIn = new HashSet<>();
    public final Map<InstructionIr, Set<IrRegister>> instructionLiveOut = new HashMap<>();

    private BlockIr(String label) {
        this.label = label;
    }

    public static class Builder {
        private BlockIr current;
        private boolean finished;

        public void add(InstructionIr instruction) {
            if (!finished) {
                current.instructions.add(instruction);
                if (instruction instanceof InstructionIr.End) {
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
    }
}