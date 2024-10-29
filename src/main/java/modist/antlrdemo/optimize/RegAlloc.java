package modist.antlrdemo.optimize;

import modist.antlrdemo.backend.asm.metadata.Register;
import modist.antlrdemo.frontend.ir.metadata.IrGlobal;
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
        Map<IrGlobal, Integer> globalUseCount = new HashMap<>();
        int maxRegisterTop = 0;
        for (FunctionIr function : program.functions) {
            function.body.forEach(block -> block.instructions.forEach(instruction -> {
                if (instruction instanceof InstructionIr.Load load &&
                        load.pointer().asConcrete() instanceof IrGlobal global) {
                    globalUseCount.put(global, globalUseCount.getOrDefault(global, 0) + 1);
                    function.globalLoadRegisterMap.put(load.result(), global);
                }
                if (instruction instanceof InstructionIr.Store store &&
                        store.pointer().asConcrete() instanceof IrGlobal global) {
                    globalUseCount.put(global, globalUseCount.getOrDefault(global, 0) + 1);
                }
            }));
            this.function = function;
            visitFunction();
            maxRegisterTop = Math.max(maxRegisterTop, function.persistentRegisterCount);
        }
        List<Map.Entry<IrGlobal, Integer>> globalUseCountList = new ArrayList<>(globalUseCount.entrySet());
        globalUseCountList.sort(Map.Entry.comparingByValue());
        for (int i = maxRegisterTop; i < Register.SAVED_REGISTERS.length; i++) {
            if (globalUseCountList.isEmpty()) {
                break;
            }
            Map.Entry<IrGlobal, Integer> entry = globalUseCountList.removeLast();
            program.globalRegisterMap.put(entry.getKey(), i);
        }
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
