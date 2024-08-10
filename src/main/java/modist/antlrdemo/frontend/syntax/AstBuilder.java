package modist.antlrdemo.frontend.syntax;

import modist.antlrdemo.frontend.syntax.node.*;
import modist.antlrdemo.frontend.grammar.MxParser;
import modist.antlrdemo.frontend.grammar.MxVisitor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;
import java.util.Optional;

public class AstBuilder implements MxVisitor<IAstNode> {
    @Override
    public ProgramNode visitProgram(MxParser.ProgramContext ctx) {
        ProgramNode programNode = withPosition(new ProgramNode(), ctx);
        programNode.classes = ctx.classDeclaration().stream().map(this::visitClassDeclaration).toList();
        programNode.variables = ctx.variableDeclarations().stream().map(this::extractVariableDeclarations).flatMap(List::stream).toList();
        programNode.functions = ctx.functionDeclaration().stream().map(this::visitFunctionDeclaration).toList();
        return programNode;
    }

    @Override
    public DeclarationNode.Class visitClassDeclaration(MxParser.ClassDeclarationContext ctx) {
        DeclarationNode.Class classNode = withPosition(new DeclarationNode.Class(), ctx.Identifier());
        classNode.name = ctx.Identifier().getText();
        MxParser.ClassDeclarationBodyContext body = ctx.classDeclarationBody();
        classNode.constructor = Optional.ofNullable(body.constructorDeclaration()).map(this::visitConstructorDeclaration);
        classNode.variables = body.variableDeclarations().stream().map(this::extractVariableDeclarations).flatMap(List::stream).toList();
        classNode.functions = body.functionDeclaration().stream().map(this::visitFunctionDeclaration).toList();
        return classNode;
    }

    @Override
    public IAstNode visitClassDeclarationBody(MxParser.ClassDeclarationBodyContext ctx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DeclarationNode.Function visitFunctionDeclaration(MxParser.FunctionDeclarationContext ctx) {
        DeclarationNode.Function functionNode = withPosition(new DeclarationNode.Function(), ctx.Identifier());
        functionNode.name = ctx.Identifier().getText();
        functionNode.returnType = Optional.ofNullable(ctx.type()).map(this::visitType);
        functionNode.parameters = ctx.parameterDeclaration().stream().map(this::visitParameterDeclaration).toList();
        functionNode.body = visitBlock(ctx.block());
        return functionNode;
    }

    @Override
    public DeclarationNode.Constructor visitConstructorDeclaration(MxParser.ConstructorDeclarationContext ctx) {
        DeclarationNode.Constructor constructorNode = withPosition(new DeclarationNode.Constructor(), ctx.Identifier());
        constructorNode.name = ctx.Identifier().getText();
        constructorNode.body = visitBlock(ctx.block());
        return constructorNode;
    }

    @Override
    public DeclarationNode.Parameter visitParameterDeclaration(MxParser.ParameterDeclarationContext ctx) {
        DeclarationNode.Parameter parameterNode = withPosition(new DeclarationNode.Parameter(), ctx.Identifier());
        parameterNode.name = ctx.Identifier().getText();
        parameterNode.type = visitType(ctx.type());
        return parameterNode;
    }

    @Override
    public StatementNode.Block visitBlock(MxParser.BlockContext ctx) {
        StatementNode.Block blockNode = withPosition(new StatementNode.Block(), ctx);
        blockNode.statements = ctx.statement().stream().map(this::visitStatement).toList();
        return blockNode;
    }

    @Override
    public StatementNode.Block visitBlockStmt(MxParser.BlockStmtContext ctx) {
        return withPosition(visitBlock(ctx.block()), ctx);
    }

    @Override
    public StatementNode.VariableDeclarations visitVariableDeclarationsStmt(MxParser.VariableDeclarationsStmtContext ctx) {
        StatementNode.VariableDeclarations variableDeclarationsNode = withPosition(new StatementNode.VariableDeclarations(), ctx);
        variableDeclarationsNode.variables = extractVariableDeclarations(ctx.variableDeclarations());
        return variableDeclarationsNode;
    }

    @Override
    public StatementNode.If visitIfStmt(MxParser.IfStmtContext ctx) {
        StatementNode.If ifNode = withPosition(new StatementNode.If(), ctx);
        ifNode.condition = visitCondition(ctx.condition());
        ifNode.thenStatement = visitStatement(ctx.ifThenStmt);
        ifNode.elseStatement = Optional.ofNullable(ctx.ifElseStmt).map(this::visitStatement);
        return ifNode;
    }

    @Override
    public StatementNode.For visitForStmt(MxParser.ForStmtContext ctx) {
        StatementNode.For forNode = withPosition(new StatementNode.For(), ctx);
        forNode.initialization = Optional.ofNullable(ctx.forInit).map(this::visitForInitialization);
        forNode.condition = Optional.ofNullable(ctx.forCondition).map(this::visitExpression);
        forNode.update = Optional.ofNullable(ctx.forUpdate).map(this::visitExpression);
        forNode.statement = visitStatement(ctx.statement());
        return forNode;
    }

    @Override
    public StatementNode.While visitWhileStmt(MxParser.WhileStmtContext ctx) {
        StatementNode.While whileNode = withPosition(new StatementNode.While(), ctx);
        whileNode.condition = visitCondition(ctx.condition());
        whileNode.statement = visitStatement(ctx.statement());
        return whileNode;
    }

    @Override
    public StatementNode.Break visitBreakStmt(MxParser.BreakStmtContext ctx) {
        return withPosition(new StatementNode.Break(), ctx);
    }

    @Override
    public StatementNode.Continue visitContinueStmt(MxParser.ContinueStmtContext ctx) {
        return withPosition(new StatementNode.Continue(), ctx);
    }

    @Override
    public StatementNode.Return visitReturnStmt(MxParser.ReturnStmtContext ctx) {
        StatementNode.Return returnNode = withPosition(new StatementNode.Return(), ctx);
        returnNode.expression = Optional.ofNullable(ctx.expression()).map(this::visitExpression);
        return returnNode;
    }

    @Override
    public StatementNode.Expression visitExpressionStmt(MxParser.ExpressionStmtContext ctx) {
        StatementNode.Expression expressionNode = withPosition(new StatementNode.Expression(), ctx);
        expressionNode.expression = visitExpression(ctx.expression());
        return expressionNode;
    }

    @Override
    public IAstNode visitVariableDeclarationsBody(MxParser.VariableDeclarationsBodyContext ctx) {
        throw new UnsupportedOperationException();
    }

    public List<DeclarationNode.Variable> extractVariableDeclarationsBody(MxParser.VariableDeclarationsBodyContext ctx) {
        return ctx.variableDeclarator().stream().map(declarator -> {
            DeclarationNode.Variable variableNode = withPosition(new DeclarationNode.Variable(), declarator.Identifier());
            variableNode.name = declarator.Identifier().getText();
            variableNode.type = visitType(ctx.type());
            variableNode.initializer = Optional.ofNullable(declarator.expression()).map(this::visitExpression);
            return variableNode;
        }).toList();
    }

    @Override
    public IAstNode visitVariableDeclarations(MxParser.VariableDeclarationsContext ctx) {
        throw new UnsupportedOperationException();
    }

    public List<DeclarationNode.Variable> extractVariableDeclarations(MxParser.VariableDeclarationsContext ctx) {
        return extractVariableDeclarationsBody(ctx.variableDeclarationsBody());
    }

    @Override
    public IAstNode visitVariableDeclarator(MxParser.VariableDeclaratorContext ctx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ForInitializationNode visitForInitialization(MxParser.ForInitializationContext ctx) {
        return withPosition((ForInitializationNode) visit(ctx.getChild(0)), ctx);
    }

    @Override
    public ExpressionNode.This visitThisExpr(MxParser.ThisExprContext ctx) {
        return withPosition(new ExpressionNode.This(), ctx);
    }

    @Override
    public ExpressionNode.PostUnary visitPostUnaryExpr(MxParser.PostUnaryExprContext ctx) {
        ExpressionNode.PostUnary postUnaryNode = withPosition(new ExpressionNode.PostUnary(), ctx.op);
        postUnaryNode.expression = visitExpression(ctx.expression());
        postUnaryNode.operator = TokenUtil.getPostUnaryOperator(ctx.op);
        return postUnaryNode;
    }

    @Override
    public ExpressionNode.Array visitArrayExpr(MxParser.ArrayExprContext ctx) {
        return withPosition(visitArray(ctx.array()), ctx);
    }

    @Override
    public ExpressionNode.Subscript visitSubscriptExpr(MxParser.SubscriptExprContext ctx) {
        ExpressionNode.Subscript subscriptNode = withPosition(new ExpressionNode.Subscript(), ctx);
        subscriptNode.expression = visitExpression(ctx.expression());
        subscriptNode.index = visitExpressionBracketPair(ctx.expressionBracketPair());
        return subscriptNode;
    }

    @Override
    public ExpressionNode.Binary visitBinaryExpr(MxParser.BinaryExprContext ctx) {
        ExpressionNode.Binary binaryNode = withPosition(new ExpressionNode.Binary(), ctx.op);
        binaryNode.leftExpression = visitExpression(ctx.expression(0));
        binaryNode.rightExpression = visitExpression(ctx.expression(1));
        binaryNode.operator = TokenUtil.getBinaryOperator(ctx.op);
        return binaryNode;
    }

    @Override
    public ExpressionNode visitParenExpr(MxParser.ParenExprContext ctx) {
        return withPosition(visitExpression(ctx.expression()), ctx);
    }

    @Override
    public ExpressionNode.Function visitFunctionExpr(MxParser.FunctionExprContext ctx) {
        ExpressionNode.Function functionNode = withPosition(new ExpressionNode.Function(), ctx);
        functionNode.expression = Optional.ofNullable(ctx.expression()).map(this::visitExpression);
        functionNode.name = ctx.Identifier().getText();
        functionNode.arguments = extractArgumentList(ctx.argumentList());
        return functionNode;
    }

    @Override
    public ExpressionNode.Variable visitVariableExpr(MxParser.VariableExprContext ctx) {
        ExpressionNode.Variable variableNode = withPosition(new ExpressionNode.Variable(), ctx);
        variableNode.expression = Optional.ofNullable(ctx.expression()).map(this::visitExpression);
        variableNode.name = ctx.Identifier().getText();
        return variableNode;
    }

    @Override
    public ExpressionNode.PreUnary visitPreUnaryExpr(MxParser.PreUnaryExprContext ctx) {
        ExpressionNode.PreUnary preUnaryNode = withPosition(new ExpressionNode.PreUnary(), ctx.op);
        preUnaryNode.expression = visitExpression(ctx.expression());
        preUnaryNode.operator = TokenUtil.getPreUnaryOperator(ctx.op);
        return preUnaryNode;
    }

    @Override
    public ExpressionNode.Literal visitLiteralExpr(MxParser.LiteralExprContext ctx) {
        return withPosition(visitLiteral(ctx.literal()), ctx);
    }

    @Override
    public ExpressionNode.FormatString visitFormatStringExpr(MxParser.FormatStringExprContext ctx) {
        return withPosition(visitFormatString(ctx.formatString()), ctx);
    }

    @Override
    public ExpressionNode.Creator visitCreatorExpr(MxParser.CreatorExprContext ctx) {
        ExpressionNode.Creator creatorNode = withPosition(new ExpressionNode.Creator(), ctx);
        creatorNode.typeName = extractTypeName(ctx.typeName());
        creatorNode.arrayCreator = Optional.ofNullable(ctx.arrayCreator()).map(this::visitArrayCreator);
        return creatorNode;
    }

    @Override
    public ExpressionNode.Assign visitAssignExpr(MxParser.AssignExprContext ctx) {
        ExpressionNode.Assign assignNode = withPosition(new ExpressionNode.Assign(), ctx.op);
        assignNode.leftExpression = visitExpression(ctx.expression(0));
        assignNode.rightExpression = visitExpression(ctx.expression(1));
        assignNode.operator = TokenUtil.getAssignOperator(ctx.op);
        return assignNode;
    }

    @Override
    public ExpressionNode.Conditional visitConditionalExpr(MxParser.ConditionalExprContext ctx) {
        ExpressionNode.Conditional conditionalNode = withPosition(new ExpressionNode.Conditional(), ctx);
        conditionalNode.condition = visitExpression(ctx.expression(0));
        conditionalNode.trueExpression = visitExpression(ctx.expression(1));
        conditionalNode.falseExpression = visitExpression(ctx.expression(2));
        return conditionalNode;
    }

    @Override
    public ArrayCreatorNode.Empty visitEmptyArrayCreator(MxParser.EmptyArrayCreatorContext ctx) {
        ArrayCreatorNode.Empty emptyArrayCreatorNode = withPosition(new ArrayCreatorNode.Empty(), ctx);
        emptyArrayCreatorNode.dimensionLengths = ctx.expressionBracketPair().stream().map(this::visitExpressionBracketPair).toList();
        emptyArrayCreatorNode.emptyDimension = ctx.emptyBracketPair().size();
        return emptyArrayCreatorNode;
    }

    @Override
    public ArrayCreatorNode.Init visitInitArrayCreator(MxParser.InitArrayCreatorContext ctx) {
        ArrayCreatorNode.Init initArrayCreatorNode = withPosition(new ArrayCreatorNode.Init(), ctx);
        initArrayCreatorNode.dimension = ctx.emptyBracketPair().size();
        initArrayCreatorNode.initializer = visitArray(ctx.array());
        return initArrayCreatorNode;
    }

    @Override
    public ExpressionNode.Array visitArray(MxParser.ArrayContext ctx) {
        ExpressionNode.Array arrayNode = withPosition(new ExpressionNode.Array(), ctx);
        arrayNode.elements = ctx.expression().stream().map(this::visitExpression).toList();
        return arrayNode;
    }

    @Override
    public ExpressionNode.FormatString visitFormatString(MxParser.FormatStringContext ctx) {
        ExpressionNode.FormatString formatStringNode = withPosition(new ExpressionNode.FormatString(), ctx);
        formatStringNode.texts = ctx.formatStringText.stream().map(TokenUtil::unesacpeString).toList();
        formatStringNode.expressions = ctx.expression().stream().map(this::visitExpression).toList();
        return formatStringNode;
    }

    @Override
    public ExpressionNode.Literal visitLiteral(MxParser.LiteralContext ctx) {
        ExpressionNode.Literal literalNode = withPosition(new ExpressionNode.Literal(), ctx);
        literalNode.value = TokenUtil.getLiteralEnum(ctx.start);
        return literalNode;
    }

    @Override
    public IAstNode visitArgumentList(MxParser.ArgumentListContext ctx) {
        throw new UnsupportedOperationException();
    }

    public List<ExpressionNode> extractArgumentList(MxParser.ArgumentListContext ctx) {
        return ctx.expression().stream().map(this::visitExpression).toList();
    }

    @Override
    public ExpressionNode visitCondition(MxParser.ConditionContext ctx) {
        return withPosition(visitExpression(ctx.expression()), ctx);
    }

    @Override
    public TypeNode visitType(MxParser.TypeContext ctx) {
        TypeNode typeNode = withPosition(new TypeNode(), ctx);
        typeNode.typeName = extractTypeName(ctx.typeName());
        typeNode.dimension = ctx.emptyBracketPair().size();
        return typeNode;
    }

    @Override
    public IAstNode visitTypeName(MxParser.TypeNameContext ctx) {
        throw new UnsupportedOperationException();
    }

    public TypeNode.TypeNameEnum extractTypeName(MxParser.TypeNameContext ctx) {
        return TokenUtil.getTypeNameEnum(ctx.start);
    }

    @Override
    public IAstNode visitEmptyBracketPair(MxParser.EmptyBracketPairContext ctx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExpressionNode visitExpressionBracketPair(MxParser.ExpressionBracketPairContext ctx) {
        return withPosition(visitExpression(ctx.expression()), ctx);
    }

    @Override
    public IAstNode visitEmptyParenthesisPair(MxParser.EmptyParenthesisPairContext ctx) {
        throw new UnsupportedOperationException();
    }

    // convenience method for double dispatch. should not call on self
    // use visitXXX methods for covariant return types
    @Override
    public IAstNode visit(ParseTree parseTree) {
        return parseTree.accept(this);
    }

    @Override
    public IAstNode visitChildren(RuleNode ruleNode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IAstNode visitTerminal(TerminalNode terminalNode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IAstNode visitErrorNode(ErrorNode errorNode) {
        throw new UnsupportedOperationException();
    }

    public StatementNode visitStatement(MxParser.StatementContext ctx) {
        return (StatementNode) visit(ctx);
    }

    public ExpressionNode visitExpression(MxParser.ExpressionContext ctx) {
        return (ExpressionNode) visit(ctx);
    }

    public ArrayCreatorNode visitArrayCreator(MxParser.ArrayCreatorContext ctx) {
        return (ArrayCreatorNode) visit(ctx);
    }

    private <T extends IAstNode> T withPosition(T astNode, Token token) {
        astNode.setPosition(TokenUtil.getPosition(token));
        return astNode;
    }

    private <T extends IAstNode> T withPosition(T astNode, ParserRuleContext ctx) {
        return withPosition(astNode, ctx.getStart());
    }

    private <T extends IAstNode> T withPosition(T astNode, TerminalNode terminal) {
        return withPosition(astNode, terminal.getSymbol());
    }
}