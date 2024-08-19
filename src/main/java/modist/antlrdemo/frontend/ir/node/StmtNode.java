package modist.antlrdemo.frontend.ir.node;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract sealed class StmtNode extends IrNode {
    public static final class Block extends StmtNode {
        public String label;
        public List<InstNode> insts;
    }

    public static final class Loop extends StmtNode {
        @Nullable
        public Block init;
        public Block condition;
        public Block loop;
        @Nullable
        public Block update;
        public Block end;
    }

    public static final class If extends StmtNode {
        public Block condition;
        public Block then;
        @Nullable
        public Block orElse;
    }
}
