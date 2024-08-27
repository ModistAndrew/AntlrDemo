package modist.antlrdemo.backend.asm.node;

import java.util.ArrayList;
import java.util.List;

public final class BlockAsm implements Asm {
    public final String label;
    public final List<InstructionAsm> instructions = new ArrayList<>();

    public BlockAsm(String label) {
        this.label = label;
    }
}
