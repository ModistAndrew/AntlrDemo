package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.error.SemanticException;
import modist.antlrdemo.frontend.syntax.node.*;

public class SemanticChecker {
    private Scope scope;

    private void pushScope(Scope newScope) {
        scope = newScope;
    }

    private void popScope() {
        scope = scope.getParent();
    }

    public void check(IAstNode node) {
        switch (node) {
            case ProgramNode program -> {
                pushScope(new GlobalScope(program));
                program.classes.forEach(this::check);
                program.functions.forEach(this::check);
                popScope();
            }
            case ArrayCreatorNode ignored -> throw new UnsupportedOperationException();
            case DeclarationNode.Class classDeclaration -> {
                pushScope(new ChildScope.Class(scope, classDeclaration));
                if (classDeclaration.constructor != null) {
                    check(classDeclaration.constructor);
                }
                classDeclaration.functions.forEach(this::check);
                popScope();
            }
            case DeclarationNode.Function functionDeclaration -> {
                pushScope(new ChildScope.Function(scope, functionDeclaration));
                check(functionDeclaration.body);
                popScope();
            }
            case DeclarationNode.Variable ignored -> throw new UnsupportedOperationException();
            case DeclarationNode.Constructor constructorDeclaration -> {
                pushScope(new ChildScope.Function(scope, constructorDeclaration));
                check(constructorDeclaration.body);
                popScope();
            }
            case DeclarationNode.Parameter ignored -> throw new UnsupportedOperationException();
            case ExpressionNode expression -> new ExpressionType.Builder(scope).build(expression); // for simple check
            case StatementNode.Block blockStatement -> blockStatement.statements.forEach(this::check);
            case StatementNode.VariableDeclarations variableDeclarationsStatement ->
                    scope.declareLocalVariables(variableDeclarationsStatement);
            case StatementNode.If ifStatement -> {
                new ExpressionType.Builder(scope).expectType(ifStatement.condition, BuiltinFeatures.BOOL);
                check(ifStatement.thenStatement);
                if (ifStatement.elseStatement != null) {
                    check(ifStatement.elseStatement);
                }
            }
            case StatementNode.For forStatement -> {
                if (forStatement.initialization != null) {
                    check(forStatement.initialization);
                }
                pushScope(new ChildScope.Loop(scope, forStatement));
                if (forStatement.condition != null) {
                    new ExpressionType.Builder(scope).expectType(forStatement.condition, BuiltinFeatures.BOOL);
                }
                if (forStatement.update != null) {
                    check(forStatement.update);
                }
                check(forStatement.statement);
                popScope();
            }
            case StatementNode.While whileStatement -> {
                new ExpressionType.Builder(scope).expectType(whileStatement.condition, BuiltinFeatures.BOOL);
                pushScope(new ChildScope.Loop(scope, whileStatement));
                check(whileStatement.statement);
                popScope();
            }
            case StatementNode.Break ignored -> {
                if (!(scope instanceof ChildScope.Loop)) {
                    throw new SemanticException("break statement not in loop", node.getPosition());
                }
            }
            case StatementNode.Continue ignored -> {
                if (!(scope instanceof ChildScope.Loop)) {
                    throw new SemanticException("continue statement not in loop", node.getPosition());
                }
            }
            case StatementNode.Return returnStatement -> {
                if (scope instanceof ChildScope.Function functionScope) {
                    if (returnStatement.expression == null || functionScope.returnType == null) {
                        if (returnStatement.expression != null) {
                            throw new SemanticException("should not return a value", node.getPosition());
                        }
                        if (functionScope.returnType != null) {
                            throw new SemanticException("should return a value", node.getPosition());
                        }
                    } else {
                        new ExpressionType.Builder(scope).expectType(returnStatement.expression, functionScope.returnType);
                    }
                } else {
                    throw new SemanticException("return statement not in function", node.getPosition());
                }
            }
            case StatementNode.Expression expressionStatement -> check(expressionStatement.expression);
            case TypeNode ignored -> throw new UnsupportedOperationException();
        }
    }
}
