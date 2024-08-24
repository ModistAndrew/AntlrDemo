package modist.antlrdemo.frontend.ir;

import modist.antlrdemo.frontend.ir.metadata.IrType;
import modist.antlrdemo.frontend.ir.metadata.Variable;
import modist.antlrdemo.frontend.ir.node.*;

import java.io.PrintStream;
import java.util.List;
import java.util.stream.IntStream;

public class IrPrinter {
    private final PrintStream printStream;
    private boolean indent;

    public IrPrinter(PrintStream printStream) {
        this.printStream = printStream;
    }

    private void println(Ir ir) {
        print(ir);
        printStream.println();
    }

    private void push() {
        indent = true;
    }

    private void pop() {
        indent = false;
    }

    public void print(Ir ir) {
        if (indent) {
            printStream.print("  ");
        }
        switch (ir) {
            case ProgramIr program -> {
                if (!program.classDefinitions.isEmpty()) {
                    program.classDefinitions.forEach(this::println);
                    printStream.println();
                }
                if (!program.globalVariables.isEmpty() || !program.constantStrings.isEmpty()) {
                    program.globalVariables.forEach(this::println);
                    program.constantStrings.forEach(this::println);
                    printStream.println();
                }
                if (!program.functionDeclarations.isEmpty() || !program.functionVarargsDeclarations.isEmpty()) {
                    program.functionDeclarations.forEach(this::println);
                    program.functionVarargsDeclarations.forEach(this::println);
                    printStream.println();
                }
                program.functions.forEach(this::println);
            }
            case ClassDefinitionIr classDefinition -> printStream.printf("%s = type { %s }",
                    classDefinition.name(), toStringTypes(classDefinition.members()));
            case GlobalVariableIr globalVariable -> printStream.printf("%s = global %s %s",
                    globalVariable.result(), globalVariable.type(), globalVariable.value());
            case ConstantStringIr constantString -> printStream.printf("%s = constant [%d x i8] c\"%s\"",
                    constantString.result(), constantString.value().length() + 1, escape(constantString.value()) + "\\00");
            case FunctionDeclarationIr functionDeclaration -> printStream.printf("declare %s %s(%s)",
                    functionDeclaration.returnType(), functionDeclaration.name(), toStringTypes(functionDeclaration.parameterTypes()));
            case FunctionVarargsDeclarationIr functionVarargsDeclaration -> printStream.printf("declare %s %s(%s)",
                    functionVarargsDeclaration.returnType(), functionVarargsDeclaration.name(),
                    toStringTypesVarargs(functionVarargsDeclaration.parameterTypes()));
            case FunctionIr function -> {
                printStream.printf("define %s %s(%s) ",
                        function.returnType, function.name, toStringArguments(function.parameterTypes, function.parameters));
                printStream.println("{");
                function.body.forEach(this::print);
                printStream.println("}");
            }
            case BlockIr block -> {
                printStream.print(block.label);
                printStream.println(":");
                push();
                block.instructions.forEach(this::println);
                pop();
            }
            case InstructionIr.Bin bin -> printStream.printf("%s = %s %s %s, %s",
                    bin.result(), bin.operator(), bin.type(), bin.left(), bin.right());
            case InstructionIr.Icmp icmp -> printStream.printf("%s = icmp %s %s %s, %s",
                    icmp.result(), icmp.operator(), icmp.type(), icmp.left(), icmp.right());
            case InstructionIr.Br br -> printStream.printf("br %s %s, label %s, label %s",
                    IrType.I1, br.condition(), br.trueLabel(), br.falseLabel());
            case InstructionIr.Jump jump -> printStream.printf("br label %s", jump.label());
            case InstructionIr.Ret ret -> {
                printStream.printf("ret %s", ret.type());
                if (ret.value() != null) {
                    printStream.printf(" %s", ret.value());
                }
            }
            case InstructionIr.Alloca alloca -> printStream.printf("%s = alloca %s",
                    alloca.result(), alloca.type());
            case InstructionIr.Load load -> printStream.printf("%s = load %s, %s %s",
                    load.result(), load.type(), IrType.PTR, load.pointer());
            case InstructionIr.Store store -> printStream.printf("store %s %s, %s %s",
                    store.type(), store.value(), IrType.PTR, store.pointer());
            case InstructionIr.MemberVariable memberVariable ->
                    printStream.printf("%s = getelementptr %s, %s %s, %s %d, %s %d",
                            memberVariable.result(), memberVariable.type(), IrType.PTR, memberVariable.pointer(),
                            IrType.I32, 0, IrType.I32, memberVariable.memberIndex());
            case InstructionIr.Subscript subscript -> printStream.printf("%s = getelementptr %s, %s %s, %s %s",
                    subscript.result(), subscript.type(), IrType.PTR, subscript.pointer(), IrType.I32, subscript.index());
            case InstructionIr.Call call -> {
                if (call.result() != null) {
                    printStream.printf("%s = ", call.result());
                }
                printStream.printf("call %s %s(%s)",
                        call.type(), call.function(), toStringArguments(call.argumentTypes(), call.arguments()));
            }
            case InstructionIr.CallVarargs callVarargs -> {
                if (callVarargs.result() != null) {
                    printStream.printf("%s = ", callVarargs.result());
                }
                printStream.printf("call %s (%s) %s(%s)",
                        callVarargs.type(), toStringTypesVarargs(callVarargs.functionArgumentTypes()),
                        callVarargs.function(), toStringArguments(callVarargs.argumentTypes(), callVarargs.arguments()));
            }
            case InstructionIr.Phi phi -> printStream.printf("%s = phi %s %s",
                    phi.result(), phi.type(), toStringPhiPairs(phi.values(), phi.labels()));
        }
    }

    private String toStringTypes(List<IrType> types) {
        return String.join(", ", types.stream().map(Object::toString).toList());
    }

    private String toStringTypesVarargs(List<IrType> types) {
        return String.join(", ", types.stream().map(Object::toString).toList()) + ", ...";
    }

    private String toStringArguments(List<IrType> argumentTypes, List<? extends Variable> arguments) {
        return String.join(", ",
                IntStream.range(0, argumentTypes.size()).mapToObj(i -> String.format("%s %s", argumentTypes.get(i), arguments.get(i)))
                        .toList());
    }

    private String toStringPhiPairs(List<Variable> values, List<String> labels) {
        return String.join(", ",
                IntStream.range(0, values.size()).mapToObj(i -> String.format("[ %s, %s ]", values.get(i), labels.get(i)))
                        .toList());
    }

    private String escape(String str) {
        return str.replace("\\", "\\\\").replace("\n", "\\0A").replace("\"", "\\22");
    }
}
