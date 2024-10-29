package modist.antlrdemo.backend.asm;

import modist.antlrdemo.backend.asm.metadata.Location;
import modist.antlrdemo.backend.asm.node.ConstantStringAsm;
import modist.antlrdemo.backend.asm.node.GlobalVariableAsm;
import modist.antlrdemo.backend.asm.node.InstructionAsm;
import modist.antlrdemo.backend.asm.node.ProgramAsm;
import modist.antlrdemo.backend.asm.metadata.Opcode;
import modist.antlrdemo.backend.asm.metadata.Register;
import modist.antlrdemo.frontend.ir.IrNamer;
import modist.antlrdemo.frontend.ir.metadata.*;
import modist.antlrdemo.frontend.ir.node.BlockIr;
import modist.antlrdemo.frontend.ir.node.FunctionIr;
import modist.antlrdemo.frontend.ir.node.InstructionIr;
import modist.antlrdemo.frontend.ir.node.ProgramIr;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.IntStream;

public class AsmBuilder {
    private ProgramAsm program;
    private FunctionBuilder functionBuilder;
    private FunctionIr function;
    private BlockIr block;

    public ProgramAsm visitProgram(ProgramIr programIr) {
        program = new ProgramAsm();
        programIr.globalVariables.forEach(globalVariable ->
                program.data.add(new GlobalVariableAsm(IrNamer.removePrefix(globalVariable.result().name()))));
        programIr.constantStrings.forEach(constantString ->
                program.rodata.add(new ConstantStringAsm(IrNamer.removePrefix(constantString.result().name()), constantString.value())));
        programIr.functions.forEach(function -> {
            this.function = function;
            functionBuilder = new FunctionBuilder(function);
            swap(function.usefulParams.stream().map(param -> functionBuilder.getLocation(param.result())).toList(),
                    function.usefulParams.stream().map(param -> new Movable.Loc(functionBuilder.getParameterLocation(param.index(), true))).toList());
            function.body.forEach(block -> {
                this.block = block;
                functionBuilder.newBlock(functionBuilder.renameLabel(block.label));
                block.instructions.forEach(this::visit);
            });
            program.text.add(functionBuilder.build());
        });
        return program;
    }

    private void visit(InstructionIr ir) {
        switch (ir) {
            case InstructionIr.Param ignored -> { // handled specially
            }
            case InstructionIr.FunctionCall functionCall -> {
                swap(IntStream.range(0, functionCall.arguments().size()).mapToObj(i -> functionBuilder.getParameterLocation(i, false)).toList(),
                        functionCall.arguments().stream().map(Movable.Op::new).toList());
                add(new InstructionAsm.Call(IrNamer.removePrefix(functionCall.function())));
                if (functionCall.result() != null) {
                    functionBuilder.move(functionBuilder.getLocation(functionCall.result()), new Location.Reg(Register.A0));
                }
            }
            case InstructionIr.Phi ignored -> { // handled specially
            }
            case InstructionIr.Result result -> {
                switch (functionBuilder.getLocation(result.result())) {
                    case Location.Reg reg -> visitResult(result, reg.register());
                    case Location.Stack stack -> {
                        visitResult(result, Register.T0);
                        add(new InstructionAsm.Sw(Register.T0, stack.offset(), Register.SP));
                    }
                }
            }
            case InstructionIr.Store store ->
                    add(new InstructionAsm.Sw(get(store.value()), 0, get(store.pointer(), Register.T1)));
            case InstructionIr.Jump jump -> {
                String currentLabel = block.label;
                Map<String, InstructionIr.Phi> toPhis = function.blockMap.get(jump.label()).phiMap;
                addPhis(currentLabel, toPhis);
                add(new InstructionAsm.J(functionBuilder.renameLabel(jump.label())));
            }
            case InstructionIr.Br br -> {
                String currentLabel = block.label;
                Map<String, InstructionIr.Phi> truePhis = function.blockMap.get(br.trueLabel()).phiMap;
                Map<String, InstructionIr.Phi> falsePhis = function.blockMap.get(br.falseLabel()).phiMap;
                boolean truePhiPresent = !truePhis.isEmpty();
                boolean falsePhiPresent = !falsePhis.isEmpty();
                String brTrueLabel = functionBuilder.renameLabel(br.trueLabel());
                String brFalseLabel = functionBuilder.renameLabel(br.falseLabel());
                String trueLabel = truePhiPresent ? functionBuilder.renameLabel(currentLabel) + ".true_phi" : brTrueLabel;
                String falseLabel = falsePhiPresent ? functionBuilder.renameLabel(currentLabel) + ".false_phi" : brFalseLabel;
                if (br.nearTrue()) {
                    add(new InstructionAsm.Bnez(get(br.condition()), trueLabel));
                    add(new InstructionAsm.J(falseLabel));
                } else {
                    add(new InstructionAsm.Beqz(get(br.condition()), falseLabel));
                    add(new InstructionAsm.J(trueLabel));
                }
                if (truePhiPresent) {
                    functionBuilder.newBlock(trueLabel);
                    addPhis(currentLabel, truePhis);
                    add(new InstructionAsm.J(brTrueLabel));
                }
                if (falsePhiPresent) {
                    functionBuilder.newBlock(falseLabel);
                    addPhis(currentLabel, falsePhis);
                    add(new InstructionAsm.J(brFalseLabel));
                }
            }
            case InstructionIr.Ret ret -> {
                if (ret.value() != null) {
                    move(new Location.Reg(Register.A0), new Movable.Op(ret.value()));
                }
                functionBuilder.prepareReturn();
                add(new InstructionAsm.Ret());
            }
        }
    }

    private void visitResult(InstructionIr.Result ir, Register destination) {
        switch (ir) {
            case InstructionIr.Subscript subscript -> {
                add(new InstructionAsm.BinImm(Register.T0, Opcode.SLLI, get(subscript.index()), IrType.LOG_MAX_BYTE_SIZE));
                add(new InstructionAsm.Bin(destination, Opcode.ADD, Register.T0, get(subscript.pointer(), Register.T1)));
            }
            case InstructionIr.MemberVariable memberVariable -> add(new InstructionAsm.BinImm(destination,
                    Opcode.ADDI, get(memberVariable.pointer()), memberVariable.memberIndex() * IrType.MAX_BYTE_SIZE));
            case InstructionIr.Load load -> add(new InstructionAsm.Lw(destination, 0, get(load.pointer())));
            case InstructionIr.Bin bin -> add(new InstructionAsm.Bin(destination,
                    bin.operator().opcode, get(bin.left()), get(bin.right(), Register.T1)));
            case InstructionIr.Icmp icmp -> {
                switch (icmp.operator()) {
                    case EQ, NE -> {
                        add(new InstructionAsm.Bin(Register.T0, Opcode.XOR, get(icmp.left()), get(icmp.right(), Register.T1)));
                        add(new InstructionAsm.Un(destination, icmp.operator() == IrOperator.EQ ? Opcode.SEQZ : Opcode.SNEZ, Register.T0));
                    }
                    case SLT, SGT -> add(new InstructionAsm.Bin(destination, Opcode.SLT,
                            get(icmp.operator() == IrOperator.SLT ? icmp.left() : icmp.right()),
                            get(icmp.operator() == IrOperator.SLT ? icmp.right() : icmp.left(), Register.T1)));
                    case SLE, SGE -> {
                        add(new InstructionAsm.Bin(Register.T0, Opcode.SLT,
                                get(icmp.operator() == IrOperator.SGE ? icmp.left() : icmp.right()),
                                get(icmp.operator() == IrOperator.SGE ? icmp.right() : icmp.left(), Register.T1)));
                        add(new InstructionAsm.Un(destination, Opcode.SEQZ, Register.T0));
                    }
                    default -> throw new IllegalArgumentException();
                }
            }
            case InstructionIr.Phi ignored -> throw new UnsupportedOperationException(); // handled in visit
            case InstructionIr.Param ignored -> throw new UnsupportedOperationException(); // handled in visit
            case InstructionIr.FunctionCall ignored -> throw new UnsupportedOperationException(); // handled in visit
        }
    }

    private Register get(IrOperand source, Register temp) {
        return switch (source.asConcrete()) {
            case IrGlobal global -> add(new InstructionAsm.La(temp, IrNamer.removePrefix(global.name())));
            case IrRegister register -> functionBuilder.get(functionBuilder.getLocation(register), temp);
            case IrConstant constant -> constant.asImmediate() == 0 ? Register.ZERO :
                    add(new InstructionAsm.Li(temp, constant.asImmediate()));
            case IrUndefined ignored -> Register.ZERO;
        };
    }

    private void move(Location destination, Movable source) {
        switch (source) {
            case Movable.Loc loc -> functionBuilder.move(destination, loc.location());
            case Movable.Op op -> {
                switch (op.operand().asConcrete()) {
                    case IrGlobal global -> {
                        switch (destination) {
                            case Location.Reg reg ->
                                    add(new InstructionAsm.La(reg.register(), IrNamer.removePrefix(global.name())));
                            case Location.Stack stack -> {
                                add(new InstructionAsm.La(Register.T0, IrNamer.removePrefix(global.name())));
                                add(new InstructionAsm.Sw(Register.T0, stack.offset(), Register.SP));
                            }
                        }
                    }
                    case IrRegister register ->
                            functionBuilder.move(destination, functionBuilder.getLocation(register));
                    case IrConstant constant -> {
                        switch (destination) {
                            case Location.Reg reg -> add(new InstructionAsm.Li(reg.register(), constant.asImmediate()));
                            case Location.Stack stack -> {
                                add(new InstructionAsm.Li(Register.T0, constant.asImmediate()));
                                add(new InstructionAsm.Sw(Register.T0, stack.offset(), Register.SP));
                            }
                        }
                    }
                    case IrUndefined ignored -> {
                    }
                }
            }
        }
    }

    private Register get(IrOperand operand) {
        return get(operand, Register.T0);
    }

    private void add(InstructionAsm instruction) {
        functionBuilder.add(instruction);
    }

    private Register add(InstructionAsm.Result instruction) {
        return functionBuilder.add(instruction);
    }

    private void addPhis(String fromLabel, Map<String, InstructionIr.Phi> toPhis) {
        List<Location> destinations = toPhis.values().stream().map(phi -> functionBuilder.getLocation(phi.result())).toList();
        List<Movable.Op> sources = toPhis.values().stream().map(phi -> new Movable.Op(phi.values().get(phi.labels().indexOf(fromLabel)))).toList();
        swap(destinations, sources);
    }

    private void swap(List<Location> destinations, List<? extends Movable> sources) {
        Map<Location, Set<Location>> destinationMap = new HashMap<>(); // only present for locations which are also destinations of other moves
        Map<Location, Movable> sourceMap = new HashMap<>();
        destinations.forEach(destination -> destinationMap.put(destination, new HashSet<>()));
        for (int i = 0; i < destinations.size(); i++) {
            Location destination = destinations.get(i);
            Movable source = sources.get(i);
            sourceMap.put(destination, source);
            Location sourceLocation = asLocation(source);
            if (destinationMap.containsKey(sourceLocation)) {
                destinationMap.get(sourceLocation).add(destination);
            }
        }
        while (!sourceMap.isEmpty()) {
            Location destinationLocation = sourceMap.keySet().iterator().next();
            Movable source = sourceMap.get(destinationLocation);
            Stack<Location> visited = new Stack<>();
            Location current = destinationLocation;
            boolean hasEnd = false;
            while (!visited.contains(current)) {
                visited.push(current);
                if (destinationMap.get(current).isEmpty()) {
                    hasEnd = true;
                    break;
                }
                current = destinationMap.get(current).iterator().next();
            }
            if (hasEnd) {
                while (!visited.peek().equals(destinationLocation)) {
                    Location d = visited.pop();
                    Location s = visited.peek();
                    functionBuilder.move(d, s);
                    sourceMap.remove(d);
                    destinationMap.get(s).remove(d);
                }
                move(destinationLocation, source);
                sourceMap.remove(destinationLocation);
                Location sourceLocation = asLocation(source);
                if (destinationMap.containsKey(sourceLocation)) {
                    destinationMap.get(sourceLocation).remove(destinationLocation);
                }
            } else {
                sourceMap.remove(current);
                destinationMap.get(visited.peek()).remove(current);
                // if self-loop, do nothing, otherwise do move on the loop
                if (!visited.peek().equals(current)) {
                    // store peek to T1 as it will never be used in move
                    functionBuilder.move(new Location.Reg(Register.T1), visited.peek());
                    while (!visited.peek().equals(current)) {
                        Location d = visited.pop();
                        Location s = visited.peek();
                        functionBuilder.move(d, s);
                        sourceMap.remove(d);
                        destinationMap.get(s).remove(d);
                    }
                    functionBuilder.move(current, new Location.Reg(Register.T1));
                }
            }
        }
    }

    @Nullable
    private Location asLocation(Movable movable) {
        return switch (movable) {
            case Movable.Loc loc -> loc.location();
            case Movable.Op op -> switch (op.operand().asConcrete()) {
                case IrRegister register -> functionBuilder.getLocation(register);
                default -> null;
            };
        };
    }

    private sealed interface Movable {
        record Loc(Location location) implements Movable {
        }

        record Op(IrOperand operand) implements Movable {
        }
    }
}
