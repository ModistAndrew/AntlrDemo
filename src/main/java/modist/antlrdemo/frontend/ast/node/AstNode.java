package modist.antlrdemo.frontend.ast.node;

import modist.antlrdemo.frontend.ast.AstVisitor;

public interface AstNode {
    <T> T accept(AstVisitor<T> visitor);
}
