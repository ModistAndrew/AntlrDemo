package modist.antlrdemo.backend;

import modist.antlrdemo.backend.asm.*;
import modist.antlrdemo.backend.metadata.Register;

import java.io.PrintStream;

public class AsmPrinter {
    private final PrintStream printStream;

    public AsmPrinter(PrintStream printStream) {
        this.printStream = printStream;
    }

    public void print(Asm asm) {
        switch (asm) {
            case ProgramAsm program -> {
                printStream.println("\t.section .text");
                program.text.forEach(this::print);
                printStream.println("\t.section .rodata");
                program.rodata.forEach(this::print);
                printStream.println("\t.section .data");
                program.data.forEach(this::print);
            }
            case FunctionAsm function -> {
                printStream.printf("\t.globl\t%s%n", function.name);
                printStream.printf("\t.type\t%s, @function%n", function.name);
                function.blocks.forEach(this::print);
            }
            case BlockAsm block -> {
                printStream.printf("%s:%n", block.label);
                block.instructions.forEach(instruction -> {
                    printStream.print("\t");
                    print(instruction);
                    printStream.println();
                });
            }
            case ConstantStringAsm constantString -> {
                printStream.printf("\t.type\t%s, @object%n", constantString.name());
                printStream.printf("%s:%n", constantString.name());
                printStream.printf("\t.asciz\t\"%s\"%n", constantString.value());
                printStream.printf("\t.size\t%s, %d%n", constantString.name(), constantString.value().length() + 1);
            }
            case GlobalVariableAsm globalVariable -> {
                printStream.printf("\t.type\t%s, @object%n", globalVariable.name());
                printStream.printf("\t.globl\t%s%n", globalVariable.name());
                printStream.printf("%s:%n", globalVariable.name());
                printStream.printf("\t.word\t%d%n", globalVariable.value());
                printStream.printf("\t.size\t%s, %d%n", globalVariable.name(), Register.BYTE_SIZE);
            }
            case InstructionAsm.Un un -> printStream.printf("%s %s, %s", un.opcode(), un.result(), un.operand());
            case InstructionAsm.Bin bin ->
                    printStream.printf("%s %s, %s, %s", bin.opcode(), bin.result(), bin.left(), bin.right());
            case InstructionAsm.BinImm binImm ->
                    printStream.printf("%s %s, %s, %d", binImm.opcode(), binImm.result(), binImm.left(), binImm.immediate());
            case InstructionAsm.Li li -> printStream.printf("li %s, %d", li.result(), li.immediate());
            case InstructionAsm.Lw lw -> printStream.printf("lw %s, %d(%s)", lw.result(), lw.offset(), lw.base());
            case InstructionAsm.LwLabel lwLabel -> printStream.printf("lw %s, %s", lwLabel.result(), lwLabel.label());
            case InstructionAsm.Sw sw -> printStream.printf("sw %s, %d(%s)", sw.value(), sw.offset(), sw.base());
            case InstructionAsm.SwLabel swLabel -> printStream.printf("sw %s, %s", swLabel.value(), swLabel.label());
            case InstructionAsm.Beqz beqz -> printStream.printf("beqz %s, %s", beqz.value(), beqz.label());
            case InstructionAsm.J j -> printStream.printf("j %s", j.label());
            case InstructionAsm.Call call -> printStream.printf("call %s", call.function());
            case InstructionAsm.Ret ignored -> printStream.print("ret");
        }
    }
}
