package modist.antlrdemo.frontend.ast;

import modist.antlrdemo.frontend.ast.node.*;

public interface AstVisitor<T> {
    //TODO: simplify the interface. some AST nodes don't need to be visited.
    T visit(ArgumentListNode node);

    T visit(ArrayCreatorBodyNode.Empty node);

    T visit(ArrayCreatorBodyNode.Literal node);

    T visit(ArrayInitializerNode node);

    T visit(BlockNode node);

    T visit(ClassDeclarationNode node);

    T visit(ConstructorDeclarationNode node);

    T visit(CreatorNode node);

    T visit(ExpressionNode.Paren node);

    T visit(ExpressionNode.This node);

    T visit(ExpressionNode.Literal node);

    T visit(ExpressionNode.FormatString node);

    T visit(ExpressionNode.Identifier node);

    T visit(ExpressionNode.New node);

    T visit(ExpressionNode.ArrayAccess node);

    T visit(ExpressionNode.MemberAccess node);

    T visit(ExpressionNode.FunctionCall node);

    T visit(ExpressionNode.PostUnary node);

    T visit(ExpressionNode.PreUnary node);

    T visit(ExpressionNode.Binary node);

    T visit(ExpressionNode.Conditional node);

    T visit(ExpressionNode.Assign node);

    T visit(FormalParameterNode node);

    T visit(FormalParameterListNode node);

    T visit(FormatStringNode node);

    T visit(FunctionDeclarationNode node);

    T visit(LiteralNode node);

    T visit(ProgramNode node);

    T visit(StatementNode.Block node);

    T visit(StatementNode.VariableDeclaration node);

    T visit(StatementNode.If node);

    T visit(StatementNode.For node);

    T visit(StatementNode.While node);

    T visit(StatementNode.Break node);

    T visit(StatementNode.Continue node);

    T visit(StatementNode.Return node);

    T visit(StatementNode.Expression node);

    T visit(StatementNode.Empty node);

    T visit(TypeNameNode node);

    T visit(TypeNode node);

    T visit(VariableDeclarationNode node);

    T visit(VariableDeclaratorNode node);
}