package modist.antlrdemo.backend.optimize;

import modist.antlrdemo.frontend.ir.node.BlockIr;
import modist.antlrdemo.frontend.ir.node.FunctionIr;
import modist.antlrdemo.frontend.ir.node.InstructionIr;
import modist.antlrdemo.frontend.ir.node.ProgramIr;

import java.util.stream.IntStream;

public class PhiElimination {
    private FunctionIr function;

    public void visitProgram(ProgramIr program) {
        program.functions.forEach(function -> {
            this.function = function;
            visitFunction();
        });
    }

    private void visitFunction() {
        function.body.forEach(this::visitBlock);
        function.body.forEach(block -> {
            block.instructions.removeLast();
            block.instructions.addAll(block.mvs);
            block.instructions.add(block.end);
        });
    }

    private void visitBlock(BlockIr block) {
        while (!block.instructions.isEmpty() && block.instructions.getFirst() instanceof InstructionIr.Phi phi) {
            block.instructions.removeFirst();
            IntStream
                    .range(0, phi.labels().size())
                    .filter(i -> phi.values().get(i) != null)
                    .forEach(i -> {
                        BlockIr previous = function.blockMap.get(phi.labels().get(i));
                        previous.mvs.add(new InstructionIr.Mv(phi.result(), phi.values().get(i)));
                    });
        }
    }
}
