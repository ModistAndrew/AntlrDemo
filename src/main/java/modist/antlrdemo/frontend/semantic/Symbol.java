package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.syntax.node.DeclarationNode;

public abstract class Symbol<T extends DeclarationNode> {
    private final T declaration;

    private Symbol(T declaration) {
        this.declaration = declaration;
    }

    private static class Class extends Symbol<DeclarationNode.Class> {
        private Class(DeclarationNode.Class declaration) {
            super(declaration);
        }
    }

    private static class Function extends Symbol<DeclarationNode.Function> {
        private Function(DeclarationNode.Function declaration) {
            super(declaration);
        }
    }

    private static class Variable extends Symbol<DeclarationNode.Variable> {
        private Variable(DeclarationNode.Variable declaration) {
            super(declaration);
        }
    }

    private static class Parameter extends Symbol<DeclarationNode.Parameter> {
        private Parameter(DeclarationNode.Parameter declaration) {
            super(declaration);
        }
    }
}