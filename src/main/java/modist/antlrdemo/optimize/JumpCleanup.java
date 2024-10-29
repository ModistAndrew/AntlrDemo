package modist.antlrdemo.optimize;

import modist.antlrdemo.backend.asm.node.BlockAsm;
import modist.antlrdemo.backend.asm.node.FunctionAsm;
import modist.antlrdemo.backend.asm.node.InstructionAsm;
import modist.antlrdemo.backend.asm.node.ProgramAsm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JumpCleanup {
    private FunctionAsm function;
    private Map<String, Integer> lineNumbers;

    public void visitProgram(ProgramAsm program) {
        lineNumbers = new HashMap<>();
        int i = 0;
        for (FunctionAsm function : program.text) {
            for (BlockAsm block : function.blocks) {
                lineNumbers.put(block.label, i);
                i += block.instructions.size();
            }
        }
        program.text.forEach(function -> {
            this.function = function;
            visitFunction();
        });
    }

    private void visitFunction() {
        for (int i = 0; i < function.blocks.size() - 1; i++) {
            BlockAsm block = function.blocks.get(i);
            BlockAsm nextBlock = function.blocks.get(i + 1);
            List<InstructionAsm> instructions = block.instructions;
            if (!instructions.isEmpty() && instructions.getLast() instanceof InstructionAsm.J jump) {
                if (nextBlock.label.equals(jump.label())) {
                    block.instructions.removeLast();
                    continue;
                }
                if (instructions.size() > 1 && Math.abs(lineNumbers.get(nextBlock.label) - lineNumbers.get(jump.label())) < 512) {
                    if (instructions.get(instructions.size() - 2) instanceof InstructionAsm.Beqz beqz) {
                        if (beqz.label().equals(nextBlock.label)) {
                            instructions.removeLast();
                            instructions.removeLast();
                            instructions.add(new InstructionAsm.Bnez(beqz.value(), jump.label()));
                        }
                    } else if (instructions.get(instructions.size() - 2) instanceof InstructionAsm.Bnez beqz) {
                        if (beqz.label().equals(nextBlock.label)) {
                            instructions.removeLast();
                            instructions.removeLast();
                            instructions.add(new InstructionAsm.Beqz(beqz.value(), jump.label()));
                        }
                    }
                }
            }
        }
    }
}
