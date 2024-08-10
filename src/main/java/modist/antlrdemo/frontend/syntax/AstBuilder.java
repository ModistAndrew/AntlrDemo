package modist.antlrdemo.frontend.syntax;

import modist.antlrdemo.frontend.syntax.node.*;
import modist.antlrdemo.frontend.grammar.MxParser;
import modist.antlrdemo.frontend.grammar.MxVisitor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class AstBuilder implements MxVisitor<IAstNode> {
    @Override
    public ProgramNode visitProgram(MxParser.ProgramContext ctx) {
        ProgramNode program = withPosition(new ProgramNode(), ctx);
        program.classes = ctx.classDeclaration().stream().map(this::visitClassDeclaration).toList();
        program.variables = ctx.variableDeclaration().stream().map(this::visitVariableDeclaration).toList();
        program.functions = ctx.functionDeclaration().stream().map(this::visitFunctionDeclaration).toList();
        return program;
    }

    @Override
    public ClassDeclarationNode visitClassDeclaration(MxParser.ClassDeclarationContext ctx) {
        ClassDeclarationNode classDeclaration = withPosition(new ClassDeclarationNode(), ctx);
        classDeclaration.name = ctx.Identifier().getText();
        classDeclaration.constructor = ctx.constructorDeclaration() != null ? visitConstructorDeclaration(ctx.constructorDeclaration()) : null;
        classDeclaration.functions = ctx.functionDeclaration().stream().map(this::visitFunctionDeclaration).toList();
        classDeclaration.variables = ctx.variableDeclaration().stream().map(this::visitVariableDeclaration).toList();
        return classDeclaration;
    }

    @Override
    public FunctionDeclarationNode visitFunctionDeclaration(MxParser.FunctionDeclarationContext ctx) {
        FunctionDeclarationNode functionDeclaration = withPosition(new FunctionDeclarationNode(), ctx);
        functionDeclaration.name = ctx.Identifier().getText();
        functionDeclaration.returnType = ctx.type() != null ? visitType(ctx.type()) : null;
        functionDeclaration.parameters = ctx.parameterDeclaration().stream().map(this::visitParameterDeclaration).toList();
        functionDeclaration.body = visitBlock(ctx.block());
        return functionDeclaration;
    }

    @Override
    public ConstructorDeclarationNode visitConstructorDeclaration(MxParser.ConstructorDeclarationContext ctx) {
        ConstructorDeclarationNode constructorDeclaration = withPosition(new ConstructorDeclarationNode(), ctx);
        constructorDeclaration.name = ctx.Identifier().getText();
        constructorDeclaration.body = visitBlock(ctx.block());
        return constructorDeclaration;
    }

    @Override
    public ParameterDeclarationNode visitParameterDeclaration(MxParser.ParameterDeclarationContext ctx) {
        ParameterDeclarationNode parameterDeclaration = withPosition(new ParameterDeclarationNode(), ctx);
        parameterDeclaration.name = ctx.Identifier().getText();
        parameterDeclaration.type = visitType(ctx.type());
        return parameterDeclaration;
    }

    @Override
    public BlockNode visitBlock(MxParser.BlockContext ctx) {
        BlockNode block = withPosition(new BlockNode(), ctx);
        block.statements = ctx.statement().stream().map(this::visitStatement).toList();
        return block;
    }

    @Override
    public StatementNode.Block visitBlockStmt(MxParser.BlockStmtContext ctx) {
        StatementNode.Block blockStatement = withPosition(new StatementNode.Block(), ctx);
        blockStatement.block = visitBlock(ctx.block());
        return blockStatement;
    }

    @Override
    public StatementNode.VariableDeclaration visitVariableDeclarationStmt(MxParser.VariableDeclarationStmtContext ctx) {
        StatementNode.VariableDeclaration variableDeclarationStatement = withPosition(new StatementNode.VariableDeclaration(), ctx);
        variableDeclarationStatement.variableDeclaration = visitVariableDeclaration(ctx.variableDeclaration());
        return variableDeclarationStatement;
    }

    @Override
    public StatementNode.If visitIfStmt(MxParser.IfStmtContext ctx) {
        StatementNode.If ifStatement = withPosition(new StatementNode.If(), ctx);
        ifStatement.condition = visitCondition(ctx.condition());
        ifStatement.thenStatement = visitStatement(ctx.ifThenStmt);
        ifStatement.elseStatement = ctx.ifElseStmt != null ? visitStatement(ctx.ifElseStmt) : null;
        return ifStatement;
    }

    @Override
    public StatementNode.For visitForStmt(MxParser.ForStmtContext ctx) {
        StatementNode.For forStatement = withPosition(new StatementNode.For(), ctx);
        forStatement.initialization = ctx.forInit != null ? visitForInitialization(ctx.forInit) : null;
        forStatement.condition = ctx.forCondition != null ? visitExpression(ctx.forCondition) : null;
        forStatement.update = ctx.forUpdate != null ? visitExpression(ctx.forUpdate) : null;
        forStatement.statement = visitStatement(ctx.statement());
        return forStatement;
    }

    @Override
    public StatementNode.While visitWhileStmt(MxParser.WhileStmtContext ctx) {
        StatementNode.While whileStatement = withPosition(new StatementNode.While(), ctx);
        whileStatement.condition = visitCondition(ctx.condition());
        whileStatement.statement = visitStatement(ctx.statement());
        return whileStatement;
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
        StatementNode.Return returnStatement = withPosition(new StatementNode.Return(), ctx);
        returnStatement.expression = ctx.expression() != null ? visitExpression(ctx.expression()) : null;
        return returnStatement;
    }

    @Override
    public StatementNode.Expression visitExpressionStmt(MxParser.ExpressionStmtContext ctx) {
        StatementNode.Expression expressionStatement = withPosition(new StatementNode.Expression(), ctx);
        expressionStatement.expression = visitExpression(ctx.expression());
        return expressionStatement;
    }

    @Override
    public VariableDeclarationNode visitVariableDeclarationBody(MxParser.VariableDeclarationBodyContext ctx) {
        VariableDeclarationNode variableDeclaration = withPosition(new VariableDeclarationNode(), ctx);
        variableDeclaration.type = visitType(ctx.type());
        variableDeclaration.declarators = ctx.variableDeclarator().stream().map(this::visitVariableDeclarator).toList();
        return variableDeclaration;
    }

    @Override
    public VariableDeclarationNode visitVariableDeclaration(MxParser.VariableDeclarationContext ctx) {
        return visitVariableDeclarationBody(ctx.variableDeclarationBody());
    }

    @Override
    public VariableDeclaratorNode visitVariableDeclarator(MxParser.VariableDeclaratorContext ctx) {
        VariableDeclaratorNode variableDeclarator = withPosition(new VariableDeclaratorNode(), ctx);
        variableDeclarator.name = ctx.Identifier().getText();
        variableDeclarator.initializer = ctx.variableInitializer() != null ? visitVariableInitializer(ctx.variableInitializer()) : null;
        return variableDeclarator;
    }

    @Override
    public VariableInitializerNode visitVariableInitializer(MxParser.VariableInitializerContext ctx) {
        return (VariableInitializerNode) visitChildren(ctx);
    }

    @Override
    public ForInitializationNode visitForInitialization(MxParser.ForInitializationContext ctx) {
        return (ForInitializationNode) visitChildren(ctx);
    }

    @Override
    public ExpressionNode.New visitNewExpr(MxParser.NewExprContext ctx) {
        ExpressionNode.New newExpression = withPosition(new ExpressionNode.New(), ctx);
        newExpression.creator = visitCreator(ctx.creator());
        return newExpression;
    }

    @Override
    public ExpressionNode.This visitThisExpr(MxParser.ThisExprContext ctx) {
        return withPosition(new ExpressionNode.This(), ctx);
    }

    @Override
    public ExpressionNode.PostUnary visitPostUnaryExpr(MxParser.PostUnaryExprContext ctx) {
        ExpressionNode.PostUnary postUnaryExpression = withPosition(new ExpressionNode.PostUnary(), ctx);
        postUnaryExpression.expression = visitExpression(ctx.expression());
        postUnaryExpression.operator = TokenUtil.getPostUnaryOperator(ctx.op);
        return postUnaryExpression;
    }

    @Override
    public ExpressionNode.Binary visitBinaryExpr(MxParser.BinaryExprContext ctx) {
        ExpressionNode.Binary binaryExpression = withPosition(new ExpressionNode.Binary(), ctx);
        binaryExpression.leftExpression = visitExpression(ctx.expression(0));
        binaryExpression.rightExpression = visitExpression(ctx.expression(1));
        binaryExpression.operator = TokenUtil.getBinaryOperator(ctx.op);
        return binaryExpression;
    }

    @Override
    public ExpressionNode.Paren visitParenExpr(MxParser.ParenExprContext ctx) {
        ExpressionNode.Paren parenExpression = withPosition(new ExpressionNode.Paren(), ctx);
        parenExpression.expression = visitExpression(ctx.expression());
        return parenExpression;
    }

    @Override
    public ExpressionNode.Function visitFunctionExpr(MxParser.FunctionExprContext ctx) {
        ExpressionNode.Function functionExpression = withPosition(new ExpressionNode.Function(), ctx);
        functionExpression.expression = ctx.expression() != null ? visitExpression(ctx.expression()) : null;
        functionExpression.name = ctx.Identifier().getText();
        functionExpression.arguments = visitArgumentList(ctx.argumentList());
        return functionExpression;
    }

    @Override
    public ExpressionNode.Variable visitVariableExpr(MxParser.VariableExprContext ctx) {
        ExpressionNode.Variable variableExpression = withPosition(new ExpressionNode.Variable(), ctx);
        variableExpression.expression = ctx.expression() != null ? visitExpression(ctx.expression()) : null;
        variableExpression.name = ctx.Identifier().getText();
        return variableExpression;
    }

    @Override
    public ExpressionNode.PreUnary visitPreUnaryExpr(MxParser.PreUnaryExprContext ctx) {
        ExpressionNode.PreUnary preUnaryExpression = withPosition(new ExpressionNode.PreUnary(), ctx);
        preUnaryExpression.expression = visitExpression(ctx.expression());
        preUnaryExpression.operator = TokenUtil.getPreUnaryOperator(ctx.op);
        return preUnaryExpression;
    }

    @Override
    public ExpressionNode.ArrayAccess visitArrayAccessExpr(MxParser.ArrayAccessExprContext ctx) {
        ExpressionNode.ArrayAccess arrayAccessExpression = withPosition(new ExpressionNode.ArrayAccess(), ctx);
        arrayAccessExpression.expression = visitExpression(ctx.expression());
        arrayAccessExpression.index = visitExpressionBracketPair(ctx.expressionBracketPair());
        return arrayAccessExpression;
    }

    @Override
    public ExpressionNode.Literal visitLiteralExpr(MxParser.LiteralExprContext ctx) {
        ExpressionNode.Literal literalExpression = withPosition(new ExpressionNode.Literal(), ctx);
        literalExpression.value = TokenUtil.getLiteralEnum(ctx.literal);
        return literalExpression;
    }

    @Override
    public ExpressionNode.FormatString visitFormatStringExpr(MxParser.FormatStringExprContext ctx) {
        ExpressionNode.FormatString formatStringExpression = withPosition(new ExpressionNode.FormatString(), ctx);
        formatStringExpression.formatString = visitFormatString(ctx.formatString());
        return formatStringExpression;
    }

    @Override
    public ExpressionNode.Assign visitAssignExpr(MxParser.AssignExprContext ctx) {
        ExpressionNode.Assign assignExpression = withPosition(new ExpressionNode.Assign(), ctx);
        assignExpression.leftExpression = visitExpression(ctx.expression(0));
        assignExpression.rightExpression = visitExpression(ctx.expression(1));
        assignExpression.operator = TokenUtil.getAssignOperator(ctx.op);
        return assignExpression;
    }

    @Override
    public ExpressionNode.Conditional visitConditionalExpr(MxParser.ConditionalExprContext ctx) {
        ExpressionNode.Conditional conditionalExpression = withPosition(new ExpressionNode.Conditional(), ctx);
        conditionalExpression.condition = visitExpression(ctx.expression(0));
        conditionalExpression.trueExpression = visitExpression(ctx.expression(1));
        conditionalExpression.falseExpression = visitExpression(ctx.expression(2));
        return conditionalExpression;
    }

    @Override
    public CreatorNode visitCreator(MxParser.CreatorContext ctx) {
        CreatorNode creator = withPosition(new CreatorNode(), ctx);
        creator.typeName = visitTypeName(ctx.typeName());
        creator.arrayCreator = ctx.arrayCreator() != null ? visitArrayCreator(ctx.arrayCreator()) : null;
        return creator;
    }

    @Override
    public ArrayCreatorNode.Literal visitLiteralArrayCreator(MxParser.LiteralArrayCreatorContext ctx) {
        ArrayCreatorNode.Literal literalArrayCreator = withPosition(new ArrayCreatorNode.Literal(), ctx);
        literalArrayCreator.dimension = ctx.emptyBracketPair().size();
        literalArrayCreator.initializer = visitArrayInitializer(ctx.arrayInitializer());
        return literalArrayCreator;
    }

    @Override
    public ArrayCreatorNode.Empty visitEmptyArrayCreator(MxParser.EmptyArrayCreatorContext ctx) {
        ArrayCreatorNode.Empty emptyArrayCreator = withPosition(new ArrayCreatorNode.Empty(), ctx);
        emptyArrayCreator.dimensionLengths = ctx.expressionBracketPair().stream().map(this::visitExpressionBracketPair).toList();
        emptyArrayCreator.emptyDimension = ctx.emptyBracketPair().size();
        return emptyArrayCreator;
    }

    @Override
    public ArrayInitializerNode visitArrayInitializer(MxParser.ArrayInitializerContext ctx) {
        ArrayInitializerNode arrayInitializer = withPosition(new ArrayInitializerNode(), ctx);
        arrayInitializer.variableInitializers = ctx.variableInitializer().stream().map(this::visitVariableInitializer).toList();
        return arrayInitializer;
    }

    @Override
    public ArgumentListNode visitArgumentList(MxParser.ArgumentListContext ctx) {
        ArgumentListNode argumentList = withPosition(new ArgumentListNode(), ctx);
        argumentList.arguments = ctx.expression().stream().map(this::visitExpression).toList();
        return argumentList;
    }

    @Override
    public ExpressionNode visitCondition(MxParser.ConditionContext ctx) {
        return visitExpression(ctx.expression());
    }

    @Override
    public TypeNode visitType(MxParser.TypeContext ctx) {
        TypeNode type = withPosition(new TypeNode(), ctx);
        type.typeName = visitTypeName(ctx.typeName());
        type.dimension = ctx.emptyBracketPair().size();
        return type;
    }

    @Override
    public TypeNameNode visitTypeName(MxParser.TypeNameContext ctx) {
        TypeNameNode typeName = withPosition(new TypeNameNode(), ctx);
        typeName.value = TokenUtil.getTypeNameEnum(ctx.typeNameToken);
        return typeName;
    }

    // unused
    @Override
    public IAstNode visitEmptyBracketPair(MxParser.EmptyBracketPairContext ctx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExpressionNode visitExpressionBracketPair(MxParser.ExpressionBracketPairContext ctx) {
        return visitExpression(ctx.expression());
    }

    @Override
    public IAstNode visitEmptyParenthesisPair(MxParser.EmptyParenthesisPairContext ctx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FormatStringNode visitFormatString(MxParser.FormatStringContext ctx) {
        FormatStringNode formatString = withPosition(new FormatStringNode(), ctx);
        formatString.texts = ctx.formatStringToken.stream().map(TokenUtil::unesacpeString).toList();
        formatString.expressions = ctx.expression().stream().map(this::visitExpression).toList();
        return formatString;
    }

    // convenience method for double dispatch. should not call on self
    // use visitXXX methods for covariant return types
    @Override
    public IAstNode visit(ParseTree parseTree) {
        return parseTree.accept(this);
    }

    // simply visit the only child. for rule set
    @Override
    public IAstNode visitChildren(RuleNode ruleNode) {
        if (ruleNode.getChildCount() != 1) {
            throw new IllegalArgumentException("RuleNode is not a single child node, but " + ruleNode.getChildCount());
        }
        return visit(ruleNode.getChild(0));
    }

    // unused. terminal nodes are dealt directly in visitXXX methods
    @Override
    public IAstNode visitTerminal(TerminalNode terminalNode) {
        throw new UnsupportedOperationException();
    }

    // unused
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

    private <T extends IAstNode> T withPosition(T astNode, ParserRuleContext ctx) {
        astNode.setPosition(TokenUtil.getPosition(ctx.getStart()));
        return astNode;
    }
}