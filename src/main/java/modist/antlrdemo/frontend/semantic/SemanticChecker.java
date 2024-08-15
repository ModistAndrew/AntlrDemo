package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.error.CompileErrorType;
import modist.antlrdemo.frontend.error.CompileException;
import modist.antlrdemo.frontend.semantic.scope.ChildScope;
import modist.antlrdemo.frontend.semantic.scope.GlobalScope;
import modist.antlrdemo.frontend.semantic.scope.Scope;
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

    private void testExpressionType(ExpressionNode expression, Type expectedType) {
        new Type.Builder(scope).testExpressionType(expression, expectedType);
    }

    private void tryMatchExpression(ExpressionOrArrayNode expression, Type expectedType) {
        new Type.Builder(scope).tryMatchExpression(expression, expectedType);
    }

    // should not use check recursively if some more complicated logic is needed
    public void check(@Nullable IAstNode node) {
        switch (node) {
            case null -> { // for convenience of null check
            }
            case ProgramNode program -> {
                pushScope(new GlobalScope(program));
                program.declarations.forEach(this::check);
                popScope();
            }
            case ArrayCreatorNode ignored -> throw new UnsupportedOperationException();
            case DeclarationNode.Class classDeclaration -> {
                pushScope(new ChildScope(scope, classDeclaration));
                classDeclaration.variables.forEach(this::check);
                check(classDeclaration.constructor);
                classDeclaration.functions.forEach(this::check);
                popScope();
            }
            case DeclarationNode.Function functionDeclaration -> {
                pushScope(new ChildScope(scope, functionDeclaration));
                functionDeclaration.parameters.forEach(this::check);
                functionDeclaration.body.forEach(this::check);
                popScope();
            }
            case DeclarationNode.Variable variableDeclaration -> scope.declareVariable(variableDeclaration);
            case ExpressionNode expression -> new Type.Builder(scope).build(expression);
            case StatementNode.Block blockStatement -> {
                pushScope(new ChildScope(scope, blockStatement));
                blockStatement.statements.forEach(this::check);
                popScope();
            }
            case StatementNode.VariableDeclarations variableDeclarationsStatement ->
                    variableDeclarationsStatement.variables.forEach(this::check);
            case StatementNode.If ifStatement -> {
                testExpressionType(ifStatement.condition, BuiltinFeatures.BOOL);
                pushScope(new ChildScope(scope, ifStatement));
                ifStatement.thenStatements.forEach(this::check);
                popScope();
                if (ifStatement.elseStatements != null) {
                    pushScope(new ChildScope(scope, ifStatement));
                    ifStatement.elseStatements.forEach(this::check);
                    popScope();
                }
            }
            case StatementNode.For forStatement -> {
                pushScope(new ChildScope(scope, forStatement));
                check(forStatement.initialization);
                if (forStatement.condition != null) {
                    testExpressionType(forStatement.condition, BuiltinFeatures.BOOL);
                }
                check(forStatement.update);
                forStatement.statements.forEach(this::check);
                popScope();
            }
            case StatementNode.While whileStatement -> {
                testExpressionType(whileStatement.condition, BuiltinFeatures.BOOL);
                pushScope(new ChildScope(scope, whileStatement));
                whileStatement.statements.forEach(this::check);
                popScope();
            }
            case StatementNode.Break ignored -> {
                if (!scope.inLoop) {
                    throw new CompileException(CompileErrorType.INVALID_CONTROL_FLOW, "break statement not in loop", node.getPosition());
                }
            }
            case StatementNode.Continue ignored -> {
                if (!scope.inLoop) {
                    throw new CompileException(CompileErrorType.INVALID_CONTROL_FLOW, "continue statement not in loop", node.getPosition());
                }
            }
            case StatementNode.Return returnStatement -> {
                if (scope.returnType == null) {
                    throw new CompileException("return statement not in function", node.getPosition());
                }
                if (scope.returnType.isVoid()) {
                    if (returnStatement.expression != null) {
                        throw new CompileException("return statement with expression in non-void function", node.getPosition());
                    }
                } else if (returnStatement.expression == null) {
                    throw new CompileException("return statement without expression in non-void function", node.getPosition());
                } else {
                    tryMatchExpression(returnStatement.expression, scope.returnType);
                }
            }
            case StatementNode.Expression expressionStatement -> check(expressionStatement.expression);
            case StatementNode.Empty ignored -> {
            }
            case TypeNode ignored -> throw new UnsupportedOperationException();
            case ArrayNode ignored -> throw new UnsupportedOperationException();
        }
    }
}
