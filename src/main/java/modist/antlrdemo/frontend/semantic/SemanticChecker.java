package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.error.*;
import modist.antlrdemo.frontend.semantic.scope.ChildScope;
import modist.antlrdemo.frontend.semantic.scope.GlobalScope;
import modist.antlrdemo.frontend.semantic.scope.Scope;
import modist.antlrdemo.frontend.ast.node.*;
import org.jetbrains.annotations.Nullable;

public class SemanticChecker {
    private Scope scope;
    private boolean returned;

    private void pushScope(Scope newScope) {
        scope = newScope;
    }

    private void popScope() {
        scope = scope.getParent();
    }

    public void check(@Nullable IAstNode node) {
        if (node == null) {
            return;
        }
        PositionRecord.set(node.getPosition());
        switch (node) {
            case ProgramNode program -> {
                pushScope(new GlobalScope(program));
                program.declarations.forEach(this::check);
                popScope();
            }
            case ArrayCreatorNode ignored -> throw new UnsupportedOperationException();
            case DeclarationNode.Class classDeclaration -> {
                pushScope(new ChildScope(scope, classDeclaration));
                classDeclaration.variables.forEach(this::check);
                classDeclaration.constructors.forEach(this::check);
                classDeclaration.functions.forEach(this::check);
                popScope();
            }
            case DeclarationNode.Function functionDeclaration -> {
                pushScope(new ChildScope(scope, functionDeclaration));
                functionDeclaration.parameters.forEach(this::check);
                returned = false;
                functionDeclaration.body.forEach(this::check);
                if (!returned && functionDeclaration.symbol.shouldReturn()) {
                    throw new MissingReturnStatementException();
                }
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
                check(ifStatement.condition);
                ifStatement.condition.type.testType(BuiltinFeatures.BOOL);
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
                    check(forStatement.condition);
                    forStatement.condition.type.testType(BuiltinFeatures.BOOL);
                }
                check(forStatement.update);
                forStatement.statements.forEach(this::check);
                popScope();
            }
            case StatementNode.While whileStatement -> {
                check(whileStatement.condition);
                whileStatement.condition.type.testType(BuiltinFeatures.BOOL);
                pushScope(new ChildScope(scope, whileStatement));
                whileStatement.statements.forEach(this::check);
                popScope();
            }
            case StatementNode.Break ignored -> {
                if (!scope.inLoop) {
                    throw new InvalidControlFlowException("break statement not in loop");
                }
            }
            case StatementNode.Continue ignored -> {
                if (!scope.inLoop) {
                    throw new InvalidControlFlowException("continue statement not in loop");
                }
            }
            case StatementNode.Return returnStatement -> {
                if (scope.returnType == null) {
                    throw new CompileException("return statement not in function");
                }
                if (scope.returnType.isVoid()) {
                    if (returnStatement.expression != null) {
                        throw new TypeMismatchException("non-void", BuiltinFeatures.VOID);
                    }
                } else if (returnStatement.expression == null) {
                    throw new TypeMismatchException(BuiltinFeatures.VOID, scope.returnType);
                } else {
                    new Type.Builder(scope).tryMatchExpression(returnStatement.expression, scope.returnType);
                }
                returned = true;
            }
            case StatementNode.Expression expressionStatement -> check(expressionStatement.expression);
            case StatementNode.Empty ignored -> {
            }
            case TypeNode ignored -> throw new UnsupportedOperationException();
            case ArrayNode ignored -> throw new UnsupportedOperationException(); // cannot visit this directly. have to use tryMatchExpression for type info
        }
    }
}
