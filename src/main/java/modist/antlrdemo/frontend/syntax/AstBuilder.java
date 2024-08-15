package modist.antlrdemo.frontend.syntax;

import modist.antlrdemo.frontend.error.*;
import modist.antlrdemo.frontend.grammar.MxLexer;
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
import java.util.stream.Stream;

public class AstBuilder implements MxVisitor<IAstNode> {
    @Override
    public ProgramNode visitProgram(MxParser.ProgramContext ctx) {
        ProgramNode programNode = withPosition(new ProgramNode(), ctx);
        programNode.classes = ctx.classDeclaration().stream().map(this::visitClassDeclaration).toList();
        programNode.functions = ctx.functionDeclaration().stream().map(this::visitFunctionDeclaration).toList();
        programNode.declarations = ctx.children.stream().map(this::visit)
                .flatMap(node -> switch (node) {
                    case StatementNode.VariableDeclarations declarations -> declarations.variables.stream();
                    case DeclarationNode declaration -> Stream.of(declaration);
                    default -> throw new ClassCastException();
                }).toList();
        return programNode;
    }

    @Override
    public DeclarationNode.Class visitClassDeclaration(MxParser.ClassDeclarationContext ctx) {
        DeclarationNode.Class classNode = withPosition(new DeclarationNode.Class(), ctx.Identifier());
        classNode.name = ctx.Identifier().getText();
        classNode.constructor = null;
        ctx.constructorDeclaration().stream().map(this::visitConstructorDeclaration).forEach(constructor -> {
            if (classNode.constructor != null) {
                throw new SymbolRedefinedException("Constructor", constructor.position, classNode.constructor.position);
            }
            classNode.constructor = constructor;
        });
        classNode.variables = ctx.variableDeclarations().stream().map(this::visitVariableDeclarations)
                .flatMap(variableDeclarations -> variableDeclarations.variables.stream()).toList();
        classNode.functions = ctx.functionDeclaration().stream().map(this::visitFunctionDeclaration).toList();
        return classNode;
    }

    @Override
    public DeclarationNode.Function visitFunctionDeclaration(MxParser.FunctionDeclarationContext ctx) {
        DeclarationNode.Function functionNode = withPosition(new DeclarationNode.Function(), ctx.Identifier());
        functionNode.name = ctx.Identifier().getText();
        functionNode.returnType = this.visitType(ctx.type());
        functionNode.parameters = ctx.parameterDeclaration().stream().map(this::visitParameterDeclaration).toList();
        functionNode.body = visitBlock(ctx.block()).statements;
        return functionNode;
    }

    @Override
    public DeclarationNode.Function visitConstructorDeclaration(MxParser.ConstructorDeclarationContext ctx) {
        DeclarationNode.Function constructorNode = withPosition(new DeclarationNode.Function(), ctx.Identifier());
        constructorNode.name = ctx.Identifier().getText();
        constructorNode.returnType = withPosition(new TypeNode(), ctx.Identifier());
        constructorNode.returnType.typeName = TokenUtil.getLiteralName(MxLexer.VOID);
        constructorNode.returnType.dimension = 0; // set to void manually
        constructorNode.parameters = new ArrayList<>();
        constructorNode.body = visitBlock(ctx.block()).statements;
        return constructorNode;
    }

    @Override
    public DeclarationNode.Variable visitParameterDeclaration(MxParser.ParameterDeclarationContext ctx) {
        DeclarationNode.Variable parameterNode = withPosition(new DeclarationNode.Variable(), ctx.Identifier());
        parameterNode.name = ctx.Identifier().getText();
        parameterNode.type = visitType(ctx.type());
        parameterNode.initializer = null;
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
        return withPosition(visitVariableDeclarations(ctx.variableDeclarations()), ctx);
    }

    @Override
    public StatementNode.If visitIfStmt(MxParser.IfStmtContext ctx) {
        StatementNode.If ifNode = withPosition(new StatementNode.If(), ctx);
        ifNode.condition = visitCondition(ctx.condition());
        ifNode.thenStatements = getStatementList(ctx.ifThenStmt);
        ifNode.elseStatements = ctx.ifElseStmt != null ? this.getStatementList(ctx.ifElseStmt) : null;
        return ifNode;
    }

    @Override
    public StatementNode.For visitForStmt(MxParser.ForStmtContext ctx) {
        StatementNode.For forNode = withPosition(new StatementNode.For(), ctx);
        forNode.initialization = ctx.forInit != null ? this.visitForInitialization(ctx.forInit) : null;
        forNode.condition = ctx.forCondition != null ? this.visitExpression(ctx.forCondition) : null;
        forNode.update = ctx.forUpdate != null ? this.visitExpression(ctx.forUpdate) : null;
        forNode.statements = getStatementList(ctx.statement());
        return forNode;
    }

    @Override
    public StatementNode.While visitWhileStmt(MxParser.WhileStmtContext ctx) {
        StatementNode.While whileNode = withPosition(new StatementNode.While(), ctx);
        whileNode.condition = visitCondition(ctx.condition());
        whileNode.statements = getStatementList(ctx.statement());
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
        returnNode.expression = ctx.expressionOrArray() != null ? this.visitExpressionOrArray(ctx.expressionOrArray()) : null;
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
    public StatementNode.VariableDeclarations visitVariableDeclarationsBody(MxParser.VariableDeclarationsBodyContext ctx) {
        StatementNode.VariableDeclarations variableDeclarationsNode = withPosition(new StatementNode.VariableDeclarations(), ctx);
        variableDeclarationsNode.variables = ctx.variableDeclarator().stream().map(declarator -> {
            DeclarationNode.Variable variableNode = withPosition(new DeclarationNode.Variable(), declarator.Identifier());
            variableNode.name = declarator.Identifier().getText();
            variableNode.type = visitType(ctx.type());
            variableNode.initializer = declarator.expressionOrArray() != null ? this.visitExpressionOrArray(declarator.expressionOrArray()) : null;
            return variableNode;
        }).toList();
        return variableDeclarationsNode;
    }

    @Override
    public StatementNode.VariableDeclarations visitVariableDeclarations(MxParser.VariableDeclarationsContext ctx) {
        return withPosition(visitVariableDeclarationsBody(ctx.variableDeclarationsBody()), ctx);
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
        functionNode.arguments = getArgumentList(ctx.argumentList());
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
        creatorNode.typeName = ctx.typeName() != null ? ctx.typeName().getText() : ctx.Identifier().getText();
        creatorNode.arrayCreator = ctx.arrayCreator() != null ? this.visitArrayCreator(ctx.arrayCreator()) : null;
        return creatorNode;
    }

    @Override
    public ExpressionNode.Assign visitAssignExpr(MxParser.AssignExprContext ctx) {
        ExpressionNode.Assign assignNode = withPosition(new ExpressionNode.Assign(), ctx.ASSIGN());
        assignNode.leftExpression = visitExpression(ctx.expression());
        assignNode.rightExpression = visitExpressionOrArray(ctx.expressionOrArray());
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
    public ArrayNode visitArray(MxParser.ArrayContext ctx) {
        ArrayNode arrayNode = withPosition(new ArrayNode(), ctx);
        arrayNode.elements = ctx.expressionOrArray().stream().map(this::visitExpressionOrArray).toList();
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

    public List<ExpressionOrArrayNode> getArgumentList(MxParser.ArgumentListContext ctx) {
        return ctx.expressionOrArray().stream().map(this::visitExpressionOrArray).toList();
    }

    @Override
    public ExpressionNode visitCondition(MxParser.ConditionContext ctx) {
        return withPosition(visitExpression(ctx.expression()), ctx);
    }

    @Override
    public TypeNode visitType(MxParser.TypeContext ctx) {
        TypeNode typeNode = withPosition(new TypeNode(), ctx);
        typeNode.typeName = ctx.typeName() != null ? ctx.typeName().getText() : ctx.VOID().getText();
        typeNode.dimension = ctx.emptyBracketPair() != null ? ctx.emptyBracketPair().size() : 0;
        return typeNode;
    }

    @Override
    public IAstNode visitTypeName(MxParser.TypeNameContext ctx) {
        throw new UnsupportedOperationException();
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

    public List<StatementNode> getStatementList(MxParser.StatementContext ctx) {
        return switch (visitStatement(ctx)) {
            case StatementNode.Block block -> block.statements;
            case StatementNode s -> List.of(s);
        };
    }

    public ExpressionNode visitExpression(MxParser.ExpressionContext ctx) {
        return (ExpressionNode) visit(ctx);
    }

    public ArrayCreatorNode visitArrayCreator(MxParser.ArrayCreatorContext ctx) {
        ArrayNode initializer = ctx.array() != null ? visitArray(ctx.array()) : null;
        boolean acceptExpression = initializer == null;
        List<ExpressionNode> dimensionLengths = new ArrayList<>();
        for (MxParser.PossibleBracketPairContext pair : ctx.possibleBracketPair()) {
            ExpressionNode expression = visitPossibleBracketPair(pair);
            if (expression == null) {
                acceptExpression = false;
            } else if (acceptExpression) {
                dimensionLengths.add(expression);
            } else {
                throw new CompileException("Invalid expression bracket pair in array creator", expression.position);
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

    @Override
    public ExpressionOrArrayNode visitExpressionOrArray(MxParser.ExpressionOrArrayContext ctx) {
        return withPosition((ExpressionOrArrayNode) visit(ctx.getChild(0)), ctx);
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