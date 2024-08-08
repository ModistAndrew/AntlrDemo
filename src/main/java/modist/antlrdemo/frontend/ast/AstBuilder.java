package modist.antlrdemo.frontend.ast;

import modist.antlrdemo.frontend.ast.metadata.Position;
import modist.antlrdemo.frontend.ast.node.*;
import modist.antlrdemo.frontend.grammar.MxParser;
import modist.antlrdemo.frontend.grammar.MxVisitor;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class AstBuilder implements MxVisitor<AstNode> {
    @Override
    public ProgramNode visitProgram(MxParser.ProgramContext ctx) {
        ProgramNode program = new ProgramNode(new Position(ctx.getStart()));
        program.classes = ctx.classDeclaration().stream().map(this::visitClassDeclaration).toList();
        program.variables = ctx.variableDeclaration().stream().map(this::visitVariableDeclaration).toList();
        program.functions = ctx.functionDeclaration().stream().map(this::visitFunctionDeclaration).toList();
        return program;
    }

    @Override
    public ClassDeclarationNode visitClassDeclaration(MxParser.ClassDeclarationContext ctx) {
        ClassDeclarationNode classDeclaration = new ClassDeclarationNode(new Position(ctx.getStart()), ctx.Identifier().getText());
        classDeclaration.constructor = ctx.constructorDeclaration() != null ? visitConstructorDeclaration(ctx.constructorDeclaration()) : null;
        classDeclaration.functions = ctx.functionDeclaration().stream().map(this::visitFunctionDeclaration).toList();
        classDeclaration.variables = ctx.variableDeclaration().stream().map(this::visitVariableDeclaration).toList();
        return classDeclaration;
    }

    @Override
    public FunctionDeclarationNode visitFunctionDeclaration(MxParser.FunctionDeclarationContext ctx) {
        FunctionDeclarationNode functionDeclaration = new FunctionDeclarationNode(new Position(ctx.getStart()), ctx.Identifier().getText());
        functionDeclaration.returnType = ctx.type() != null ? visitType(ctx.type()) : null;
        functionDeclaration.parameters = ctx.parameterDeclaration().stream().map(this::visitParameterDeclaration).toList();
        functionDeclaration.body = visitBlock(ctx.block());
        return functionDeclaration;
    }

    @Override
    public ConstructorDeclarationNode visitConstructorDeclaration(MxParser.ConstructorDeclarationContext ctx) {
        ConstructorDeclarationNode constructorDeclaration = new ConstructorDeclarationNode(new Position(ctx.getStart()), ctx.Identifier().getText());
        constructorDeclaration.body = visitBlock(ctx.block());
        return constructorDeclaration;
    }

    @Override
    public ParameterDeclarationNode visitParameterDeclaration(MxParser.ParameterDeclarationContext ctx) {
        ParameterDeclarationNode parameterDeclaration = new ParameterDeclarationNode(new Position(ctx.getStart()), ctx.Identifier().getText());
        parameterDeclaration.type = visitType(ctx.type());
        return parameterDeclaration;
    }

    @Override
    public BlockNode visitBlock(MxParser.BlockContext ctx) {
        BlockNode block = new BlockNode(new Position(ctx.getStart()));
        block.statements = ctx.statement().stream().map(this::visitStatement).toList();
        return block;
    }

    @Override
    public StatementNode.Block visitBlockStmt(MxParser.BlockStmtContext ctx) {
        StatementNode.Block blockStatement = new StatementNode.Block(new Position(ctx.getStart()));
        blockStatement.block = visitBlock(ctx.block());
        return blockStatement;
    }

    @Override
    public StatementNode.VariableDeclaration visitVariableDeclarationStmt(MxParser.VariableDeclarationStmtContext ctx) {
        StatementNode.VariableDeclaration variableDeclarationStatement = new StatementNode.VariableDeclaration(new Position(ctx.getStart()));
        variableDeclarationStatement.variableDeclaration = visitVariableDeclaration(ctx.variableDeclaration());
        return variableDeclarationStatement;
    }

    @Override
    public StatementNode.If visitIfStmt(MxParser.IfStmtContext ctx) {
        StatementNode.If ifStatement = new StatementNode.If(new Position(ctx.getStart()));
        ifStatement.condition = visitCondition(ctx.condition());
        ifStatement.thenStatement = visitStatement(ctx.ifThenStmt);
        ifStatement.elseStatement = ctx.ifElseStmt != null ? visitStatement(ctx.ifElseStmt) : null;
        return ifStatement;
    }

    @Override
    public StatementNode.For visitForStmt(MxParser.ForStmtContext ctx) {
        StatementNode.For forStatement = new StatementNode.For(new Position(ctx.getStart()));
        forStatement.initialization = ctx.forInit != null ? visitForInitialization(ctx.forInit) : null;
        forStatement.condition = ctx.forCondition != null ? visitExpression(ctx.forCondition) : null;
        forStatement.update = ctx.forUpdate != null ? visitExpression(ctx.forUpdate) : null;
        forStatement.statement = visitStatement(ctx.statement());
        return forStatement;
    }

    @Override
    public StatementNode.While visitWhileStmt(MxParser.WhileStmtContext ctx) {
        StatementNode.While whileStatement = new StatementNode.While(new Position(ctx.getStart()));
        whileStatement.condition = visitCondition(ctx.condition());
        whileStatement.statement = visitStatement(ctx.statement());
        return whileStatement;
    }

    @Override
    public StatementNode.Break visitBreakStmt(MxParser.BreakStmtContext ctx) {
        return new StatementNode.Break(new Position(ctx.getStart()));
    }

    @Override
    public StatementNode.Continue visitContinueStmt(MxParser.ContinueStmtContext ctx) {
        return new StatementNode.Continue(new Position(ctx.getStart()));
    }

    @Override
    public StatementNode.Return visitReturnStmt(MxParser.ReturnStmtContext ctx) {
        StatementNode.Return returnStatement = new StatementNode.Return(new Position(ctx.getStart()));
        returnStatement.expression = ctx.expression() != null ? visitExpression(ctx.expression()) : null;
        return returnStatement;
    }

    @Override
    public StatementNode.Expression visitExpressionStmt(MxParser.ExpressionStmtContext ctx) {
        StatementNode.Expression expressionStatement = new StatementNode.Expression(new Position(ctx.getStart()));
        expressionStatement.expression = visitExpression(ctx.expression());
        return expressionStatement;
    }

    @Override
    public VariableDeclarationNode visitVariableDeclarationBody(MxParser.VariableDeclarationBodyContext ctx) {
        VariableDeclarationNode variableDeclaration = new VariableDeclarationNode(new Position(ctx.getStart()));
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
        VariableDeclaratorNode variableDeclarator = new VariableDeclaratorNode(new Position(ctx.getStart()), ctx.Identifier().getText());
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
        ExpressionNode.New newExpression = new ExpressionNode.New(new Position(ctx.getStart()));
        newExpression.creator = visitCreator(ctx.creator());
        return newExpression;
    }

    @Override
    public ExpressionNode.This visitThisExpr(MxParser.ThisExprContext ctx) {
        return new ExpressionNode.This(new Position(ctx.getStart()));
    }

    @Override
    public ExpressionNode.PostUnary visitPostUnaryExpr(MxParser.PostUnaryExprContext ctx) {
        ExpressionNode.PostUnary postUnaryExpression = new ExpressionNode.PostUnary(new Position(ctx.getStart()));
        postUnaryExpression.expression = visitExpression(ctx.expression());
        postUnaryExpression.operator = TokenUtil.getPostUnaryOperator(ctx.op);
        return postUnaryExpression;
    }

    @Override
    public ExpressionNode.Binary visitBinaryExpr(MxParser.BinaryExprContext ctx) {
        ExpressionNode.Binary binaryExpression = new ExpressionNode.Binary(new Position(ctx.getStart()));
        binaryExpression.leftExpression = visitExpression(ctx.expression(0));
        binaryExpression.rightExpression = visitExpression(ctx.expression(1));
        binaryExpression.operator = TokenUtil.getBinaryOperator(ctx.op);
        return binaryExpression;
    }

    @Override
    public ExpressionNode.Paren visitParenExpr(MxParser.ParenExprContext ctx) {
        ExpressionNode.Paren parenExpression = new ExpressionNode.Paren(new Position(ctx.getStart()));
        parenExpression.expression = visitExpression(ctx.expression());
        return parenExpression;
    }

    @Override
    public ExpressionNode.PreUnary visitPreUnaryExpr(MxParser.PreUnaryExprContext ctx) {
        ExpressionNode.PreUnary preUnaryExpression = new ExpressionNode.PreUnary(new Position(ctx.getStart()));
        preUnaryExpression.expression = visitExpression(ctx.expression());
        preUnaryExpression.operator = TokenUtil.getPreUnaryOperator(ctx.op);
        return preUnaryExpression;
    }

    @Override
    public ExpressionNode.ArrayAccess visitArrayAccessExpr(MxParser.ArrayAccessExprContext ctx) {
        ExpressionNode.ArrayAccess arrayAccessExpression = new ExpressionNode.ArrayAccess(new Position(ctx.getStart()));
        arrayAccessExpression.expression = visitExpression(ctx.expression());
        arrayAccessExpression.index = visitExpressionBracketPair(ctx.expressionBracketPair());
        return arrayAccessExpression;
    }

    @Override
    public ExpressionNode.Literal visitLiteralExpr(MxParser.LiteralExprContext ctx) {
        ExpressionNode.Literal literalExpression = new ExpressionNode.Literal(new Position(ctx.getStart()));
        literalExpression.value = TokenUtil.getLiteralEnum(ctx.literal);
        return literalExpression;
    }

    @Override
    public ExpressionNode.FunctionCall visitFunctionCallExpr(MxParser.FunctionCallExprContext ctx) {
        ExpressionNode.FunctionCall functionCallExpression = new ExpressionNode.FunctionCall(new Position(ctx.getStart()));
        functionCallExpression.expression = visitExpression(ctx.expression());
        functionCallExpression.arguments = visitArgumentList(ctx.argumentList());
        return functionCallExpression;
    }

    @Override
    public ExpressionNode.MemberAccess visitMemberAccessExpr(MxParser.MemberAccessExprContext ctx) {
        ExpressionNode.MemberAccess memberAccessExpression = new ExpressionNode.MemberAccess(new Position(ctx.getStart()));
        memberAccessExpression.expression = visitExpression(ctx.expression());
        memberAccessExpression.member = ctx.Identifier().getText();
        return memberAccessExpression;
    }

    @Override
    public ExpressionNode.FormatString visitFormatStringExpr(MxParser.FormatStringExprContext ctx) {
        ExpressionNode.FormatString formatStringExpression = new ExpressionNode.FormatString(new Position(ctx.getStart()));
        formatStringExpression.formatString = visitFormatString(ctx.formatString());
        return formatStringExpression;
    }

    @Override
    public ExpressionNode.Assign visitAssignExpr(MxParser.AssignExprContext ctx) {
        ExpressionNode.Assign assignExpression = new ExpressionNode.Assign(new Position(ctx.getStart()));
        assignExpression.leftExpression = visitExpression(ctx.expression(0));
        assignExpression.rightExpression = visitExpression(ctx.expression(1));
        assignExpression.operator = TokenUtil.getAssignOperator(ctx.op);
        return assignExpression;
    }

    @Override
    public ExpressionNode.Conditional visitConditionalExpr(MxParser.ConditionalExprContext ctx) {
        ExpressionNode.Conditional conditionalExpression = new ExpressionNode.Conditional(new Position(ctx.getStart()));
        conditionalExpression.condition = visitExpression(ctx.expression(0));
        conditionalExpression.trueExpression = visitExpression(ctx.expression(1));
        conditionalExpression.falseExpression = visitExpression(ctx.expression(2));
        return conditionalExpression;
    }

    @Override
    public ExpressionNode.Identifier visitIdentifierExpr(MxParser.IdentifierExprContext ctx) {
        ExpressionNode.Identifier identifierExpression = new ExpressionNode.Identifier(new Position(ctx.getStart()));
        identifierExpression.name = ctx.Identifier().getText();
        return identifierExpression;
    }

    @Override
    public CreatorNode visitCreator(MxParser.CreatorContext ctx) {
        CreatorNode creator = new CreatorNode(new Position(ctx.getStart()));
        creator.typeName = visitTypeName(ctx.typeName());
        creator.arrayCreator = ctx.arrayCreator() != null ? visitArrayCreator(ctx.arrayCreator()) : null;
        return creator;
    }

    @Override
    public ArrayCreatorNode.Literal visitLiteralArrayCreator(MxParser.LiteralArrayCreatorContext ctx) {
        ArrayCreatorNode.Literal literalArrayCreator = new ArrayCreatorNode.Literal(new Position(ctx.getStart()));
        literalArrayCreator.dimension = ctx.emptyBracketPair().size();
        literalArrayCreator.initializer = visitArrayInitializer(ctx.arrayInitializer());
        return literalArrayCreator;
    }

    @Override
    public ArrayCreatorNode.Empty visitEmptyArrayCreator(MxParser.EmptyArrayCreatorContext ctx) {
        ArrayCreatorNode.Empty emptyArrayCreator = new ArrayCreatorNode.Empty(new Position(ctx.getStart()));
        emptyArrayCreator.initializedLengths = ctx.expressionBracketPair().stream().map(this::visitExpressionBracketPair).toList();
        emptyArrayCreator.emptyDimension = ctx.emptyBracketPair().size();
        return emptyArrayCreator;
    }

    @Override
    public ArrayInitializerNode visitArrayInitializer(MxParser.ArrayInitializerContext ctx) {
        ArrayInitializerNode arrayInitializer = new ArrayInitializerNode(new Position(ctx.getStart()));
        arrayInitializer.variableInitializers = ctx.variableInitializer().stream().map(this::visitVariableInitializer).toList();
        return arrayInitializer;
    }

    @Override
    public ArgumentListNode visitArgumentList(MxParser.ArgumentListContext ctx) {
        ArgumentListNode argumentList = new ArgumentListNode(new Position(ctx.getStart()));
        argumentList.arguments = ctx.expression().stream().map(this::visitExpression).toList();
        return argumentList;
    }

    @Override
    public ExpressionNode visitCondition(MxParser.ConditionContext ctx) {
        return visitExpression(ctx.expression());
    }

    @Override
    public TypeNode visitType(MxParser.TypeContext ctx) {
        TypeNode type = new TypeNode(new Position(ctx.getStart()));
        type.typeName = visitTypeName(ctx.typeName());
        type.dimension = ctx.emptyBracketPair().size();
        return type;
    }

    @Override
    public TypeNameNode visitTypeName(MxParser.TypeNameContext ctx) {
        TypeNameNode typeName = new TypeNameNode(new Position(ctx.getStart()));
        typeName.value = TokenUtil.getTypeNameEnum(ctx.typeNameToken);
        return typeName;
    }

    // unused
    @Override
    public AstNode visitEmptyBracketPair(MxParser.EmptyBracketPairContext ctx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExpressionNode visitExpressionBracketPair(MxParser.ExpressionBracketPairContext ctx) {
        return visitExpression(ctx.expression());
    }

    @Override
    public AstNode visitEmptyParenthesisPair(MxParser.EmptyParenthesisPairContext ctx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FormatStringNode visitFormatString(MxParser.FormatStringContext ctx) {
        FormatStringNode formatString = new FormatStringNode(new Position(ctx.getStart()));
        formatString.texts = ctx.formatStringToken.stream().map(TokenUtil::unesacpeString).toList();
        formatString.expressions = ctx.expression().stream().map(this::visitExpression).toList();
        return formatString;
    }

    // convenience method for double dispatch. should not call on self
    // use visitXXX methods for covariant return types
    @Override
    public AstNode visit(ParseTree parseTree) {
        return parseTree.accept(this);
    }

    // simply visit the only child. for rule set
    @Override
    public AstNode visitChildren(RuleNode ruleNode) {
        if (ruleNode.getChildCount() != 1) {
            throw new IllegalArgumentException("RuleNode is not a single child node, but " + ruleNode.getChildCount());
        }
        return visit(ruleNode.getChild(0));
    }

    // unused. terminal nodes are dealt directly in visitXXX methods
    @Override
    public AstNode visitTerminal(TerminalNode terminalNode) {
        throw new UnsupportedOperationException();
    }

    // unused
    @Override
    public AstNode visitErrorNode(ErrorNode errorNode) {
        throw new UnsupportedOperationException();
    }

    protected StatementNode visitStatement(MxParser.StatementContext ctx) {
        return (StatementNode) visit(ctx);
    }

    protected ExpressionNode visitExpression(MxParser.ExpressionContext ctx) {
        return (ExpressionNode) visit(ctx);
    }

    protected ArrayCreatorNode visitArrayCreator(MxParser.ArrayCreatorContext ctx) {
        return (ArrayCreatorNode) visit(ctx);
    }
}