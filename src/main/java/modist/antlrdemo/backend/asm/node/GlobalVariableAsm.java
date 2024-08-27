package modist.antlrdemo.backend.asm.node;

public record GlobalVariableAsm(String name, int value) implements Asm {
    public GlobalVariableAsm(String name) {
        this(name, 0);
    }
}
