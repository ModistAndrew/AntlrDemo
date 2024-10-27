package modist.antlrdemo.optimize;

import modist.antlrdemo.frontend.ir.node.BlockIr;
import modist.antlrdemo.frontend.ir.node.FunctionIr;
import modist.antlrdemo.frontend.ir.node.InstructionIr;
import modist.antlrdemo.frontend.ir.node.ProgramIr;

public class ControlFlowGraphBuilder {
    private FunctionIr function;

    public void visitProgram(ProgramIr program) {
        program.functions.forEach(function -> {
            this.function = function;
            visitFunction();
        });
    }

    private void visitFunction() {
        function.body.forEach(block -> function.blockMap.put(block.label, block));
        function.body.forEach(this::visitBlock);
        bfs();
    }

    private void visitBlock(BlockIr block) {
        switch ((InstructionIr.End) block.instructions.getLast()) {
            case InstructionIr.Ret ignored -> {
            }
            case InstructionIr.Jump jump -> addEdge(block, jump.label());
            case InstructionIr.Br br -> {
                addEdge(block, br.trueLabel());
                addEdge(block, br.falseLabel());
            }
        }
    }

    private void addEdge(BlockIr from, String toLabel) {
        BlockIr to = function.blockMap.get(toLabel);
        from.successors.add(to);
        to.predecessors.add(from);
    }

    private void bfs() {
        BlockIr entry = function.getEntry();
        entry.bfsVisited = true;
        function.bfsOrder.add(entry);
        int head = 0;
        while (head < function.bfsOrder.size()) {
            function.bfsOrder.get(head).successors.stream()
                    .filter(successor -> !successor.bfsVisited)
                    .forEach(successor -> {
                        successor.bfsVisited = true;
                        function.bfsOrder.add(successor);
                    });
            head++;
        }
        // dead code elimination
        function.body.removeIf(block -> !block.bfsVisited);
        function.blockMap.values().removeIf(block -> !block.bfsVisited);
        function.body.forEach(block -> {
            block.successors.removeIf(successor -> !successor.bfsVisited);
            block.predecessors.removeIf(predecessor -> !predecessor.bfsVisited);
        });
    }
}
