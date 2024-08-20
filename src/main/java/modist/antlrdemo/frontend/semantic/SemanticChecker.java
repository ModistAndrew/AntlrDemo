package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.error.*;
import modist.antlrdemo.frontend.semantic.scope.ChildScope;
import modist.antlrdemo.frontend.semantic.scope.GlobalScope;
import modist.antlrdemo.frontend.semantic.scope.Scope;
import modist.antlrdemo.frontend.ast.node.*;
import org.jetbrains.annotations.Nullable;

// check the semantic correctness of the AST
// also store data for ir building
public class SemanticChecker {
    private Scope scope;
    private final SymbolRenamer renamer = new SymbolRenamer();
    private boolean returned;

    private void pushScope(Scope newScope) {
        scope = newScope;
    }

    private void popScope() {
        scope = scope.getParent();
    }

    public void check(@Nullable Ast node) {
        if (node == null) {
            return;
        }
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
                classDefinition.variables.forEach(variable -> {
                    if (variable.initializer != null) {
                        throw new CompileException("class variable cannot have initializer", variable.position);
                    }
                });
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
                check(forStatement.initialization);
                if (forStatement.condition != null) {
                    check(forStatement.condition);
                    forStatement.condition.type.testType(BuiltinFeatures.BOOL);
                }
                check(forStatement.update);
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
            case StatementAst.Break ignored -> {
                if (!scope.inLoop) {
                    throw new InvalidControlFlowException("break statement not in loop");
                }
            }
            case StatementAst.Continue ignored -> {
                if (!scope.inLoop) {
                    throw new InvalidControlFlowException("continue statement not in loop");
                }
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
