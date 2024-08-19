package modist.antlrdemo.frontend.ir.node;

import modist.antlrdemo.frontend.semantic.Type;

import java.util.List;

public abstract sealed class DeclNode extends IrNode {
    public String name;

    public static final class Class extends DeclNode {
        public List<Type> members;
    }

    public static final class Function extends DeclNode {
        public Type returnType;
        public List<Type> parameters;
        public StmtNode.Block body;
    }
}
