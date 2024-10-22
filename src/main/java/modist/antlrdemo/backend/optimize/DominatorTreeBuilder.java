package modist.antlrdemo.backend.optimize;

import modist.antlrdemo.frontend.ir.node.BlockIr;
import modist.antlrdemo.frontend.ir.node.FunctionIr;
import modist.antlrdemo.frontend.ir.node.ProgramIr;

import java.util.HashSet;
import java.util.Set;

public class DominatorTreeBuilder {
    private FunctionIr function;

    public void visitProgram(ProgramIr program) {
        program.functions.forEach(function -> {
            this.function = function;
            visitFunction();
        });
    }

    private void visitFunction() {
        bfs();
        getDominators();
        buildDominatorTree();
        getDominatorFrontiers();
    }

    private void bfs() {
        BlockIr entry = function.body.getFirst();
        entry.bfsVisited = true;
        function.bfsOrder.add(entry);
        int head = 0;
        while (head < function.body.size()) {
            function.bfsOrder.get(head).successors.stream()
                    .filter(successor -> !successor.bfsVisited)
                    .forEach(successor -> {
                        successor.bfsVisited = true;
                        function.bfsOrder.add(successor);
                    });
            head++;
        }
    }

    private void getDominators() {
        function.body.forEach(block -> block.dominators = new HashSet<>(function.body));
        boolean changed = true;
        while (changed) {
            changed = false;
            for (BlockIr block : function.bfsOrder) {
                Set<BlockIr> newDominators;
                if (block.predecessors.isEmpty()) {
                    newDominators = Set.of(block);
                } else {
                    newDominators = new HashSet<>(block.predecessors.getFirst().dominators);
                    block.predecessors.forEach(predecessor -> newDominators.retainAll(predecessor.dominators));
                    newDominators.add(block);
                }
                if (!block.dominators.equals(newDominators)) {
                    block.dominators = newDominators;
                    changed = true;
                }
            }
        }
    }

    private void buildDominatorTree() {
        function.body.forEach(block -> {
            BlockIr immediateDominator = block.dominators.stream()
                    .filter(dominator -> dominator.dominators.size() + 1 == block.dominators.size())
                    .findAny().orElse(null);
            block.immediateDominator = immediateDominator;
            if (immediateDominator != null) {
                immediateDominator.dominatorTreeChildren.add(block);
            }
        });
    }

    private void getDominatorFrontiers() {
        function.body.forEach(block -> {
            Set<BlockIr> currentStrictDominators = new HashSet<>(block.dominators);
            currentStrictDominators.remove(block);
            block.predecessors.forEach(predecessor -> predecessor.dominators.stream()
                    .filter(candidate -> !currentStrictDominators.contains(candidate))
                    .forEach(candidate -> candidate.dominanceFrontiers.add(block)));
        });
    }
}
