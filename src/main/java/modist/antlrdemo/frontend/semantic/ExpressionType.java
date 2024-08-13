package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.error.TypeMismatchException;
import modist.antlrdemo.frontend.error.SemanticException;
import modist.antlrdemo.frontend.metadata.LiteralEnum;
import modist.antlrdemo.frontend.metadata.Operator;
import modist.antlrdemo.frontend.metadata.Position;
import modist.antlrdemo.frontend.syntax.node.ArrayCreatorNode;
import modist.antlrdemo.frontend.syntax.node.ExpressionNode;
import modist.antlrdemo.frontend.syntax.node.ExpressionNode.*;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public record ExpressionType(@Nullable Type type, boolean isLValue) {

    public ExpressionType(Type type) {
        this(type, false);
    }

    public record Builder(Scope scope) {
        private Type getJointType(ExpressionNode... expressions) {
            @Nullable Type type = null;
            for (ExpressionNode expression : expressions) {
                Type newType = getType(expression);
                if (type == null) {
                    type = newType;
                } else {
                    Type jointType = type.join(newType);
                    if (jointType == null) {
                        throw new TypeMismatchException(newType, type, expression.position);
                    }
                    type = jointType;
                }
            }
            // we use null in the loop for the first time to represent the type of the first expression
            // we return Type.NULL if the expressionNodes is empty
            return type == null ? Type.NULL : type;
        }

        private Type expectType(ExpressionNode expression, Predicate<Type> predicate, String predicateDescription) {
            Type type = getType(expression);
            if (!predicate.test(type)) {
                throw new TypeMismatchException(type, predicateDescription, expression.position);
            }
            return type;
        }

        private Type expectType(ExpressionNode expression, Type expectedType) {
            Type type = getType(expression);
            if (expectedType.join(type) == null) {
                throw new TypeMismatchException(type, expectedType, expression.position);
            }
            return type;
        }

        private Type getType(ExpressionNode expression, boolean checkLvalue) {
            ExpressionType expressionType = build(expression);
            if (expressionType.type == null) {
                throw new TypeMismatchException(expressionType.type, "non-null", expression.position);
            }
            if (checkLvalue && !expressionType.isLValue) {
                throw new SemanticException("Expression is not an lvalue", expression.position);
            }
            return expressionType.type;
        }

        private Type getType(ExpressionNode expression) {
            return getType(expression, false);
        }

        private boolean canOperate(Type type, Operator op) {
            return switch (op) {
                case EQ, NE -> true;
                case ADD, LT, GT, LE, GE -> type.equals(BuiltinFeatures.INT) || type.equals(BuiltinFeatures.STRING);
                case INC, DEC, NOT, AND, XOR, OR, SUB, MUL, DIV, MOD, SHL, SHR -> type == BuiltinFeatures.INT;
                case LOGICAL_AND, LOGICAL_OR, LOGICAL_NOT -> type == BuiltinFeatures.BOOL;
            };
        }

        private Type testOperator(Type type, Operator op, Position position) {
            if (!canOperate(type, op)) {
                throw new SemanticException(String.format("Operator '%s' cannot be applied to type '%s'", op, type), position);
            }
            return type;
        }

        public ExpressionType build(ExpressionNode expression) {
            return switch (expression) {
                case This ignored -> switch (scope) {
                    case ChildScope.Class classScope -> new ExpressionType(classScope.classType);
                    default -> throw new SemanticException("Use of 'this' outside class", expression.position);
                };
                case Literal literal -> switch (literal.value) {
                    case LiteralEnum.Int ignored -> new ExpressionType(BuiltinFeatures.INT);
                    case LiteralEnum.Bool ignored -> new ExpressionType(BuiltinFeatures.BOOL);
                    case LiteralEnum.Str ignored -> new ExpressionType(BuiltinFeatures.STRING);
                    case LiteralEnum.Null ignored -> new ExpressionType(Type.NULL);
                };
                case Array array -> new ExpressionType(getJointType(array.elements.toArray(ExpressionNode[]::new)));
                case FormatString formatString -> {
                    formatString.expressions.forEach(child -> expectType(child, Type::isPrimitive, "primitive"));
                    yield new ExpressionType(BuiltinFeatures.STRING);
                }
                case Creator creator -> {
                    Symbol.TypeName typeName = scope.resolveTypeName(creator.typeName, expression.position);
                    yield switch (creator.arrayCreator) {
                        case null -> new ExpressionType(new Type(typeName));
                        case ArrayCreatorNode.Empty empty -> {
                            empty.dimensionLengths.forEach(child -> expectType(child, BuiltinFeatures.INT));
                            yield new ExpressionType(new Type(typeName, empty.dimensionLengths.size() + empty.emptyDimension));
                        }
                        case ArrayCreatorNode.Init init ->
                                new ExpressionType(expectType(init.initializer, new Type(typeName, init.dimension)));
                    };
                }
                case Subscript subscript -> {
                    expectType(subscript.index, BuiltinFeatures.INT);
                    yield new ExpressionType(expectType(subscript.expression, Type::isArray, "array").decreaseDimension());
                }
                case Variable variable ->
                        variable.expression == null ? new ExpressionType(scope.resolveVariable(variable.name, expression.position).type, true) :
                                new ExpressionType(scope.getClass(getType(variable.expression)).variables.resolve(variable.name, expression.position).type, true);
                case Function function -> {
                    Symbol.Function functionSymbol = function.expression == null ?
                            scope.resolveFunction(function.name, expression.position) :
                            scope.getClass(getType(function.expression)).functions.resolve(function.name, expression.position);
                    if (functionSymbol.parameters.size() != function.arguments.size()) {
                        throw new SemanticException(String.format("Function '%s' expects %d arguments, but %d given",
                                function.name, functionSymbol.parameters.size(), function.arguments.size()), expression.position);
                    }
                    for (int i = 0; i < function.arguments.size(); i++) {
                        expectType(function.arguments.get(i), functionSymbol.parameters.get(i).type);
                    }
                    yield new ExpressionType(functionSymbol.returnType);
                }
                case PostUnaryAssign postUnaryAssign ->
                        new ExpressionType(testOperator(getType(postUnaryAssign.expression, true), postUnaryAssign.operator, expression.position));
                case PreUnaryAssign preUnaryAssign ->
                        new ExpressionType(testOperator(getType(preUnaryAssign.expression, true), preUnaryAssign.operator, expression.position));
                case PreUnary preUnary ->
                        new ExpressionType(testOperator(getType(preUnary.expression), preUnary.operator, expression.position));
                case Binary binary ->
                        new ExpressionType(testOperator(getJointType(binary.leftExpression, binary.rightExpression), binary.operator, expression.position));
                case Conditional conditional -> {
                    expectType(conditional.condition, BuiltinFeatures.BOOL);
                    yield new ExpressionType(getJointType(conditional.trueExpression, conditional.falseExpression));
                }
                case Assign assign -> {
                    expectType(assign.rightExpression, getType(assign.leftExpression, true));
                    yield new ExpressionType(null);
                }
            };
        }
    }
}
