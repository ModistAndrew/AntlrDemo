package modist.antlrdemo.optimize;

import modist.antlrdemo.backend.asm.metadata.Register;
import modist.antlrdemo.frontend.ir.metadata.IrRegister;
import modist.antlrdemo.frontend.ir.node.BlockIr;
import modist.antlrdemo.frontend.ir.node.FunctionIr;
import modist.antlrdemo.frontend.ir.node.InstructionIr;
import modist.antlrdemo.frontend.ir.node.ProgramIr;

import java.util.*;

public class RegAlloc {
    private FunctionIr function;
    private BlockIr block;

    public void visitProgram(ProgramIr program) {
        program.functions.forEach(function -> {
            this.function = function;
            visitFunction();
        });
    }

    private void visitFunction() {
        this.block = function.getEntry();
        color();
        function.registerMap.values().forEach(i ->
                function.persistentRegisterCount = Math.max(function.persistentRegisterCount, i + 1));
        function.getEntry().instructions.forEach(instruction -> {
            if (instruction instanceof InstructionIr.Param param) {
                function.usefulParams.add(param);
            }
        });
    }

    private void color() {
        Set<Integer> inUseColors = new HashSet<>();
        block.liveIn.forEach(register -> {
            if (function.registerMap.containsKey(register)) { // all liveIn registers must be in colorMap or defined in the current block
                inUseColors.add(function.registerMap.get(register));
            }
        });
        block.instructions.forEach(instruction -> {
            // phi instruction is not considered as its uses is not added into liveIn
            if (!(instruction instanceof InstructionIr.Phi)) {
                instruction.uses().forEach(operand -> {
                    if (operand.asConcrete() instanceof IrRegister register
                            && !block.instructionLiveOut.get(instruction).contains(register)) {
                        // defined var must in colorMap (except for phi)
                        inUseColors.remove(function.registerMap.get(register));
                    }
                });
            }
            if (instruction.def() != null) {
                int color = function.persistentRegisters.contains(instruction.def()) ?
                        0 : -Register.TEMP_REGISTERS.length;
                while (!inUseColors.add(color)) {
                    color++;
                }
                function.registerMap.put(instruction.def(), color);
            }
        });
        for (BlockIr children : block.dominatorTreeChildren) {
            this.block = children;
            color();
        }
    }
}
