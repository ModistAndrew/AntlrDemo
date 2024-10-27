package modist.antlrdemo.optimize;

import modist.antlrdemo.frontend.ir.metadata.IrRegister;
import modist.antlrdemo.frontend.ir.node.BlockIr;
import modist.antlrdemo.frontend.ir.node.FunctionIr;
import modist.antlrdemo.frontend.ir.node.ProgramIr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RegAlloc {
    private static final int K = 16;
    private FunctionIr function;
    private final Set<IrRegister> spilled = new HashSet<>();
    private final Set<Integer> inUseColors = new HashSet<>();
    private final Map<IrRegister, Integer> colorMap = new HashMap<>();
    private BlockIr block;

    public void visitProgram(ProgramIr program) {
        program.functions.forEach(function -> {
            this.function = function;
            this.spilled.clear();
            this.inUseColors.clear();
            this.colorMap.clear();
            visitFunction();
            function.colorMap = new HashMap<>(colorMap);
        });
    }

    private void visitFunction() {
        spill();
        this.block = function.getEntry();
        color();
    }

    private void spill() {
        function.body.forEach(block -> block.instructions.forEach(instruction -> {
            int count = 0;
            for (IrRegister register : block.instructionLiveOut.get(instruction)) {
                if (!spilled.contains(register)) {
                    count++;
                }
                if (count > K) {
                    spilled.add(register);
                }
            }
        }));
    }

    private void color() {
        block.liveIn.forEach(register -> {
            if (colorMap.containsKey(register)) {
                inUseColors.add(colorMap.get(register));
            }
        });
        block.instructions.forEach(instruction -> {
            instruction.uses().forEach(operand -> {
                if (operand.asConcrete() instanceof IrRegister register
                        && colorMap.containsKey(register)
                        && !block.instructionLiveOut.get(instruction).contains(register)) {
                    inUseColors.remove(colorMap.get(register));
                }
            });
            instruction.defs().forEach(register -> {
                if (!spilled.contains(register)) {
                    int color = 0; // should never exceed K
                    while (!inUseColors.add(color)) {
                        color++;
                    }
                    colorMap.put(register, color);
                }
            });
        });
        for (BlockIr children : block.dominatorTreeChildren) {
            this.block = children;
            color();
        }
    }
}
