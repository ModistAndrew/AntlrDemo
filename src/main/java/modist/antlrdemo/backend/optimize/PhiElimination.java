package modist.antlrdemo.backend.optimize;

import modist.antlrdemo.frontend.ir.metadata.IrRegister;
import modist.antlrdemo.frontend.ir.node.BlockIr;
import modist.antlrdemo.frontend.ir.node.FunctionIr;
import modist.antlrdemo.frontend.ir.node.InstructionIr;
import modist.antlrdemo.frontend.ir.node.ProgramIr;

import java.util.stream.IntStream;

// assume that phi is executed parallel (in fact, not the situation with clang, so there may be some bugs in ir_test)
// we use temp to store the result of phi to avoid the situation that the result of phi is used in the same block
// TODO: optimize the code
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
            block.mvPhis.forEach(block.instructions::addFirst);
            block.instructions.removeLast();
            block.instructions.addAll(block.mvTemps);
            block.instructions.add(block.end);
        });
    }

    private void visitBlock(BlockIr block) {
        while (!block.instructions.isEmpty() && block.instructions.getFirst() instanceof InstructionIr.Phi phi) {
            IrRegister result = phi.result();
            IrRegister temp = new IrRegister(result.name() + ".mv");
            block.instructions.removeFirst();
            block.mvPhis.add(new InstructionIr.Mv(result, temp));
            IntStream
                    .range(0, phi.labels().size())
                    .filter(i -> phi.values().get(i) != null)
                    .forEach(i -> {
                        BlockIr previous = function.blockMap.get(phi.labels().get(i));
                        previous.mvTemps.add(new InstructionIr.Mv(temp, phi.values().get(i)));
                    });
        }
    }
}
