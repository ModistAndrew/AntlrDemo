package modist.antlrdemo.optimize;

import modist.antlrdemo.frontend.ir.IrNamer;
import modist.antlrdemo.frontend.ir.metadata.IrConcrete;
import modist.antlrdemo.frontend.ir.metadata.IrRegister;
import modist.antlrdemo.frontend.ir.metadata.VariableDef;
import modist.antlrdemo.frontend.ir.metadata.VariableUse;
import modist.antlrdemo.frontend.ir.node.BlockIr;
import modist.antlrdemo.frontend.ir.node.FunctionIr;
import modist.antlrdemo.frontend.ir.node.InstructionIr;
import modist.antlrdemo.frontend.ir.node.ProgramIr;

import java.util.*;

public class Mem2Reg {
    private FunctionIr function;
    public IrNamer irNamer;
    private final Map<String, Stack<IrConcrete>> variableStacks = new HashMap<>();

    public void visitProgram(ProgramIr program) {
        program.functions.forEach(function -> {
            this.function = function;
            this.irNamer = new IrNamer();
            this.variableStacks.clear();
            visitFunction();
        });
    }

    private void visitFunction() {
        placePhi();
        fillVariables(function.getEntry());
        function.body.forEach(block -> block.phiMap.forEach((name, phi) -> block.instructions.addFirst(phi)));
    }

    private void placePhi() {
        function.body.forEach(block ->
                block.variableDefs.forEach(def ->
                        block.dominanceFrontiers.forEach(dominanceFrontier -> insertPhi(dominanceFrontier, def))));
    }


    private void insertPhi(BlockIr block, VariableDef def) {
        if (block.phiMap.containsKey(def.name())) {
            return;
        }
        InstructionIr.Phi phi = new InstructionIr.Phi(new IrRegister(irNamer.phiVariable(def.name())), def.type());
        block.phiMap.put(def.name(), phi);
        block.dominanceFrontiers.forEach(dominanceFrontier -> insertPhi(dominanceFrontier, def));
    }

    private void fillVariables(BlockIr block) {
        block.phiMap.forEach((name, phi) -> pushVariable(name, phi.result()));
        block.variableReferences.forEach(variableReference -> {
            switch (variableReference) {
                // use concrete value
                case VariableDef def -> pushVariable(def.name(), switch (def.value()) {
                    case IrConcrete concrete -> concrete;
                    case VariableUse use -> peekVariable(use.name);
                    case null -> null;
                });
                case VariableUse use -> use.value = peekVariable(use.name);
            }
        });
        block.successors.forEach(successor -> {
            successor.phiMap.keySet().removeIf(name -> !variablePresent(name)); // remove phi that should never be used
            successor.phiMap.forEach((name, phi) -> phi.add(block, peekVariable(name)));
        });
        block.dominatorTreeChildren.forEach(this::fillVariables);
        block.variableDefs.forEach(def -> popVariable(def.name()));
        block.phiMap.forEach((name, phi) -> popVariable(name));
    }

    private boolean variablePresent(String name) {
        return variableStacks.containsKey(name) && !variableStacks.get(name).isEmpty();
    }

    private void pushVariable(String name, IrConcrete value) {
        if (!variableStacks.containsKey(name)) {
            variableStacks.put(name, new Stack<>());
        }
        variableStacks.get(name).push(value);
    }

    private void popVariable(String name) {
        variableStacks.get(name).pop();
    }

    private IrConcrete peekVariable(String name) {
        return variableStacks.get(name).peek();
    }
}
