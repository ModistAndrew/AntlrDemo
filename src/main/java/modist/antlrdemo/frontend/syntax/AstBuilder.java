package modist.antlrdemo.frontend.syntax;

import modist.antlrdemo.frontend.error.SemanticException;
import modist.antlrdemo.frontend.syntax.node.*;
import modist.antlrdemo.frontend.grammar.MxParser;
import modist.antlrdemo.frontend.grammar.MxVisitor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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
        classNode.constructor = null;
        ctx.constructorDeclaration().stream().map(this::visitConstructorDeclaration).forEach(constructor -> {
            if (classNode.constructor != null) {
                throw new SemanticException("Multiple constructors in class " + classNode.name, constructor.position);
            }
            classNode.constructor = constructor;
        });
        classNode.variables = ctx.variableDeclarations().stream().map(this::extractVariableDeclarations).flatMap(List::stream).toList();
        classNode.functions = ctx.functionDeclaration().stream().map(this::visitFunctionDeclaration).toList();
        return classNode;
    }

    @Override
    public DeclarationNode.Function visitFunctionDeclaration(MxParser.FunctionDeclarationContext ctx) {
        DeclarationNode.Function functionNode = withPosition(new DeclarationNode.Function(), ctx.Identifier());
        functionNode.name = ctx.Identifier().getText();
        functionNode.returnType = ctx.type() != null ? this.visitType(ctx.type()) : null;
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
        ifNode.elseStatement = ctx.ifElseStmt != null ? this.visitStatement(ctx.ifElseStmt) : null;
        return ifNode;
    }

    @Override
    public StatementNode.For visitForStmt(MxParser.ForStmtContext ctx) {
        StatementNode.For forNode = withPosition(new StatementNode.For(), ctx);
        forNode.initialization = ctx.forInit != null ? this.visitForInitialization(ctx.forInit) : null;
        forNode.condition = ctx.forCondition != null ? this.visitExpression(ctx.forCondition) : null;
        forNode.update = ctx.forUpdate != null ? this.visitExpression(ctx.forUpdate) : null;
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
        returnNode.expression = ctx.expression() != null ? this.visitExpression(ctx.expression()) : null;
        return returnNode;
    }

    @Override
    public StatementNode.Expression visitExpressionStmt(MxParser.ExpressionStmtContext ctx) {
        StatementNode.Expression expressionNode = withPosition(new StatementNode.Expression(), ctx);
        expressionNode.expression = visitExpression(ctx.expression());
        return expressionNode;
    }

    @Override
    public StatementNode.Empty visitEmptyStmt(MxParser.EmptyStmtContext ctx) {
        return withPosition(new StatementNode.Empty(), ctx);
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
            variableNode.initializer = declarator.expression() != null ? this.visitExpression(declarator.expression()) : null;
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
    public ExpressionNode.PreUnaryAssign visitPreUnaryAssignExpr(MxParser.PreUnaryAssignExprContext ctx) {
        ExpressionNode.PreUnaryAssign preUnaryAssignNode = withPosition(new ExpressionNode.PreUnaryAssign(), ctx.op);
        preUnaryAssignNode.expression = visitExpression(ctx.expression());
        preUnaryAssignNode.operator = TokenUtil.getOperator(ctx.op);
        return preUnaryAssignNode;
    }

    @Override
    public ExpressionNode.This visitThisExpr(MxParser.ThisExprContext ctx) {
        return withPosition(new ExpressionNode.This(), ctx);
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
        binaryNode.operator = TokenUtil.getOperator(ctx.op);
        return binaryNode;
    }

    @Override
    public ExpressionNode.PostUnaryAssign visitPostUnaryAssignExpr(MxParser.PostUnaryAssignExprContext ctx) {
        ExpressionNode.PostUnaryAssign postUnaryAssignNode = withPosition(new ExpressionNode.PostUnaryAssign(), ctx.op);
        postUnaryAssignNode.expression = visitExpression(ctx.expression());
        postUnaryAssignNode.operator = TokenUtil.getOperator(ctx.op);
        return postUnaryAssignNode;
    }

    @Override
    public ExpressionNode visitParenExpr(MxParser.ParenExprContext ctx) {
        return withPosition(visitExpression(ctx.expression()), ctx);
    }

    @Override
    public ExpressionNode.Function visitFunctionExpr(MxParser.FunctionExprContext ctx) {
        ExpressionNode.Function functionNode = withPosition(new ExpressionNode.Function(), ctx);
        functionNode.expression = ctx.expression() != null ? this.visitExpression(ctx.expression()) : null;
        functionNode.name = ctx.Identifier().getText();
        functionNode.arguments = extractArgumentList(ctx.argumentList());
        return functionNode;
    }

    @Override
    public ExpressionNode.Variable visitVariableExpr(MxParser.VariableExprContext ctx) {
        ExpressionNode.Variable variableNode = withPosition(new ExpressionNode.Variable(), ctx);
        variableNode.expression = ctx.expression() != null ? this.visitExpression(ctx.expression()) : null;
        variableNode.name = ctx.Identifier().getText();
        return variableNode;
    }

    @Override
    public ExpressionNode.PreUnary visitPreUnaryExpr(MxParser.PreUnaryExprContext ctx) {
        ExpressionNode.PreUnary preUnaryNode = withPosition(new ExpressionNode.PreUnary(), ctx.op);
        preUnaryNode.expression = visitExpression(ctx.expression());
        preUnaryNode.operator = TokenUtil.getOperator(ctx.op);
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
        creatorNode.typeName = ctx.typeName.getText();
        creatorNode.arrayCreator = ctx.arrayCreator() != null ? this.visitArrayCreator(ctx.arrayCreator()) : null;
        return creatorNode;
    }

    @Override
    public ExpressionNode.Assign visitAssignExpr(MxParser.AssignExprContext ctx) {
        ExpressionNode.Assign assignNode = withPosition(new ExpressionNode.Assign(), ctx.ASSIGN());
        assignNode.leftExpression = visitExpression(ctx.expression(0));
        assignNode.rightExpression = visitExpression(ctx.expression(1));
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
        typeNode.typeName = ctx.typeName.getText();
        typeNode.dimension = ctx.emptyBracketPair().size();
        return typeNode;
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
    @Nullable
    public ExpressionNode visitPossibleBracketPair(MxParser.PossibleBracketPairContext ctx) {
        return ctx.expression() != null ? withPosition(visitExpression(ctx.expression()), ctx) : null;
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
        ExpressionNode.Array initializer = ctx.array() != null ? visitArray(ctx.array()) : null;
        boolean acceptExpression = initializer == null;
        List<ExpressionNode> dimensionLengths = new ArrayList<>();
        for (MxParser.PossibleBracketPairContext pair : ctx.possibleBracketPair()) {
            ExpressionNode expression = visitPossibleBracketPair(pair);
            if (expression == null) {
                acceptExpression = false;
            } else if (acceptExpression) {
                dimensionLengths.add(expression);
            } else {
                throw new SemanticException("Invalid expression bracket pair in array creator", expression.position);
            }
        }
        if (initializer == null) {
            ArrayCreatorNode.Empty arrayCreatorNode = withPosition(new ArrayCreatorNode.Empty(), ctx);
            arrayCreatorNode.dimensionLengths = dimensionLengths;
            arrayCreatorNode.emptyDimension = ctx.possibleBracketPair().size() - dimensionLengths.size();
            return arrayCreatorNode;
        } else {
            ArrayCreatorNode.Init arrayCreatorNode = withPosition(new ArrayCreatorNode.Init(), ctx);
            arrayCreatorNode.initializer = initializer;
            arrayCreatorNode.dimension = ctx.possibleBracketPair().size();
            return arrayCreatorNode;
        }
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