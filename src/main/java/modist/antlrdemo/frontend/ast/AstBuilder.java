package modist.antlrdemo.frontend.ast;

import modist.antlrdemo.frontend.ast.node.*;
import modist.antlrdemo.frontend.grammar.MxParser;
import modist.antlrdemo.frontend.grammar.MxVisitor;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.Nullable;

public class AstBuilder implements MxVisitor<AstNode> {
    @Override
    public ProgramNode visitProgram(MxParser.ProgramContext ctx) {
        return null;
    }

    @Override
    public ClassDeclarationNode visitClassDeclaration(MxParser.ClassDeclarationContext ctx) {
        return null;
    }

    @Override
    public FunctionDeclarationNode visitFunctionDeclaration(MxParser.FunctionDeclarationContext ctx) {
        return null;
    }

    @Override
    public ConstructorDeclarationNode visitConstructorDeclaration(MxParser.ConstructorDeclarationContext ctx) {
        return null;
    }

    @Override
    public FormalParameterListNode visitFormalParameterList(MxParser.FormalParameterListContext ctx) {
        return null;
    }

    @Override
    public FormalParameterNode visitFormalParameter(MxParser.FormalParameterContext ctx) {
        return null;
    }

    @Override
    public BlockNode visitBlock(MxParser.BlockContext ctx) {
        return null;
    }

    @Override
    public StatementNode.Block visitBlockStmt(MxParser.BlockStmtContext ctx) {
        return null;
    }

    @Override
    public StatementNode.VariableDeclaration visitVariableDeclarationStmt(MxParser.VariableDeclarationStmtContext ctx) {
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
    public StatementNode.Empty visitEmptyStmt(MxParser.EmptyStmtContext ctx) {
        return null;
    }

    @Override
    public VariableDeclarationNode visitVariableDeclarationBody(MxParser.VariableDeclarationBodyContext ctx) {
        return null;
    }

    @Override
    public VariableDeclarationNode visitVariableDeclaration(MxParser.VariableDeclarationContext ctx) {
        return visitVariableDeclarationBody(ctx.variableDeclarationBody());
    }

    @Override
    public VariableDeclaratorNode visitVariableDeclarator(MxParser.VariableDeclaratorContext ctx) {
        return null;
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
    public ExpressionNode.Binary visitBinaryExpr(MxParser.BinaryExprContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode.Paren visitParenExpr(MxParser.ParenExprContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode.PreUnary visitPreUnaryExpr(MxParser.PreUnaryExprContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode.ArrayAccess visitArrayAccessExpr(MxParser.ArrayAccessExprContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode.Literal visitLiteralExpr(MxParser.LiteralExprContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode.FunctionCall visitFunctionCallExpr(MxParser.FunctionCallExprContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode.MemberAccess visitMemberAccessExpr(MxParser.MemberAccessExprContext ctx) {
        return null;
    }

    @Override
    public ExpressionNode.FormatString visitFormatStringExpr(MxParser.FormatStringExprContext ctx) {
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
    public ExpressionNode.Identifier visitIdentifierExpr(MxParser.IdentifierExprContext ctx) {
        return null;
    }

    @Override
    public LiteralNode visitLiteral(MxParser.LiteralContext ctx) {
        return null;
    }

    @Override
    public CreatorNode visitCreator(MxParser.CreatorContext ctx) {
        return null;
    }

    @Override
    public CreatorBodyNode visitCreatorBody(MxParser.CreatorBodyContext ctx) {
        return (CreatorBodyNode) visitChildren(ctx);
    }

    @Override
    public ArrayCreatorBodyNode.Literal visitLiteralArrayCreator(MxParser.LiteralArrayCreatorContext ctx) {
        return null;
    }

    @Override
    public ArrayCreatorBodyNode.Empty visitEmptyArrayCreator(MxParser.EmptyArrayCreatorContext ctx) {
        return null;
    }

    @Override
    public ArrayInitializerNode visitArrayInitializer(MxParser.ArrayInitializerContext ctx) {
        return null;
    }

    @Override
    public ArgumentListNode visitArgumentList(MxParser.ArgumentListContext ctx) {
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
    public TypeNameNode visitTypeName(MxParser.TypeNameContext ctx) {
        return null;
    }

    @Override
    @Nullable
    public TypeNode visitReturnType(MxParser.ReturnTypeContext ctx) {
        return ctx.type() == null ? null : visitType(ctx.type());
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
    public FormatStringNode.Atom visitAtomFormatString(MxParser.AtomFormatStringContext ctx) {
        return null;
    }

    @Override
    public FormatStringNode.Complex visitComplexFormatString(MxParser.ComplexFormatStringContext ctx) {
        return null;
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
            throw new IllegalArgumentException("RuleNode is not a single child node");
        }
        return visit(ruleNode.getChild(0));
    }

    // unused. terminal nodes are dealt directly in visitXXX methods
    @Override
    public AstNode visitTerminal(TerminalNode terminalNode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AstNode visitErrorNode(ErrorNode errorNode) { // TODO: error handling
        throw new UnsupportedOperationException();
    }

    protected StatementNode visitStatement(MxParser.StatementContext ctx) {
        return (StatementNode) visit(ctx);
    }

    protected ExpressionNode visitExpression(MxParser.ExpressionContext ctx) {
        return (ExpressionNode) visit(ctx);
    }

    protected ArrayCreatorBodyNode visitArrayCreatorBody(MxParser.ArrayCreatorBodyContext ctx) {
        return (ArrayCreatorBodyNode) visit(ctx);
    }

    protected FormatStringNode visitFormatString(MxParser.FormatStringContext ctx) {
        return (FormatStringNode) visit(ctx);
    }
}