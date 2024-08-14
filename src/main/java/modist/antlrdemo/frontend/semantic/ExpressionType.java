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
        // expressions shouldn't be empty
        @Nullable
        private Type joinTypes(ExpressionNode... expressions) {
            Type expectedType = getNullableType(expressions[0]);
            for (int i = 1; i < expressions.length; i++) {
                expectedType = joinType(expressions[i], expectedType);
            }
            return expectedType;
        }

        private Type testSolidType(ExpressionNode expression, Predicate<Type> predicate, String predicateDescription) {
            Type type = getSolidType(expression); // not null for the convenience of the predicate
            if (!predicate.test(type)) {
                throw new TypeMismatchException(type, predicateDescription, expression.position);
            }
            return type;
        }

        // won't return null if expectedType is not null
        @Nullable
        public Type joinType(ExpressionNode expression, @Nullable Type expectedType) {
            Type type = getNullableType(expression);
            if (expectedType == null || type == null) {
                if (expectedType != null || type != null) {
                    throw new TypeMismatchException(type, expectedType, expression.position);
                }
                return null;
            }
            Type jointType = expectedType.join(type);
            if (jointType == null) {
                throw new TypeMismatchException(type, expectedType, expression.position);
            }
            return jointType;
        }

        @Nullable
        private Type getType(ExpressionNode expression, boolean checkLvalue, boolean checkNull) {
            ExpressionType expressionType = build(expression);
            if (checkNull && expressionType.type == null) {
                throw new TypeMismatchException(expressionType.type, "non-void", expression.position);
            }
            if (checkLvalue && !expressionType.isLValue) {
                throw new SemanticException("Expression is not an lvalue", expression.position);
            }
            return expressionType.type;
        }

        private Type getLvalueType(ExpressionNode expression) {
            return getType(expression, true, true);
        }

        private Type getSolidType(ExpressionNode expression) {
            return getType(expression, false, true);
        }

        @Nullable
        private Type getNullableType(ExpressionNode expression) {
            return getType(expression, false, false);
        }

        @Nullable
        private Type getOperationResult(Type type, Operator op) {
            boolean isBool = type.equals(BuiltinFeatures.BOOL);
            boolean isInt = type.equals(BuiltinFeatures.INT);
            boolean isString = type.equals(BuiltinFeatures.STRING);
            return switch (op) {
                case EQ, NE -> BuiltinFeatures.BOOL;
                case ADD -> isInt || isString ? type : null;
                case LT, GT, LE, GE -> isInt || isString ? BuiltinFeatures.BOOL : null;
                case INC, DEC, NOT, AND, XOR, OR, SUB, MUL, DIV, MOD, SHL, SHR -> isInt ? type : null;
                case LOGICAL_AND, LOGICAL_OR, LOGICAL_NOT -> isBool ? type : null;
            };
        }

        private Type testOperator(Type type, Operator op, Position position) {
            Type result = getOperationResult(type, op);
            if (result == null) {
                throw new SemanticException(String.format("Operator '%s' cannot be applied to type '%s'", op, type), position);
            }
            return result;
        }

        public ExpressionType build(ExpressionNode expression) {
            return switch (expression) {
                case This ignored -> {
                    if (!scope.inClass) {
                        throw new SemanticException("Use of 'this' outside of class", expression.position);
                    }
                    yield new ExpressionType(scope.thisType);
                }
                case Literal literal -> switch (literal.value) {
                    case LiteralEnum.Int ignored -> new ExpressionType(BuiltinFeatures.INT);
                    case LiteralEnum.Bool ignored -> new ExpressionType(BuiltinFeatures.BOOL);
                    case LiteralEnum.Str ignored -> new ExpressionType(BuiltinFeatures.STRING);
                    case LiteralEnum.Null ignored -> new ExpressionType(Type.NULL);
                };
                case Array array -> {
                    Type elementType = array.elements.isEmpty() ? Type.NULL : joinTypes(array.elements.toArray(ExpressionNode[]::new));
                    if (elementType == null) {
                        throw new TypeMismatchException(elementType, "non-void", expression.position);
                    }
                    yield new ExpressionType(elementType.increaseDimension());
                }
                case FormatString formatString -> {
                    formatString.expressions.forEach(child -> testSolidType(child, Type::isPrimitive, "primitive"));
                    yield new ExpressionType(BuiltinFeatures.STRING);
                }
                case Creator creator -> {
                    Symbol.TypeName typeName = scope.resolveTypeName(creator.typeName, expression.position);
                    yield switch (creator.arrayCreator) {
                        case null -> new ExpressionType(new Type(typeName));
                        case ArrayCreatorNode.Empty empty -> {
                            empty.dimensionLengths.forEach(child -> joinType(child, BuiltinFeatures.INT));
                            yield new ExpressionType(new Type(typeName, empty.dimensionLengths.size() + empty.emptyDimension));
                        }
                        case ArrayCreatorNode.Init init ->
                                new ExpressionType(joinType(init.initializer, new Type(typeName, init.dimension)));
                    };
                }
                case Subscript subscript -> {
                    joinType(subscript.index, BuiltinFeatures.INT);
                    yield new ExpressionType(testSolidType(subscript.expression, Type::isArray, "array").decreaseDimension(), true);
                }
                case Variable variable ->
                        variable.expression == null ? new ExpressionType(scope.resolveVariable(variable.name, expression.position).type, true) :
                                new ExpressionType(scope.getClass(getSolidType(variable.expression)).variables.resolve(variable.name, expression.position).type, true);
                case Function function -> {
                    Symbol.Function functionSymbol = function.expression == null ?
                            scope.resolveFunction(function.name, expression.position) :
                            scope.getClass(getSolidType(function.expression)).functions.resolve(function.name, expression.position);
                    if (functionSymbol.parameters.size() != function.arguments.size()) {
                        throw new SemanticException(String.format("Function '%s' expects %d arguments, but %d given",
                                function.name, functionSymbol.parameters.size(), function.arguments.size()), expression.position);
                    }
                    for (int i = 0; i < function.arguments.size(); i++) {
                        joinType(function.arguments.get(i), functionSymbol.parameters.get(i).type);
                    }
                    yield new ExpressionType(functionSymbol.returnType);
                }
                case PostUnaryAssign postUnaryAssign ->
                        new ExpressionType(testOperator(getLvalueType(postUnaryAssign.expression), postUnaryAssign.operator, expression.position));
                case PreUnaryAssign preUnaryAssign ->
                        new ExpressionType(testOperator(getLvalueType(preUnaryAssign.expression), preUnaryAssign.operator, expression.position), true);
                case PreUnary preUnary ->
                        new ExpressionType(testOperator(getSolidType(preUnary.expression), preUnary.operator, expression.position));
                case Binary binary ->
                        new ExpressionType(testOperator(joinType(binary.rightExpression, getSolidType(binary.leftExpression)), binary.operator, expression.position));
                case Conditional conditional -> {
                    joinType(conditional.condition, BuiltinFeatures.BOOL);
                    yield new ExpressionType(joinTypes(conditional.falseExpression, conditional.trueExpression));
                }
                case Assign assign -> {
                    joinType(assign.rightExpression, getLvalueType(assign.leftExpression));
                    yield new ExpressionType(null);
                }
            };
        }
    }
}
