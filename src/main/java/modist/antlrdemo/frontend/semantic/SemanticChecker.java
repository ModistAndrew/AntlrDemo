package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.BuiltinFeatures;
import modist.antlrdemo.frontend.error.*;
import modist.antlrdemo.frontend.semantic.scope.ChildScope;
import modist.antlrdemo.frontend.semantic.scope.GlobalScope;
import modist.antlrdemo.frontend.semantic.scope.Scope;
import modist.antlrdemo.frontend.ast.node.*;

// check the semantic correctness of the AST
// also store data for ir building
// data storage is dealt with:
// Symbol(Symbol, irName, classType, memberIndex, ...) in Scope;
// Scope(labelName, local variable irName, ...) in SemanticChecker;
// Type(Type, presentDimensions, ...) in SemanticChecker;
// SemanticChecker(loopLabelName, ...)
// with the help of SemanticNamer in Scope and global
public class SemanticChecker {
    private Scope scope;
    // whether the function has returned. not stored in scope as it should be spread upwards, which is hard to deal with in scope
    private boolean returned;

    private void pushScope(Scope newScope) {
        scope = newScope;
    }

    private void popScope() {
        scope = scope.getParent();
    }

    public void check(Ast node) {
        PositionRecord.set(node.getPosition());
        switch (node) {
            case ProgramAst program -> {
                pushScope(new GlobalScope(program));
                program.definitions.forEach(this::check);
                popScope();
            }
            case ArrayCreatorAst ignored -> throw new UnsupportedOperationException();
            case DefinitionAst.Class classDefinition -> {
                pushScope(new ChildScope(scope, classDefinition));
                classDefinition.variables.forEach(this::check);
                classDefinition.constructors.forEach(this::check);
                classDefinition.functions.forEach(this::check);
                popScope();
            }
            case DefinitionAst.Function functionDefinition -> {
                pushScope(new ChildScope(scope, functionDefinition));
                functionDefinition.parameters.forEach(this::check);
                returned = false;
                functionDefinition.body.forEach(this::check);
                if (!returned && functionDefinition.symbol.shouldReturn()) {
                    throw new MissingReturnStatementException();
                }
                popScope();
            }
            case DefinitionAst.Variable variableDefinition -> scope.declareVariable(variableDefinition);
            case ExpressionAst expression -> new Type.Builder(scope).build(expression);
            case StatementAst.Block blockStatement -> {
                pushScope(new ChildScope(scope, blockStatement));
                blockStatement.statements.forEach(this::check);
                popScope();
            }
            case StatementAst.VariableDefinitions variableDefinitionsStatement ->
                    variableDefinitionsStatement.variables.forEach(this::check);
            case StatementAst.If ifStatement -> {
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
            case StatementAst.For forStatement -> {
                pushScope(new ChildScope(scope, forStatement));
                if (forStatement.initialization != null) {
                    check(forStatement.initialization);
                }
                if (forStatement.condition != null) {
                    check(forStatement.condition);
                    forStatement.condition.type.testType(BuiltinFeatures.BOOL);
                }
                if (forStatement.update != null) {
                    check(forStatement.update);
                }
                forStatement.statements.forEach(this::check);
                popScope();
            }
            case StatementAst.While whileStatement -> {
                check(whileStatement.condition);
                whileStatement.condition.type.testType(BuiltinFeatures.BOOL);
                pushScope(new ChildScope(scope, whileStatement));
                whileStatement.statements.forEach(this::check);
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
                    new Type.Builder(scope).tryMatchExpression(returnStatement.expression, scope.returnType);
                }
                returned = true;
            }
            case StatementAst.Expression expressionStatement -> check(expressionStatement.expression);
            case StatementAst.Empty ignored -> {
            }
            case TypeAst ignored -> throw new UnsupportedOperationException();
            case ArrayAst ignored ->
                    throw new UnsupportedOperationException(); // cannot visit this directly. have to use tryMatchExpression for type info
        }
    }
}
