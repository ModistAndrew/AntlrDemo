package modist.antlrdemo.optimize;

import modist.antlrdemo.frontend.ir.metadata.IrRegister;
import modist.antlrdemo.frontend.ir.node.BlockIr;
import modist.antlrdemo.frontend.ir.node.FunctionIr;
import modist.antlrdemo.frontend.ir.node.InstructionIr;
import modist.antlrdemo.frontend.ir.node.ProgramIr;

import java.util.*;

public class RegAlloc {
    private FunctionIr function;
    private final Map<IrRegister, Integer> colorMap = new HashMap<>();
    private BlockIr block;

    public void visitProgram(ProgramIr program) {
        program.functions.forEach(function -> {
            this.function = function;
            visitFunction();
        });
    }

    private void visitFunction() {
        this.colorMap.clear();
        this.block = function.getEntry();
        color();
        function.colorMap = new HashMap<>(colorMap);
        function.colorCount = colorMap.isEmpty() ? 0 : Collections.max(colorMap.values()) + 1;
    }

    private void color() {
        Set<Integer> inUseColors = new HashSet<>();
        block.liveIn.forEach(register -> {
            if (colorMap.containsKey(register)) { // all liveIn registers must be in colorMap or defined in the current block
                inUseColors.add(colorMap.get(register));
            }
        });
        block.instructions.forEach(instruction -> {
            // phi instruction is not considered as its uses is not added into liveIn
            if (!(instruction instanceof InstructionIr.Phi)) {
                instruction.uses().forEach(operand -> {
                    if (operand.asConcrete() instanceof IrRegister register
                            && !block.instructionLiveOut.get(instruction).contains(register)) {
                        // defined var must in colorMap (except for phi)
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
