package modist.antlrdemo.backend.asm;

import modist.antlrdemo.backend.asm.node.*;

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
                printStream.printf("%s:%n", constantString.name());
                printStream.printf("\t.asciz\t\"%s\"%n", escape(constantString.value()));
            }
            case GlobalVariableAsm globalVariable -> {
                printStream.printf("\t.globl\t%s%n", globalVariable.name());
                printStream.printf("%s:%n", globalVariable.name());
                printStream.printf("\t.word\t%d%n", globalVariable.value());
            }
            case InstructionAsm.Un un -> printStream.printf("%s %s, %s", un.opcode(), un.result(), un.operand());
            case InstructionAsm.Bin bin ->
                    printStream.printf("%s %s, %s, %s", bin.opcode(), bin.result(), bin.left(), bin.right());
            case InstructionAsm.BinImm binImm ->
                    printStream.printf("%s %s, %s, %d", binImm.opcode(), binImm.result(), binImm.left(), binImm.immediate());
            case InstructionAsm.Mv mv -> printStream.printf("mv %s, %s", mv.result(), mv.operand());
            case InstructionAsm.La la -> printStream.printf("la %s, %s", la.result(), la.label());
            case InstructionAsm.Li li -> printStream.printf("li %s, %d", li.result(), li.immediate());
            case InstructionAsm.Lw lw -> printStream.printf("lw %s, %d(%s)", lw.result(), lw.immediate(), lw.base());
            case InstructionAsm.Sw sw -> printStream.printf("sw %s, %d(%s)", sw.value(), sw.immediate(), sw.base());
            case InstructionAsm.SwLabel swLabel -> printStream.printf("sw %s, %s", swLabel.value(), swLabel.label());
            case InstructionAsm.Beqz beqz -> printStream.printf("beqz %s, %s", beqz.value(), beqz.label());
            case InstructionAsm.Bnez bnez -> printStream.printf("bnez %s, %s", bnez.value(), bnez.label());
            case InstructionAsm.J j -> printStream.printf("j %s", j.label());
            case InstructionAsm.Call call -> printStream.printf("call %s", call.function());
            case InstructionAsm.Ret ignored -> printStream.print("ret");
        }
    }

    private String escape(String str) {
        return str.replace("\\", "\\\\").replace("\n", "\\n").replace("\"", "\\\"");
    }
}
