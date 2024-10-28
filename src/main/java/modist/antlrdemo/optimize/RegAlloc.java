package modist.antlrdemo.optimize;

import modist.antlrdemo.frontend.ir.metadata.IrRegister;
import modist.antlrdemo.frontend.ir.node.BlockIr;
import modist.antlrdemo.frontend.ir.node.FunctionIr;
import modist.antlrdemo.frontend.ir.node.InstructionIr;
import modist.antlrdemo.frontend.ir.node.ProgramIr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RegAlloc {
    private FunctionIr function;
    private final Set<Integer> inUseColors = new HashSet<>();
    private final Map<IrRegister, Integer> colorMap = new HashMap<>();
    private BlockIr block;

    public void visitProgram(ProgramIr program) {
        program.functions.forEach(function -> {
            this.function = function;
            visitFunction();
        });
    }

    private void visitFunction() {
        this.inUseColors.clear();
        this.colorMap.clear();
        this.block = function.getEntry();
        color();
        function.colorMap = new HashMap<>(colorMap);
    }

    private void color() {
        block.liveIn.forEach(register -> {
            if (colorMap.containsKey(register)) {
                inUseColors.add(colorMap.get(register));
            }
        });
        block.instructions.forEach(instruction -> {
            // phi instruction is not considered as its uses is not added into liveIn
            if (!(instruction instanceof InstructionIr.Phi)) {
                instruction.uses().forEach(operand -> {
                    if (operand.asConcrete() instanceof IrRegister register
                            && !block.instructionLiveOut.get(instruction).contains(register)) {
                        // must in colorMap
                        inUseColors.remove(colorMap.get(register));
                    }
                });
            }
            if (instruction.def() != null) {
                int color = 0;
                while (!inUseColors.add(color)) {
                    color++;
                }
                colorMap.put(instruction.def(), color);
            }
        });
        for (BlockIr children : block.dominatorTreeChildren) {
            this.block = children;
            color();
        }
    }
}
