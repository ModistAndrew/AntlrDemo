package modist.antlrdemo.frontend.ir.node;

import java.util.ArrayList;
import java.util.List;

public final class BlockIr implements Ir {
    public final String label;
    public final List<InstructionIr> instructions = new ArrayList<>();

    private BlockIr(String label) {
        this.label = label;
    }

    public static class Builder {
        private BlockIr current;
        private boolean finished;

        public void add(InstructionIr instruction) {
            if (!finished) {
                current.instructions.add(instruction);
            }
            if (instruction instanceof InstructionIr.End) {
                finished = true;
            }
        }

        public BlockIr build(InstructionIr.End end) {
            add(end); // must add the end instruction to the block
            return current;
        }

        public void begin(String label) {
            current = new BlockIr(label);
            finished = false;
        }

        public String currentLabel() {
            return current.label;
        }
    }
}
