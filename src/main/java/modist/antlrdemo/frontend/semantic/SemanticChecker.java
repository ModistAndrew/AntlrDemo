package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.semantic.error.*;
import modist.antlrdemo.frontend.semantic.scope.ChildScope;
import modist.antlrdemo.frontend.semantic.scope.GlobalScope;
import modist.antlrdemo.frontend.semantic.scope.Scope;
import modist.antlrdemo.frontend.ast.node.*;

// check the semantic correctness of the AST together with Symbol and Type
// also store data for IR building
public class SemanticChecker {
    private Scope scope;

    private void pushScope(Scope newScope) {
        scope = newScope;
    }

    private void popScope() {
        if (scope.getParent() != null) {
            scope.getParent().returned |= scope.returned;
        }
        scope = scope.getParent();
    }

    public void visit(Ast node) {
        PositionRecord.set(node.getPosition());
        switch (node) {
            case ProgramAst program -> {
                pushScope(new GlobalScope(program));
                program.definitions.forEach(this::visit);
                popScope();
            }
            case ArrayCreatorAst ignored -> throw new UnsupportedOperationException();
            case DefinitionAst.Class classDefinition -> {
                pushScope(new ChildScope(scope, classDefinition));
                classDefinition.variables.forEach(this::visit);
                classDefinition.constructors.forEach(this::visit);
                classDefinition.functions.forEach(this::visit);
                popScope();
            }
            case DefinitionAst.Function functionDefinition -> {
                pushScope(new ChildScope(scope, functionDefinition));
                functionDefinition.parameters.forEach(this::visit);
                functionDefinition.body.forEach(this::visit);
                if (!scope.returned && functionDefinition.symbol.shouldReturn()) {
                    throw new MissingReturnStatementException();
                }
                popScope();
            }
            case DefinitionAst.Variable variableDefinition -> scope.declareVariable(variableDefinition);
            case ExpressionAst expression -> new Type.Builder(scope).build(expression);
            case StatementAst.Block blockStatement -> {
                pushScope(new ChildScope(scope, blockStatement));
                blockStatement.statements.forEach(this::visit);
                popScope();
            }
            case StatementAst.VariableDefinitions variableDefinitionsStatement ->
                    variableDefinitionsStatement.variables.forEach(this::visit);
            case StatementAst.If ifStatement -> {
                visit(ifStatement.condition);
                ifStatement.condition.type.test(BuiltinFeatures.BOOL);
                pushScope(new ChildScope(scope, ifStatement));
                ifStatement.thenStatements.forEach(this::visit);
                popScope();
                if (ifStatement.elseStatements != null) {
                    pushScope(new ChildScope(scope, ifStatement));
                    ifStatement.elseStatements.forEach(this::visit);
                    popScope();
                }
            }
            case StatementAst.For forStatement -> {
                pushScope(new ChildScope(scope, forStatement));
                if (forStatement.initialization != null) {
                    visit(forStatement.initialization);
                }
                if (forStatement.condition != null) {
                    visit(forStatement.condition);
                    forStatement.condition.type.test(BuiltinFeatures.BOOL);
                }
                if (forStatement.update != null) {
                    visit(forStatement.update);
                }
                forStatement.statements.forEach(this::visit);
                popScope();
            }
            case StatementAst.While whileStatement -> {
                visit(whileStatement.condition);
                whileStatement.condition.type.test(BuiltinFeatures.BOOL);
                pushScope(new ChildScope(scope, whileStatement));
                whileStatement.statements.forEach(this::visit);
                popScope();
            }
            case StatementAst.Break breakStatement -> {
                if (scope.loopLabelName == null) {
                    throw new InvalidControlFlowException("break statement not in loop");
                }
                breakStatement.loopLabelName = scope.loopLabelName;
            }
            case StatementAst.Continue continueStatement -> {
                if (scope.loopLabelName == null) {
                    throw new InvalidControlFlowException("continue statement not in loop");
                }
                continueStatement.loopLabelName = scope.loopLabelName;
            }
            case StatementAst.Return returnStatement -> {
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
                    new Type.Builder(scope).matchExpression(returnStatement.expression, scope.returnType);
                }
                scope.returned = true;
            }
            case StatementAst.Expression expressionStatement -> visit(expressionStatement.expression);
            case StatementAst.Empty ignored -> {
            }
            case TypeAst ignored -> throw new UnsupportedOperationException();
            case ArrayAst ignored ->
                    throw new UnsupportedOperationException();
        }
    }
}
