package modist.antlrdemo.backend.asm;

import java.util.ArrayList;
import java.util.List;

public final class BlockAsm implements Asm {
    public String name;
    public final List<InstructionAsm> instructions = new ArrayList<>();
}
