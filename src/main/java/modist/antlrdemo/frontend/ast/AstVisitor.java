package modist.antlrdemo.frontend.ast;

import modist.antlrdemo.frontend.ast.node.*;

public interface AstVisitor {
    void visit(ArgumentListNode node);

    void visit(ArrayCreatorBodyNode.Empty node);

    void visit(ArrayCreatorBodyNode.Expression node);

    void visit(ArrayInitializerNode node);

    void visit(BlockNode node);

    void visit(ClassDeclarationNode node);

    void visit(ConstructorDeclarationNode node);

    void visit(CreatorNode node);

    void visit(ExpressionNode.Paren node);

    void visit(ExpressionNode.This node);

    void visit(ExpressionNode.Literal node);

    void visit(ExpressionNode.FormatString node);

    void visit(ExpressionNode.Identifier node);

    void visit(ExpressionNode.New node);

    void visit(ExpressionNode.ArrayAccess node);

    void visit(ExpressionNode.MemberAccess node);

    void visit(ExpressionNode.FunctionCall node);

    void visit(ExpressionNode.PostUnary node);

    void visit(ExpressionNode.PreUnary node);

    void visit(ExpressionNode.Binary node);

    void visit(ExpressionNode.Conditional node);

    void visit(ExpressionNode.Assign node);

    void visit(ForControlNode node);

    void visit(FormalParameterNode node);

    void visit(FormatStringNode node);

    void visit(FunctionDeclarationNode node);

    void visit(LiteralNode node);

    void visit(ProgramNode node);

    void visit(StatementNode.Block node);

    void visit(StatementNode.VariableDeclaration node);

    void visit(StatementNode.If node);

    void visit(StatementNode.For node);

    void visit(StatementNode.While node);

    void visit(StatementNode.Break node);

    void visit(StatementNode.Continue node);

    void visit(StatementNode.Return node);

    void visit(StatementNode.Expression node);

    void visit(StatementNode.Empty node);

    void visit(TypeNameNode node);

    void visit(TypeNode node);

    void visit(VariableDeclarationNode node);

    void visit(VariableDeclaratorNode node);
}