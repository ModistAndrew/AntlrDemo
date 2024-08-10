package modist.antlrdemo.frontend.syntax;

import modist.antlrdemo.frontend.syntax.node.*;
import modist.antlrdemo.frontend.grammar.MxParser;
import modist.antlrdemo.frontend.grammar.MxVisitor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

public class AstBuilder implements MxVisitor<IAstNode> {
    @Override
    public ProgramNode visitProgram(MxParser.ProgramContext ctx) {
        return null;
    }

    @Override
    public DeclarationNode.Class visitClassDeclaration(MxParser.ClassDeclarationContext ctx) {
        return null;
    }

    @Override
    public IAstNode visitClassDeclarationBody(MxParser.ClassDeclarationBodyContext ctx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DeclarationNode.Function visitFunctionDeclaration(MxParser.FunctionDeclarationContext ctx) {
        return null;
    }

    @Override
    public DeclarationNode.Constructor visitConstructorDeclaration(MxParser.ConstructorDeclarationContext ctx) {
        return null;
    }

    @Override
    public DeclarationNode.Parameter visitParameterDeclaration(MxParser.ParameterDeclarationContext ctx) {
        return null;
    }

    @Override
    public StatementNode.Block visitBlock(MxParser.BlockContext ctx) {
        return null;
    }

    @Override
    public StatementNode.Block visitBlockStmt(MxParser.BlockStmtContext ctx) {
        return visitBlock(ctx.block());
    }

    @Override
    public StatementNode.VariableDeclarations visitVariableDeclarationsStmt(MxParser.VariableDeclarationsStmtContext ctx) {
        return null;
    }

    @Override
    public StatementNode.If visitIfStmt(MxParser.IfStmtContext ctx) {
        return null;
    }

    @Override
    public StatementNode.For visitForStmt(MxParser.ForStmtContext ctx) {
        return null;
    }

    @Override
    public StatementNode.While visitWhileStmt(MxParser.WhileStmtContext ctx) {
        return null;
    }

    @Override
    public StatementNode.Break visitBreakStmt(MxParser.BreakStmtContext ctx) {
        return null;
    }

    @Override
    public StatementNode.Continue visitContinueStmt(MxParser.ContinueStmtContext ctx) {
        return null;
    }

    @Override
    public StatementNode.Return visitReturnStmt(MxParser.ReturnStmtContext ctx) {
        return null;
    }

    @Override
    public StatementNode.Expression visitExpressionStmt(MxParser.ExpressionStmtContext ctx) {
        return null;
    }

    @Override
    public IAstNode visitVariableDeclarationsBody(MxParser.VariableDeclarationsBodyContext ctx) {
        throw new UnsupportedOperationException();
    }

    public List<DeclarationNode.Variable> extractVariableDeclarationsBody(MxParser.VariableDeclarationsBodyContext ctx) {
        return null;
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
        return null;
    }

    @Override
    public ExpressionNode.This visitThisExpr(MxParser.ThisExprContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode.PostUnary visitPostUnaryExpr(MxParser.PostUnaryExprContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode.Array visitArrayExpr(MxParser.ArrayExprContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode.Subscript visitSubscriptExpr(MxParser.SubscriptExprContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode.Binary visitBinaryExpr(MxParser.BinaryExprContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode visitParenExpr(MxParser.ParenExprContext ctx) {
        return visitExpression(ctx.expression());
    }

    @Override
    public ExpressionNode.Function visitFunctionExpr(MxParser.FunctionExprContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode.Variable visitVariableExpr(MxParser.VariableExprContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode.PreUnary visitPreUnaryExpr(MxParser.PreUnaryExprContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode.Literal visitLiteralExpr(MxParser.LiteralExprContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode.FormatString visitFormatStringExpr(MxParser.FormatStringExprContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode.Creator visitCreatorExpr(MxParser.CreatorExprContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode.Assign visitAssignExpr(MxParser.AssignExprContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode.Conditional visitConditionalExpr(MxParser.ConditionalExprContext ctx) {
        return null;
    }

    @Override
    public ArrayCreatorNode.Empty visitEmptyArrayCreator(MxParser.EmptyArrayCreatorContext ctx) {
        return null;
    }

    @Override
    public ArrayCreatorNode.Init visitInitArrayCreator(MxParser.InitArrayCreatorContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode.Array visitArray(MxParser.ArrayContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode.FormatString visitFormatString(MxParser.FormatStringContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode.Literal visitLiteral(MxParser.LiteralContext ctx) {
        return null;
    }

    @Override
    public IAstNode visitArgumentList(MxParser.ArgumentListContext ctx) {
        throw new UnsupportedOperationException();
    }

    public List<ExpressionNode> extractArgumentList(MxParser.ArgumentListContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode visitCondition(MxParser.ConditionContext ctx) {
        return visitExpression(ctx.expression());
    }

    @Override
    public TypeNode visitType(MxParser.TypeContext ctx) {
        return null;
    }

    @Override
    public IAstNode visitTypeName(MxParser.TypeNameContext ctx) {
        throw new UnsupportedOperationException();
    }

    public TypeNode.TypeEnum extractTypeName(MxParser.TypeNameContext ctx) {
        return null;
    }

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