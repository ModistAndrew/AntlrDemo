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
    }

    private void visitBlock(BlockIr block) {
        switch (block.end) {
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
}
