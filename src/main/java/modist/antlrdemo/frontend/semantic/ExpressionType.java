package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.error.ExpressionTypeMismatchException;
import modist.antlrdemo.frontend.error.SemanticException;
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
                Type newType = getSolidType(expression);
                if (type == null) {
                    type = newType;
                } else {
                    Type jointType = type.join(newType);
                    if (jointType == null) {
                        throw new ExpressionTypeMismatchException(newType, type, expression.position);
                    }
                    type = jointType;
                }
            }
            // we use null in the loop for the first time to represent the type of the first expression
            // we return Type.NULL if the expressionNodes is empty
            return type == null ? modist.antlrdemo.frontend.semantic.Type.NULL : type;
        }

        private Type expectType(ExpressionNode expression, Predicate<Type> predicate, String predicateDescription, boolean checkLvalue) {
            Type type = getSolidType(expression, checkLvalue);
            if (!predicate.test(type)) {
                throw new ExpressionTypeMismatchException(type, predicateDescription, expression.position);
            }
            return type;
        }

        private Type expectType(ExpressionNode expression, Predicate<Type> predicate, String predicateDescription) {
            return expectType(expression, predicate, predicateDescription, false);
        }

        private Type expectType(ExpressionNode expression, Type expectedType, boolean checkLvalue) {
            Type type = getSolidType(expression, checkLvalue);
            if (expectedType.join(type) == null) {
                throw new ExpressionTypeMismatchException(type, expectedType, expression.position);
            }
            return type;
        }

        private Type expectType(ExpressionNode expression, Type expectedType) {
            return expectType(expression, expectedType, false);
        }

        private Type getSolidType(ExpressionNode expression, boolean checkLvalue) {
            ExpressionType expressionType = build(expression);
            if (expressionType.type == null) {
                throw new ExpressionTypeMismatchException(expressionType.type, "non-null", expression.position);
            }
            if (checkLvalue && !expressionType.isLValue) {
                throw new SemanticException("Expression is not an lvalue", expression.position);
            }
            return expressionType.type;
        }

        private Type getSolidType(ExpressionNode expression) {
            return getSolidType(expression, false);
        }

        private boolean isArithmetic(Type type) {
            return type == BuiltinFeatures.INT;
        }

        public ExpressionType build(ExpressionNode expression) {
            return switch (expression) {
                case This ignored -> switch (scope) {
                    case ChildScope.Class classScope -> new ExpressionType(classScope.classType);
                    default -> throw new SemanticException("Use of 'this' outside class", expression.position);
                };
                case Literal literal -> switch (literal.value) {
                    case Literal.LiteralEnum.Int ignored -> new ExpressionType(BuiltinFeatures.INT);
                    case Literal.LiteralEnum.Bool ignored -> new ExpressionType(BuiltinFeatures.BOOL);
                    case Literal.LiteralEnum.Str ignored -> new ExpressionType(BuiltinFeatures.STRING);
                    case Literal.LiteralEnum.Null ignored ->
                            new ExpressionType(modist.antlrdemo.frontend.semantic.Type.NULL);
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
                        expectType(function.arguments.get(i), functionSymbol.parameters.get(i).type);
                    }
                    yield new ExpressionType(functionSymbol.returnType);
                }
                case PostUnary postUnary ->
                        new ExpressionType(expectType(postUnary.expression, BuiltinFeatures.INT, true));
                case PreUnary preUnary -> {
                    Type type = expectType(preUnary.expression, BuiltinFeatures.INT, true);
                    yield switch (preUnary.operator) {
                        case PreUnary.Operator.Plus, PreUnary.Operator.Minus -> new ExpressionType(BuiltinFeatures.INT);
                        case PreUnary.Operator.Not -> new ExpressionType(BuiltinFeatures.BOOL);
                    };
                }
            };
        }
    }
}
