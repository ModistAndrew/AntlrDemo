package modist.antlrdemo.frontend.ast;

import modist.antlrdemo.frontend.ast.metadata.Position;
import modist.antlrdemo.frontend.ast.node.AstNode;
import modist.antlrdemo.frontend.ast.node.ClassDeclarationNode;
import modist.antlrdemo.frontend.ast.node.ProgramNode;
import modist.antlrdemo.frontend.grammar.MxParser;
import modist.antlrdemo.frontend.grammar.MxVisitor;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

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
    public AstNode visitClassBody(MxParser.ClassBodyContext ctx) {
        return null;
    }

    @Override
    public AstNode visitFunctionDeclaration(MxParser.FunctionDeclarationContext ctx) {
        return null;
    }

    @Override
    public AstNode visitConstructorDeclaration(MxParser.ConstructorDeclarationContext ctx) {
        return null;
    }

    @Override
    public AstNode visitFormalParameterList(MxParser.FormalParameterListContext ctx) {
        return null;
    }

    @Override
    public AstNode visitFormalParameter(MxParser.FormalParameterContext ctx) {
        return null;
    }

    @Override
    public AstNode visitBlock(MxParser.BlockContext ctx) {
        return null;
    }

    @Override
    public AstNode visitBlockStmt(MxParser.BlockStmtContext ctx) {
        return null;
    }

    @Override
    public AstNode visitVariableDeclarationStmt(MxParser.VariableDeclarationStmtContext ctx) {
        return null;
    }

    @Override
    public AstNode visitIfStmt(MxParser.IfStmtContext ctx) {
        return null;
    }

    @Override
    public AstNode visitForStmt(MxParser.ForStmtContext ctx) {
        return null;
    }

    @Override
    public AstNode visitWhileStmt(MxParser.WhileStmtContext ctx) {
        return null;
    }

    @Override
    public AstNode visitBreakStmt(MxParser.BreakStmtContext ctx) {
        return null;
    }

    @Override
    public AstNode visitContinueStmt(MxParser.ContinueStmtContext ctx) {
        return null;
    }

    @Override
    public AstNode visitReturnStmt(MxParser.ReturnStmtContext ctx) {
        return null;
    }

    @Override
    public AstNode visitExpressionStmt(MxParser.ExpressionStmtContext ctx) {
        return null;
    }

    @Override
    public AstNode visitEmptyStmt(MxParser.EmptyStmtContext ctx) {
        return null;
    }

    @Override
    public AstNode visitVariableDeclarationBody(MxParser.VariableDeclarationBodyContext ctx) {
        return null;
    }

    @Override
    public AstNode visitVariableDeclaration(MxParser.VariableDeclarationContext ctx) {
        return null;
    }

    @Override
    public AstNode visitVariableDeclarator(MxParser.VariableDeclaratorContext ctx) {
        return null;
    }

    @Override
    public AstNode visitVariableInitializer(MxParser.VariableInitializerContext ctx) {
        return null;
    }

    @Override
    public AstNode visitForControl(MxParser.ForControlContext ctx) {
        return null;
    }

    @Override
    public AstNode visitForInitialization(MxParser.ForInitializationContext ctx) {
        return null;
    }

    @Override
    public AstNode visitNewExpr(MxParser.NewExprContext ctx) {
        return null;
    }

    @Override
    public AstNode visitThisExpr(MxParser.ThisExprContext ctx) {
        return null;
    }

    @Override
    public AstNode visitPostUnaryExpr(MxParser.PostUnaryExprContext ctx) {
        return null;
    }

    @Override
    public AstNode visitBinaryExpr(MxParser.BinaryExprContext ctx) {
        return null;
    }

    @Override
    public AstNode visitParenExpr(MxParser.ParenExprContext ctx) {
        return null;
    }

    @Override
    public AstNode visitPreUnaryExpr(MxParser.PreUnaryExprContext ctx) {
        return null;
    }

    @Override
    public AstNode visitArrayAccessExpr(MxParser.ArrayAccessExprContext ctx) {
        return null;
    }

    @Override
    public AstNode visitLiteralExpr(MxParser.LiteralExprContext ctx) {
        return null;
    }

    @Override
    public AstNode visitFunctionCallExpr(MxParser.FunctionCallExprContext ctx) {
        return null;
    }

    @Override
    public AstNode visitMemberAccessExpr(MxParser.MemberAccessExprContext ctx) {
        return null;
    }

    @Override
    public AstNode visitFormatStringExpr(MxParser.FormatStringExprContext ctx) {
        return null;
    }

    @Override
    public AstNode visitAssignExpr(MxParser.AssignExprContext ctx) {
        return null;
    }

    @Override
    public AstNode visitConditionalExpr(MxParser.ConditionalExprContext ctx) {
        return null;
    }

    @Override
    public AstNode visitIdentifierExpr(MxParser.IdentifierExprContext ctx) {
        return null;
    }

    @Override
    public AstNode visitLiteral(MxParser.LiteralContext ctx) {
        return null;
    }

    @Override
    public AstNode visitCreator(MxParser.CreatorContext ctx) {
        return null;
    }

    @Override
    public AstNode visitCreatorBody(MxParser.CreatorBodyContext ctx) {
        return null;
    }

    @Override
    public AstNode visitLiteralArrayCreator(MxParser.LiteralArrayCreatorContext ctx) {
        return null;
    }

    @Override
    public AstNode visitEmptyArrayCreator(MxParser.EmptyArrayCreatorContext ctx) {
        return null;
    }

    @Override
    public AstNode visitArrayInitializer(MxParser.ArrayInitializerContext ctx) {
        return null;
    }

    @Override
    public AstNode visitArgumentList(MxParser.ArgumentListContext ctx) {
        return null;
    }

    @Override
    public AstNode visitCondition(MxParser.ConditionContext ctx) {
        return null;
    }

    @Override
    public AstNode visitType(MxParser.TypeContext ctx) {
        return null;
    }

    @Override
    public AstNode visitTypeName(MxParser.TypeNameContext ctx) {
        return null;
    }

    @Override
    public AstNode visitReturnType(MxParser.ReturnTypeContext ctx) {
        return null;
    }

    @Override
    public AstNode visitEmptyBracketPair(MxParser.EmptyBracketPairContext ctx) {
        return null;
    }

    @Override
    public AstNode visitExpressionBracketPair(MxParser.ExpressionBracketPairContext ctx) {
        return null;
    }

    @Override
    public AstNode visitAtomFormatString(MxParser.AtomFormatStringContext ctx) {
        return null;
    }

    @Override
    public AstNode visitComplexFormatString(MxParser.ComplexFormatStringContext ctx) {
        return null;
    }

    @Override
    public AstNode visit(ParseTree parseTree) { // convenience method for double dispatch. should not call on self
        // use visitXXX methods for covariant return types
        return parseTree.accept(this);
    }

    @Override
    public AstNode visitChildren(RuleNode ruleNode) { // simply visit the only child. for rule set
        if(ruleNode.getChildCount() != 1) {
            throw new IllegalArgumentException("RuleNode is not a single child node");
        }
        return visit(ruleNode.getChild(0));
    }

    @Override
    public AstNode visitTerminal(TerminalNode terminalNode) { // unused. terminal nodes are dealt directly in visitXXX methods
        throw new UnsupportedOperationException();
    }

    @Override
    public AstNode visitErrorNode(ErrorNode errorNode) { // TODO: error handling
        throw new UnsupportedOperationException();
    }
}