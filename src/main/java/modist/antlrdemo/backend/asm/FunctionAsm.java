package modist.antlrdemo.backend.asm;

import java.util.ArrayList;
import java.util.List;

public final class FunctionAsm implements Asm {
    public String name;
    public final List<BlockAsm> blocks = new ArrayList<>();
}
