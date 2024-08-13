package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.error.SemanticException;
import modist.antlrdemo.frontend.syntax.node.*;
import org.jetbrains.annotations.Nullable;

public class SemanticChecker {
    private Scope scope;

    private void pushScope(Scope newScope) {
        scope = newScope;
    }

    private void popScope() {
        scope = scope.getParent();
    }

    // should not use check recursively if some more complicated logic is needed; do it manually
    public void check(@Nullable IAstNode node) {
        switch (node) {
            case null -> { // for convenience of null check
            }
            case ProgramNode program -> {
                pushScope(new GlobalScope(program));
                program.classes.forEach(this::check);
                program.functions.forEach(this::check);
                popScope();
            }
            case ArrayCreatorNode ignored -> throw new UnsupportedOperationException();
            case DeclarationNode.Class classDeclaration -> {
                pushScope(new ChildScope(scope, classDeclaration));
                check(classDeclaration.constructor);
                classDeclaration.functions.forEach(this::check);
                popScope();
            }
            case DeclarationNode.Function functionDeclaration -> {
                pushScope(new ChildScope(scope, functionDeclaration));
                functionDeclaration.body.statements.forEach(this::check); // we've already entered the function scope
                popScope();
            }
            case DeclarationNode.Variable ignored -> throw new UnsupportedOperationException(); // are treated in scope.declareVariable
            case DeclarationNode.Constructor constructorDeclaration -> {
                pushScope(new ChildScope(scope, constructorDeclaration));
                constructorDeclaration.body.statements.forEach(this::check); // we've already entered the function scope
                popScope();
            }
            case DeclarationNode.Parameter ignored -> throw new UnsupportedOperationException();
            case ExpressionNode expression -> new ExpressionType.Builder(scope).build(expression);
            case StatementNode.Block blockStatement -> {
                pushScope(new ChildScope(scope, blockStatement));
                blockStatement.statements.forEach(this::check);
                popScope();
            }
            case StatementNode.VariableDeclarations variableDeclarationsStatement ->
                    variableDeclarationsStatement.variables.forEach(scope::declareVariable);
            case StatementNode.If ifStatement -> {
                new ExpressionType.Builder(scope).expectType(ifStatement.condition, BuiltinFeatures.BOOL);
                check(ifStatement.thenStatement);
                check(ifStatement.elseStatement);
            }
            case StatementNode.For forStatement -> {
                pushScope(new ChildScope(scope, forStatement));
                check(forStatement.initialization);
                if (forStatement.condition != null) {
                    new ExpressionType.Builder(scope).expectType(forStatement.condition, BuiltinFeatures.BOOL);
                }
                check(forStatement.update);
                switch (forStatement.statement) {
                    case StatementNode.Block block ->
                            block.statements.forEach(this::check); // we've already entered the loop scope
                    default -> check(forStatement.statement);
                }
                popScope();
            }
            case StatementNode.While whileStatement -> {
                new ExpressionType.Builder(scope).expectType(whileStatement.condition, BuiltinFeatures.BOOL);
                pushScope(new ChildScope(scope, whileStatement));
                switch (whileStatement.statement) {
                    case StatementNode.Block block ->
                            block.statements.forEach(this::check); // we've already entered the loop scope
                    default -> check(whileStatement.statement);
                }
                popScope();
            }
            case StatementNode.Break ignored -> {
                if (!scope.inLoop) {
                    throw new SemanticException("break statement not in loop", node.getPosition());
                }
            }
            case StatementNode.Continue ignored -> {
                if (!scope.inLoop) {
                    throw new SemanticException("continue statement not in loop", node.getPosition());
                }
            }
            case StatementNode.Return returnStatement -> {
                if (!scope.inFunction) {
                    throw new SemanticException("return statement not in function", node.getPosition());
                }
                if (returnStatement.expression == null || scope.returnType == null) {
                    if (returnStatement.expression != null) {
                        throw new SemanticException("should not return a value", node.getPosition());
                    }
                    if (scope.returnType != null) {
                        throw new SemanticException("should return a value", node.getPosition());
                    }
                } else {
                    new ExpressionType.Builder(scope).expectType(returnStatement.expression, scope.returnType);
                }
            }
            case StatementNode.Expression expressionStatement -> check(expressionStatement.expression);
            case TypeNode ignored -> throw new UnsupportedOperationException();
        }
    }
}
