package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.error.CompileErrorType;
import modist.antlrdemo.frontend.error.CompileException;
import modist.antlrdemo.frontend.error.InvalidTypeException;
import modist.antlrdemo.frontend.error.TypeMismatchException;
import modist.antlrdemo.frontend.metadata.LiteralEnum;
import modist.antlrdemo.frontend.metadata.Operator;
import modist.antlrdemo.frontend.metadata.Position;
import modist.antlrdemo.frontend.semantic.scope.Scope;
import modist.antlrdemo.frontend.syntax.node.*;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;

public record Type(@Nullable Symbol.TypeName typeName, int dimension) {
    public static final Type NULL = new Type(null);

    // typeName==null: null type. type is not fixed and can match any non-primitive type. dimension must be 0
    // typeName==VOID: void type. type is fixed and can only match void type. dimension must be 0
    public Type(Scope scope, TypeNode typeNode) {
        this(scope.resolveTypeName(typeNode.typeName, typeNode.position), typeNode.dimension);
    }

    public Type(Symbol.TypeName typeName) {
        this(typeName, 0);
    }

    public Type(Scope scope, DeclarationNode.Class classNode) {
        this(scope.resolveTypeName(classNode.name, classNode.position));
    }

    public boolean isNull() {
        return typeName == null;
    }

    public boolean isVoid() {
        return typeName == BuiltinFeatures.VOID_TYPE_NAME;
    }

    public boolean isArray() {
        return dimension > 0;
    }

    public boolean isPrimitive() {
        return typeName != null && typeName.primitive && dimension == 0;
    }

    public boolean canFormat() {
        return equals(BuiltinFeatures.INT) || equals(BuiltinFeatures.BOOL) || equals(BuiltinFeatures.STRING);
    }

    // return the narrower type if matched, throw exception otherwise
    // this should be a typical situation of TypeMismatchException
    private Type tryMatch(Type other, Position position) {
        if (equals(other)) {
            return this;
        }
        if (isNull() && !other.isPrimitive()) {
            return other;
        }
        if (other.isNull() && !isPrimitive()) {
            return this;
        }
        throw new TypeMismatchException(this, other, position);
    }

    // just to simplify the code
    @Nullable
    private Type getOperationResult(Operator op) {
        boolean isVoid = isVoid();
        boolean isBool = equals(BuiltinFeatures.BOOL);
        boolean isInt = equals(BuiltinFeatures.INT);
        boolean isString = equals(BuiltinFeatures.STRING);
        return switch (op) {
            case EQ, NE -> isVoid ? null : BuiltinFeatures.BOOL;
            case ADD -> (isInt || isString) ? this : null;
            case LT, GT, LE, GE -> (isInt || isString) ? BuiltinFeatures.BOOL : null;
            case INC, DEC, NOT, AND, XOR, OR, SUB, MUL, DIV, MOD, SHL, SHR -> isInt ? this : null;
            case LOGICAL_AND, LOGICAL_OR, LOGICAL_NOT -> isBool ? this : null;
        };
    }

    // return the result type of the operation, throw exception if the operation is invalid
    private Type getOperationResult(Operator op, Position position) {
        Type result = getOperationResult(op);
        if (result != null) {
            return result;
        }
        throw new CompileException(String.format("Operator '%s' cannot be applied to type '%s'", op, this), position);
    }

    // throw InvalidTypeException if not valid
    private void testType(Type expectedType, Position position) {
        Objects.requireNonNull(expectedType);
        if (equals(expectedType)) {
            return;
        }
        if (isNull() && !expectedType.isPrimitive()) {
            return;
        }
        throw new InvalidTypeException(this, expectedType.toString(), position);
    }

    // throw InvalidTypeException if not valid
    private void testType(Predicate<Type> predicate, String predicateDescription, Position position) {
        if (predicate.test(this)) {
            return;
        }
        throw new InvalidTypeException(this, predicateDescription, position);
    }

    private Type decreaseDimension() {
        return new Type(typeName, dimension - 1);
    }

    @Override
    public String toString() {
        if (isNull()) {
            return "null";
        }
        return typeName.name + "[]".repeat(dimension);
    }

    public static class Builder {
        private final Scope scope;
        private boolean isLValue;

        public Builder(Scope scope) {
            this.scope = scope;
        }

        private Type buildLvalue(ExpressionNode expression) {
            Type type = build(expression);
            if (!isLValue) {
                throw new CompileException("Expression is not an lvalue", expression.position);
            }
            return type;
        }

        // return expectedType if matched, throw exception otherwise
        // we throw TypeMismatchException instead of InvalidTypeException because this function is used to check an ExpressionOrArrayNode against a declared type
        public void tryMatchExpression(ExpressionOrArrayNode expressionOrArray, Type expectedType) {
            Objects.requireNonNull(expectedType.typeName());
            switch (expressionOrArray) {
                case ExpressionNode expression ->
                        build(expression).tryMatch(expectedType, expression.position); // we use tryMatch just for throwing TypeMismatchException; expectedType is already checked
                case ArrayNode array -> {
                    if (!expectedType.isArray()) {
                        throw new CompileException(CompileErrorType.TYPE_MISMATCH, "non-array type expected", array.position);
                    }
                    Type expectedElementType = expectedType.decreaseDimension();
                    array.elements.forEach(child -> tryMatchExpression(child, expectedElementType));
                }
            }
        }

        // throw InvalidTypeException if not valid
        public void testExpressionType(ExpressionNode expression, Type expectedType) {
            build(expression).testType(expectedType, expression.position);
        }

        public Type build(ExpressionNode expression) {
            boolean isLValueTemp = false;
            Type returnType = switch (expression) {
                case ExpressionNode.This ignored -> {
                    if (scope.thisType == null) {
                        throw new CompileException("Use of 'this' outside of class", expression.position);
                    }
                    yield scope.thisType;
                }
                case ExpressionNode.Literal literal -> switch (literal.value) {
                    case LiteralEnum.Int ignored -> BuiltinFeatures.INT;
                    case LiteralEnum.Bool ignored -> BuiltinFeatures.BOOL;
                    case LiteralEnum.Str ignored -> BuiltinFeatures.STRING;
                    case LiteralEnum.Null ignored -> NULL;
                };
                case ExpressionNode.FormatString formatString -> {
                    formatString.expressions.forEach(child -> build(child).testType(Type::canFormat, "string, int or bool", child.position));
                    yield BuiltinFeatures.STRING;
                }
                case ExpressionNode.Creator creator -> {
                    Symbol.TypeName typeName = scope.resolveTypeName(creator.typeName, expression.position);
                    yield switch (creator.arrayCreator) {
                        case null -> new Type(typeName);
                        case ArrayCreatorNode.Empty empty -> {
                            empty.dimensionLengths.forEach(child -> build(child).testType(BuiltinFeatures.INT, child.position));
                            yield new Type(typeName, empty.dimensionLengths.size() + empty.emptyDimension);
                        }
                        case ArrayCreatorNode.Init init -> {
                            Type type = new Type(typeName, init.dimension);
                            tryMatchExpression(init.initializer, type);
                            yield type;
                        }
                    };
                }
                case ExpressionNode.Subscript subscript -> {
                    Type arrayType = build(subscript.expression);
                    if (!arrayType.isArray()) {
                        throw new CompileException(CompileErrorType.DIMENSION_OUT_OF_BOUND, "subscripting non-array type", expression.position);
                    }
                    testExpressionType(subscript.index, BuiltinFeatures.INT);
                    isLValueTemp = true;
                    yield arrayType.decreaseDimension();
                }
                case ExpressionNode.Variable variable -> {
                    isLValueTemp = true;
                    yield variable.expression == null ? scope.resolveVariable(variable.name, expression.position).type :
                            scope.resolveClass(build(variable.expression), variable.expression.position).variables.resolve(variable.name, expression.position).type;
                }
                case ExpressionNode.Function function -> {
                    Symbol.Function functionSymbol = function.expression == null ?
                            scope.resolveFunction(function.name, expression.position) :
                            scope.resolveClass(build(function.expression), function.expression.position).functions.resolve(function.name, expression.position);
                    if (functionSymbol.parameters.size() != function.arguments.size()) {
                        throw new CompileException(String.format("Function '%s' expects %d arguments, but %d given",
                                function.name, functionSymbol.parameters.size(), function.arguments.size()), expression.position);
                    }
                    for (int i = 0; i < function.arguments.size(); i++) {
                        tryMatchExpression(function.arguments.get(i), functionSymbol.parameters.get(i).type);
                    }
                    yield functionSymbol.returnType;
                }
                case ExpressionNode.PostUnaryAssign postUnaryAssign ->
                        buildLvalue(postUnaryAssign.expression).getOperationResult(postUnaryAssign.operator, expression.position);
                case ExpressionNode.PreUnaryAssign preUnaryAssign -> {
                    isLValueTemp = true;
                    yield buildLvalue(preUnaryAssign.expression).getOperationResult(preUnaryAssign.operator, expression.position);
                }
                case ExpressionNode.PreUnary preUnary ->
                        build(preUnary.expression).getOperationResult(preUnary.operator, expression.position);
                case ExpressionNode.Binary binary ->
                        build(binary.leftExpression).tryMatch(build(binary.rightExpression), binary.rightExpression.position).getOperationResult(binary.operator, expression.position);
                case ExpressionNode.Conditional conditional -> {
                    build(conditional.condition).testType(BuiltinFeatures.BOOL, conditional.condition.position);
                    yield build(conditional.trueExpression).tryMatch(build(conditional.falseExpression), conditional.falseExpression.position);
                }
                case ExpressionNode.Assign assign -> {
                    tryMatchExpression(assign.rightExpression, buildLvalue(assign.leftExpression));
                    yield BuiltinFeatures.VOID;
                }
            };
            isLValue = isLValueTemp;
            return returnType;
        }
    }
}