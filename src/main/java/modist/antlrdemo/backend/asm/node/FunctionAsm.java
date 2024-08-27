package modist.antlrdemo.backend.asm.node;

import java.util.*;

public final class FunctionAsm implements Asm {
    public final String name;
    // the first block should be with the same name as the function
    public final List<BlockAsm> blocks = new ArrayList<>();

    public FunctionAsm(String name) {
        this.name = name;
        blocks.add(new BlockAsm(name));
    }
}
