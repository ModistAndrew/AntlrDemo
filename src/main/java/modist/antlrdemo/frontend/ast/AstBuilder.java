package modist.antlrdemo.frontend.ast;

import modist.antlrdemo.frontend.grammar.MxLexer;
import modist.antlrdemo.frontend.grammar.MxParser;
import modist.antlrdemo.frontend.grammar.MxVisitor;
import modist.antlrdemo.frontend.ast.node.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

// TODO: an AST printer to format the code
public class AstBuilder implements MxVisitor<Ast> {
    @Override
    public ProgramAst visitProgram(MxParser.ProgramContext ctx) {
        ProgramAst programNode = new ProgramAst();
        programNode.definitions = ctx.children.stream().map(this::visit)
                .flatMap(node -> switch (node) {
                    case StatementAst.VariableDefinitions variableDefinitions -> variableDefinitions.variables.stream();
                    case DefinitionAst definition -> Stream.of(definition);
                    default -> throw new ClassCastException();
                }).toList();
        programNode.classes = new ArrayList<>();
        programNode.functions = new ArrayList<>();
        programNode.variables = new ArrayList<>();
        programNode.definitions.forEach(definition -> {
            switch (definition) {
                case DefinitionAst.Class classNode -> programNode.classes.add(classNode);
                case DefinitionAst.Function functionNode -> programNode.functions.add(functionNode);
                case DefinitionAst.Variable variableNode -> programNode.variables.add(variableNode);
            }
        });
        return withPosition(programNode, ctx);
    }

    @Override
    public DefinitionAst.Class visitClassDefinition(MxParser.ClassDefinitionContext ctx) {
        DefinitionAst.Class classNode = withPosition(new DefinitionAst.Class(), ctx.Identifier());
        classNode.name = ctx.Identifier().getText();
        classNode.constructors = ctx.constructorDefinition().stream().map(this::visitConstructorDefinition).toList();
        classNode.variables = ctx.variableDefinitions().stream().map(this::visitVariableDefinitions)
                .flatMap(variableDefinitions -> variableDefinitions.variables.stream()).toList();
        classNode.functions = ctx.functionDefinition().stream().map(this::visitFunctionDefinition).toList();
        return classNode;
    }

    @Override
    public DefinitionAst.Function visitFunctionDefinition(MxParser.FunctionDefinitionContext ctx) {
        DefinitionAst.Function functionNode = withPosition(new DefinitionAst.Function(), ctx.Identifier());
        functionNode.name = ctx.Identifier().getText();
        functionNode.returnType = this.visitType(ctx.type());
        functionNode.parameters = ctx.parameterDefinition().stream().map(this::visitParameterDefinition).toList();
        functionNode.body = visitBlock(ctx.block()).statements;
        return functionNode;
    }

    @Override
    public DefinitionAst.Function visitConstructorDefinition(MxParser.ConstructorDefinitionContext ctx) {
        DefinitionAst.Function constructorNode = withPosition(new DefinitionAst.Function(), ctx.Identifier());
        constructorNode.name = ctx.Identifier().getText();
        constructorNode.returnType = withPosition(new TypeAst(), ctx.Identifier());
        constructorNode.returnType.typeName = TokenUtil.getLiteralName(MxLexer.VOID);
        constructorNode.returnType.dimension = 0; // set to void manually
        constructorNode.parameters = new ArrayList<>();
        constructorNode.body = visitBlock(ctx.block()).statements;
        return constructorNode;
    }

    @Override
    public DefinitionAst.Variable visitParameterDefinition(MxParser.ParameterDefinitionContext ctx) {
        DefinitionAst.Variable parameterNode = withPosition(new DefinitionAst.Variable(), ctx.Identifier());
        parameterNode.name = ctx.Identifier().getText();
        parameterNode.type = visitType(ctx.type());
        parameterNode.initializer = null;
        return parameterNode;
    }

    @Override
    public StatementAst.Block visitBlock(MxParser.BlockContext ctx) {
        StatementAst.Block blockNode = withPosition(new StatementAst.Block(), ctx);
        blockNode.statements = ctx.statement().stream().map(this::visitStatement).toList();
        return blockNode;
    }

    @Override
    public StatementAst.Block visitBlockStmt(MxParser.BlockStmtContext ctx) {
        return withPosition(visitBlock(ctx.block()), ctx);
    }

    @Override
    public StatementAst.VariableDefinitions visitVariableDefinitionsStmt(MxParser.VariableDefinitionsStmtContext ctx) {
        return withPosition(visitVariableDefinitions(ctx.variableDefinitions()), ctx);
    }

    @Override
    public StatementAst.If visitIfStmt(MxParser.IfStmtContext ctx) {
        StatementAst.If ifNode = withPosition(new StatementAst.If(), ctx);
        ifNode.condition = visitCondition(ctx.condition());
        ifNode.thenStatements = getStatementList(ctx.ifThenStmt);
        ifNode.elseStatements = ctx.ifElseStmt != null ? this.getStatementList(ctx.ifElseStmt) : null;
        return ifNode;
    }

    @Override
    public StatementAst.For visitForStmt(MxParser.ForStmtContext ctx) {
        StatementAst.For forNode = withPosition(new StatementAst.For(), ctx);
        forNode.initialization = ctx.forInit != null ? this.visitForInitialization(ctx.forInit) : null;
        forNode.condition = ctx.forCondition != null ? this.visitExpression(ctx.forCondition) : null;
        forNode.update = ctx.forUpdate != null ? this.visitExpression(ctx.forUpdate) : null;
        forNode.statements = getStatementList(ctx.statement());
        return forNode;
    }

    @Override
    public StatementAst.While visitWhileStmt(MxParser.WhileStmtContext ctx) {
        StatementAst.While whileNode = withPosition(new StatementAst.While(), ctx);
        whileNode.condition = visitCondition(ctx.condition());
        whileNode.statements = getStatementList(ctx.statement());
        return whileNode;
    }

    @Override
    public StatementAst.Break visitBreakStmt(MxParser.BreakStmtContext ctx) {
        return withPosition(new StatementAst.Break(), ctx);
    }

    @Override
    public StatementAst.Continue visitContinueStmt(MxParser.ContinueStmtContext ctx) {
        return withPosition(new StatementAst.Continue(), ctx);
    }

    @Override
    public StatementAst.Return visitReturnStmt(MxParser.ReturnStmtContext ctx) {
        StatementAst.Return returnNode = withPosition(new StatementAst.Return(), ctx);
        returnNode.expression = ctx.expressionOrArray() != null ? this.visitExpressionOrArray(ctx.expressionOrArray()) : null;
        return returnNode;
    }

    @Override
    public StatementAst.Expression visitExpressionStmt(MxParser.ExpressionStmtContext ctx) {
        StatementAst.Expression expressionNode = withPosition(new StatementAst.Expression(), ctx);
        expressionNode.expression = visitExpression(ctx.expression());
        return expressionNode;
    }

    @Override
    public StatementAst.Empty visitEmptyStmt(MxParser.EmptyStmtContext ctx) {
        return withPosition(new StatementAst.Empty(), ctx);
    }

    @Override
    public StatementAst.VariableDefinitions visitVariableDefinitionsBody(MxParser.VariableDefinitionsBodyContext ctx) {
        StatementAst.VariableDefinitions variableDefinitionsNode = withPosition(new StatementAst.VariableDefinitions(), ctx);
        variableDefinitionsNode.variables = ctx.variableDeclarator().stream().map(declarator -> {
            DefinitionAst.Variable variableNode = withPosition(new DefinitionAst.Variable(), declarator.Identifier());
            variableNode.name = declarator.Identifier().getText();
            variableNode.type = visitType(ctx.type());
            variableNode.initializer = declarator.expressionOrArray() != null ? this.visitExpressionOrArray(declarator.expressionOrArray()) : null;
            return variableNode;
        }).toList();
        return variableDefinitionsNode;
    }

    @Override
    public StatementAst.VariableDefinitions visitVariableDefinitions(MxParser.VariableDefinitionsContext ctx) {
        return withPosition(visitVariableDefinitionsBody(ctx.variableDefinitionsBody()), ctx);
    }

    @Override
    public Ast visitVariableDeclarator(MxParser.VariableDeclaratorContext ctx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ForInitializationAst visitForInitialization(MxParser.ForInitializationContext ctx) {
        return withPosition((ForInitializationAst) visit(ctx.getChild(0)), ctx);
    }

    @Override
    public ExpressionAst.PreUnaryAssign visitPreUnaryAssignExpr(MxParser.PreUnaryAssignExprContext ctx) {
        ExpressionAst.PreUnaryAssign preUnaryAssignNode = withPosition(new ExpressionAst.PreUnaryAssign(), ctx.op);
        preUnaryAssignNode.expression = visitExpression(ctx.expression());
        preUnaryAssignNode.operator = TokenUtil.getOperator(ctx.op);
        return preUnaryAssignNode;
    }

    @Override
    public ExpressionAst.This visitThisExpr(MxParser.ThisExprContext ctx) {
        return withPosition(new ExpressionAst.This(), ctx);
    }

    @Override
    public ExpressionAst.Subscript visitSubscriptExpr(MxParser.SubscriptExprContext ctx) {
        ExpressionAst.Subscript subscriptNode = withPosition(new ExpressionAst.Subscript(), ctx);
        subscriptNode.expression = visitExpression(ctx.expression());
        subscriptNode.index = visitExpressionBracketPair(ctx.expressionBracketPair());
        return subscriptNode;
    }

    @Override
    public ExpressionAst.Binary visitBinaryExpr(MxParser.BinaryExprContext ctx) {
        ExpressionAst.Binary binaryNode = withPosition(new ExpressionAst.Binary(), ctx.op);
        binaryNode.left = visitExpression(ctx.expression(0));
        binaryNode.right = visitExpression(ctx.expression(1));
        binaryNode.operator = TokenUtil.getOperator(ctx.op);
        return binaryNode;
    }

    @Override
    public ExpressionAst.PostUnaryAssign visitPostUnaryAssignExpr(MxParser.PostUnaryAssignExprContext ctx) {
        ExpressionAst.PostUnaryAssign postUnaryAssignNode = withPosition(new ExpressionAst.PostUnaryAssign(), ctx.op);
        postUnaryAssignNode.expression = visitExpression(ctx.expression());
        postUnaryAssignNode.operator = TokenUtil.getOperator(ctx.op);
        return postUnaryAssignNode;
    }

    @Override
    public ExpressionAst visitParenExpr(MxParser.ParenExprContext ctx) {
        return withPosition(visitExpression(ctx.expression()), ctx);
    }

    @Override
    public ExpressionAst.Function visitFunctionExpr(MxParser.FunctionExprContext ctx) {
        ExpressionAst.Function functionNode = withPosition(new ExpressionAst.Function(), ctx);
        functionNode.expression = ctx.expression() != null ? this.visitExpression(ctx.expression()) : null;
        functionNode.name = ctx.Identifier().getText();
        functionNode.arguments = getArgumentList(ctx.argumentList());
        return functionNode;
    }

    @Override
    public ExpressionAst.Variable visitVariableExpr(MxParser.VariableExprContext ctx) {
        ExpressionAst.Variable variableNode = withPosition(new ExpressionAst.Variable(), ctx);
        variableNode.expression = ctx.expression() != null ? this.visitExpression(ctx.expression()) : null;
        variableNode.name = ctx.Identifier().getText();
        return variableNode;
    }

    @Override
    public ExpressionAst.PreUnary visitPreUnaryExpr(MxParser.PreUnaryExprContext ctx) {
        ExpressionAst.PreUnary preUnaryNode = withPosition(new ExpressionAst.PreUnary(), ctx.op);
        preUnaryNode.expression = visitExpression(ctx.expression());
        preUnaryNode.operator = TokenUtil.getOperator(ctx.op);
        return preUnaryNode;
    }

    @Override
    public ExpressionAst.Literal visitLiteralExpr(MxParser.LiteralExprContext ctx) {
        return withPosition(visitLiteral(ctx.literal()), ctx);
    }

    @Override
    public ExpressionAst.FormatString visitFormatStringExpr(MxParser.FormatStringExprContext ctx) {
        return withPosition(visitFormatString(ctx.formatString()), ctx);
    }

    @Override
    public ExpressionAst.Creator visitCreatorExpr(MxParser.CreatorExprContext ctx) {
        ExpressionAst.Creator creatorNode = withPosition(new ExpressionAst.Creator(), ctx);
        creatorNode.typeName = ctx.typeName() != null ? ctx.typeName().getText() : ctx.Identifier().getText();
        creatorNode.arrayCreator = ctx.arrayCreator() != null ? this.visitArrayCreator(ctx.arrayCreator()) : null;
        return creatorNode;
    }

    @Override
    public ExpressionAst.Assign visitAssignExpr(MxParser.AssignExprContext ctx) {
        ExpressionAst.Assign assignNode = withPosition(new ExpressionAst.Assign(), ctx.ASSIGN());
        assignNode.left = visitExpression(ctx.expression());
        assignNode.right = visitExpressionOrArray(ctx.expressionOrArray());
        return assignNode;
    }

    @Override
    public ExpressionAst.Conditional visitConditionalExpr(MxParser.ConditionalExprContext ctx) {
        ExpressionAst.Conditional conditionalNode = withPosition(new ExpressionAst.Conditional(), ctx);
        conditionalNode.condition = visitExpression(ctx.expression(0));
        conditionalNode.trueExpression = visitExpression(ctx.expression(1));
        conditionalNode.falseExpression = visitExpression(ctx.expression(2));
        return conditionalNode;
    }

    @Override
    public ArrayAst visitArray(MxParser.ArrayContext ctx) {
        ArrayAst arrayNode = withPosition(new ArrayAst(), ctx);
        arrayNode.elements = ctx.expressionOrArray().stream().map(this::visitExpressionOrArray).toList();
        return arrayNode;
    }

    @Override
    public ExpressionAst.FormatString visitFormatString(MxParser.FormatStringContext ctx) {
        ExpressionAst.FormatString formatStringNode = withPosition(new ExpressionAst.FormatString(), ctx);
        formatStringNode.texts = ctx.formatStringText.stream().map(TokenUtil::unesacpeString).toList();
        formatStringNode.expressions = ctx.expression().stream().map(this::visitExpression).toList();
        return formatStringNode;
    }

    @Override
    public ExpressionAst.Literal visitLiteral(MxParser.LiteralContext ctx) {
        ExpressionAst.Literal literalNode = withPosition(new ExpressionAst.Literal(), ctx);
        literalNode.value = TokenUtil.getConstant(ctx.start);
        return literalNode;
    }

    @Override
    public Ast visitArgumentList(MxParser.ArgumentListContext ctx) {
        throw new UnsupportedOperationException();
    }

    public List<ExpressionOrArrayAst> getArgumentList(MxParser.ArgumentListContext ctx) {
        return ctx.expressionOrArray().stream().map(this::visitExpressionOrArray).toList();
    }

    @Override
    public ExpressionAst visitCondition(MxParser.ConditionContext ctx) {
        return withPosition(visitExpression(ctx.expression()), ctx);
    }

    @Override
    public TypeAst visitType(MxParser.TypeContext ctx) {
        TypeAst typeNode = withPosition(new TypeAst(), ctx);
        typeNode.typeName = ctx.typeName() != null ? ctx.typeName().getText() : ctx.VOID().getText();
        typeNode.dimension = ctx.emptyBracketPair() != null ? ctx.emptyBracketPair().size() : 0;
        return typeNode;
    }

    @Override
    public Ast visitTypeName(MxParser.TypeNameContext ctx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Ast visitEmptyBracketPair(MxParser.EmptyBracketPairContext ctx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExpressionAst visitExpressionBracketPair(MxParser.ExpressionBracketPairContext ctx) {
        return withPosition(visitExpression(ctx.expression()), ctx);
    }

    @Override
    @Nullable
    public ExpressionAst visitPossibleBracketPair(MxParser.PossibleBracketPairContext ctx) {
        return ctx.expression() != null ? withPosition(visitExpression(ctx.expression()), ctx) : null;
    }

    @Override
    public Ast visitEmptyParenthesisPair(MxParser.EmptyParenthesisPairContext ctx) {
        throw new UnsupportedOperationException();
    }

    // convenience method for double dispatch. should not call on self
    // use visitXXX methods for covariant return types
    @Override
    public Ast visit(ParseTree parseTree) {
        return parseTree.accept(this);
    }

    @Override
    public Ast visitChildren(RuleNode ruleNode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Ast visitTerminal(TerminalNode terminalNode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Ast visitErrorNode(ErrorNode errorNode) {
        throw new UnsupportedOperationException();
    }

    public StatementAst visitStatement(MxParser.StatementContext ctx) {
        return (StatementAst) visit(ctx);
    }

    public List<StatementAst> getStatementList(MxParser.StatementContext ctx) {
        return switch (visitStatement(ctx)) {
            case StatementAst.Block block -> block.statements;
            case StatementAst s -> List.of(s);
        };
    }

    public ExpressionAst visitExpression(MxParser.ExpressionContext ctx) {
        return (ExpressionAst) visit(ctx);
    }

    public ArrayCreatorAst visitArrayCreator(MxParser.ArrayCreatorContext ctx) {
        ArrayCreatorAst arrayCreatorNode = withPosition(new ArrayCreatorAst(), ctx);
        arrayCreatorNode.dimensions = ctx.possibleBracketPair().stream().map(this::visitPossibleBracketPair).toList();
        arrayCreatorNode.initializer = ctx.array() != null ? this.visitArray(ctx.array()) : null;
        return arrayCreatorNode;
    }

    @Override
    public ExpressionOrArrayAst visitExpressionOrArray(MxParser.ExpressionOrArrayContext ctx) {
        return withPosition((ExpressionOrArrayAst) visit(ctx.getChild(0)), ctx);
    }

    private <T extends Ast> T withPosition(T astNode, Token token) {
        astNode.setPosition(TokenUtil.getPosition(token));
        return astNode;
    }

    private <T extends Ast> T withPosition(T astNode, ParserRuleContext ctx) {
        return withPosition(astNode, ctx.getStart());
    }

    private <T extends Ast> T withPosition(T astNode, TerminalNode terminal) {
        return withPosition(astNode, terminal.getSymbol());
    }
}