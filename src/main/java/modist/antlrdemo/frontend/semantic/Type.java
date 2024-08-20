package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.error.*;
import modist.antlrdemo.frontend.ast.metadata.LiteralEnum;
import modist.antlrdemo.frontend.ast.metadata.Operator;
import modist.antlrdemo.frontend.semantic.scope.Scope;
import modist.antlrdemo.frontend.ast.node.*;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;

public record Type(@Nullable Symbol.TypeName typeName, int dimension) {
    // typeName==null: null type. type is not fixed and can match any non-builtin type. dimension must be 0
    // typeName==VOID: void type. type is fixed and can only match void type. dimension must be 0
    public static final Type NULL = new Type(null);

    public Symbol.Class resolveClass() {
        if (typeName == null) {
            throw new CompileException("No class for null type");
        }
        if (isArray()) {
            return BuiltinFeatures.ARRAY_CLASS;
        }
        return typeName.definition;
    }

    public Type(Scope scope, TypeAst typeNode) {
        this(scope.resolveTypeName(typeNode.typeName), typeNode.dimension);
    }

    public Type(Symbol.TypeName typeName) {
        this(typeName, 0);
    }

    public boolean isNull() {
        return typeName == null;
    }

    public boolean isVoid() {
        return typeName == BuiltinFeatures.VOID_TYPE_NAME;
    }

    public boolean isInt() {
        return equals(BuiltinFeatures.INT);
    }

    public boolean isBool() {
        return equals(BuiltinFeatures.BOOL);
    }

    public boolean isString() {
        return equals(BuiltinFeatures.STRING);
    }

    public boolean isArray() {
        return dimension > 0;
    }

    public boolean isBuiltin() {
        return typeName != null && typeName.builtin && dimension == 0;
    }

    public boolean canFormat() {
        return equals(BuiltinFeatures.INT) || equals(BuiltinFeatures.BOOL) || equals(BuiltinFeatures.STRING);
    }

    // return the narrower type if matched, throw exception otherwise
    // this should be a typical situation of TypeMismatchException
    public Type tryMatch(Type other) {
        if (equals(other)) {
            return this;
        }
        if (isNull() && !other.isBuiltin()) {
            return other;
        }
        if (other.isNull() && !isBuiltin()) {
            return this;
        }
        throw new TypeMismatchException(this, other);
    }

    // just to simplify the code
    @Nullable
    private Type getOperationResultInternal(Operator op) {
        return switch (op) {
            case EQ, NE -> isVoid() ? null : BuiltinFeatures.BOOL;
            case ADD -> (isInt() || isString()) ? this : null;
            case LT, GT, LE, GE -> (isInt() || isString()) ? BuiltinFeatures.BOOL : null;
            case INC, DEC, NOT, AND, XOR, OR, SUB, MUL, DIV, MOD, SHL, SHR -> isInt() ? this : null;
            case LOGICAL_AND, LOGICAL_OR, LOGICAL_NOT -> isBool() ? this : null;
        };
    }

    // return the result type of the operation, throw exception if the operation is invalid
    public Type getOperationResult(Operator op) {
        Type result = getOperationResultInternal(op);
        if (result != null) {
            return result;
        }
        throw new InvalidTypeException(this, String.format("applicable for operator '%s'", op));
    }

    // throw InvalidTypeException if not valid
    public void testType(Type expectedType) {
        Objects.requireNonNull(expectedType);
        if (equals(expectedType)) {
            return;
        }
        if (isNull() && !expectedType.isBuiltin()) {
            return;
        }
        throw new InvalidTypeException(this, expectedType.toString());
    }

    // throw InvalidTypeException if not valid
    public void testType(Predicate<Type> predicate, String predicateDescription) {
        if (predicate.test(this)) {
            return;
        }
        throw new InvalidTypeException(this, predicateDescription);
    }

    public Type decreaseDimension() {
        return new Type(typeName, dimension - 1);
    }

    @Override
    public String toString() {
        if (isNull()) {
            return "null";
        }
        return typeName.name + "[]".repeat(dimension);
    }

    // store the type in the expression node
    public static class Builder {
        private final Scope scope;
        private boolean isLValue;

        public Builder(Scope scope) {
            this.scope = scope;
        }

        public Type buildLvalue(ExpressionAst expression) {
            Type type = build(expression);
            if (!isLValue) {
                throw new InvalidTypeException(type, "lvalue");
            }
            return type;
        }

        // return expectedType if matched, throw exception otherwise
        // we throw TypeMismatchException instead of InvalidTypeException because this function is used to check an ExpressionOrArrayAst against a declared type
        public Type tryMatchExpression(ExpressionOrArrayAst expressionOrArray, Type expectedType) {
            Objects.requireNonNull(expectedType.typeName());
            expressionOrArray.setType(expectedType);
            return switch (expressionOrArray) {
                case ExpressionAst expression ->
                        build(expression).tryMatch(expectedType); // we use tryMatch just for throwing TypeMismatchException; expectedType is already checked
                case ArrayAst array -> {
                    if (!expectedType.isArray()) {
                        throw new TypeMismatchException("array", expectedType);
                    }
                    Type expectedElementType = expectedType.decreaseDimension();
                    array.elements.forEach(child -> tryMatchExpression(child, expectedElementType));
                    yield expectedType;
                }
            };
        }

        public Type build(ExpressionAst expression) {
            PositionRecord.set(expression.getPosition());
            boolean isLValueTemp = false;
            Type returnType = switch (expression) {
                case ExpressionAst.This ignored -> {
                    if (scope.thisType == null) {
                        throw new CompileException("Use of 'this' outside of class");
                    }
                    yield new Type(scope.thisType);
                }
                case ExpressionAst.Literal literal -> switch (literal.value) {
                    case LiteralEnum.Int ignored -> BuiltinFeatures.INT;
                    case LiteralEnum.Bool ignored -> BuiltinFeatures.BOOL;
                    case LiteralEnum.Str ignored -> BuiltinFeatures.STRING;
                    case LiteralEnum.Null ignored -> NULL;
                };
                case ExpressionAst.FormatString formatString -> {
                    formatString.expressions.forEach(child -> build(child).testType(Type::canFormat, "string, int or bool"));
                    yield BuiltinFeatures.STRING;
                }
                case ExpressionAst.Creator creator -> {
                    Symbol.TypeName typeName = scope.resolveTypeName(creator.typeName);
                    if (creator.arrayCreator == null) {
                        yield new Type(typeName);
                    }
                    ArrayCreatorAst arrayCreator = creator.arrayCreator;
                    Type type = new Type(typeName, arrayCreator.dimensions.size());
                    if (arrayCreator.initializer != null) {
                        arrayCreator.dimensions.forEach(e -> {
                            if (e != null) {
                                throw new CompileException("Brace array initializer should be used with no dimension length specified");
                            }
                        });
                        yield tryMatchExpression(arrayCreator.initializer, type);
                    }
                    if (arrayCreator.dimensions.getFirst() == null) {
                        throw new CompileException("Empty array initializer should be used with at least the first dimension length specified");
                    }
                    boolean startEmpty = false;
                    for (ExpressionAst e : arrayCreator.dimensions) {
                        if (startEmpty) {
                            if (e != null) {
                                throw new CompileException("No dimension length should be specified after an empty dimension");
                            }
                        } else {
                            if (e == null) {
                                startEmpty = true;
                            } else {
                                build(e).testType(BuiltinFeatures.INT);
                            }
                        }
                    }
                    yield type;
                }
                case ExpressionAst.Subscript subscript -> {
                    Type arrayType = build(subscript.expression);
                    if (!arrayType.isArray()) {
                        throw new DimensionOutOfBoundException();
                    }
                    build(subscript.index).testType(BuiltinFeatures.INT);
                    isLValueTemp = true;
                    yield arrayType.decreaseDimension();
                }
                case ExpressionAst.Variable variable -> {
                    isLValueTemp = true;
                    yield variable.expression == null ? scope.resolveVariable(variable.name).type :
                            build(variable.expression).resolveClass().variables.resolve(variable.name).type;
                }
                case ExpressionAst.Function function -> {
                    Symbol.Function functionSymbol = function.expression == null ?
                            scope.resolveFunction(function.name) :
                            build(function.expression).resolveClass().functions.resolve(function.name);
                    if (functionSymbol.parameters.size() != function.arguments.size()) {
                        throw new CompileException(String.format("Function '%s' expects %d arguments, but %d given",
                                function.name, functionSymbol.parameters.size(), function.arguments.size()));
                    }
                    for (int i = 0; i < function.arguments.size(); i++) {
                        tryMatchExpression(function.arguments.get(i), functionSymbol.parameters.get(i).type);
                    }
                    yield functionSymbol.returnType;
                }
                case ExpressionAst.PostUnaryAssign postUnaryAssign ->
                        buildLvalue(postUnaryAssign.expression).getOperationResult(postUnaryAssign.operator);
                case ExpressionAst.PreUnaryAssign preUnaryAssign -> {
                    isLValueTemp = true;
                    yield buildLvalue(preUnaryAssign.expression).getOperationResult(preUnaryAssign.operator);
                }
                case ExpressionAst.PreUnary preUnary ->
                        build(preUnary.expression).getOperationResult(preUnary.operator);
                case ExpressionAst.Binary binary ->
                        build(binary.left).tryMatch(build(binary.right)).getOperationResult(binary.operator);
                case ExpressionAst.Conditional conditional -> {
                    build(conditional.condition).testType(BuiltinFeatures.BOOL);
                    yield build(conditional.trueExpression).tryMatch(build(conditional.falseExpression));
                }
                case ExpressionAst.Assign assign -> {
                    tryMatchExpression(assign.right, buildLvalue(assign.left));
                    yield BuiltinFeatures.VOID;
                }
            };
            isLValue = isLValueTemp;
            expression.type = returnType;
            return returnType;
        }
    }
}