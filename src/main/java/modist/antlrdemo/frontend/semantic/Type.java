package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.ast.metadata.Constant;
import modist.antlrdemo.frontend.ast.metadata.Operator;
import modist.antlrdemo.frontend.ir.metadata.IrType;
import modist.antlrdemo.frontend.semantic.error.*;
import modist.antlrdemo.frontend.semantic.scope.Scope;
import modist.antlrdemo.frontend.ast.node.*;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;

public record Type(@Nullable Symbol.TypeName typeName, int dimension) {
    // typeName==null: null type. type is not fixed and can match any non-builtin type. dimension must be 0
    // typeName==VOID: void type. type is fixed and can only match void type. dimension must be 0
    // (VOID, 1) for void pointer in builtin functions
    // (VOID, 0) used for varargs in builtin functions
    public static final Type NULL = new Type(null);

    public Symbol.TypeName resolveTypeName() {
        if (typeName == null) {
            throw new CompileException("No class for null type");
        }
        if (isArray()) {
            return BuiltinFeatures.ARRAY_TYPE_NAME;
        }
        return typeName;
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
        return equals(BuiltinFeatures.VOID);
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

    public IrType irType() {
        if (isInt()) {
            return IrType.I32;
        }
        if (isBool()) {
            return IrType.I1;
        }
        if (isVoid()) {
            return IrType.VOID;
        }
        return IrType.PTR;
    }

    public boolean canFormat() {
        return isInt() || isBool() || isString();
    }

    // return the narrower type if matched, throw exception otherwise
    // this should be a typical situation of TypeMismatchException
    public Type match(Type other) {
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

    // return the result type of the operation on self, throw exception if the operation is invalid
    public Type getOperationResult(Operator op) {
        Type result = op.operate(this);
        if (result != null) {
            return result;
        }
        throw new InvalidTypeException(this, String.format("applicable for operator '%s'", op));
    }

    // throw InvalidTypeException if not valid
    public void test(Type expectedType) {
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
    public void test(Predicate<Type> predicate, String predicateDescription) {
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
        private boolean isLvalue;

        public Builder(Scope scope) {
            this.scope = scope;
        }

        public Type buildLvalue(ExpressionAst expression) {
            Type type = build(expression);
            if (!isLvalue) {
                throw new InvalidTypeException(type, "lvalue");
            }
            return type;
        }

        // array should not be visited directly. must use matchExpression for type information
        // return expectedType if matched, throw exception otherwise
        // we throw TypeMismatchException instead of InvalidTypeException because this function is used to check an ExpressionOrArrayAst against a declared type
        public Type matchExpression(ExpressionOrArrayAst expressionOrArray, Type expectedType) {
            Objects.requireNonNull(expectedType.typeName());
            expressionOrArray.type = expectedType;
            return switch (expressionOrArray) {
                case ExpressionAst expression ->
                        build(expression).match(expectedType); // we use match just for throwing TypeMismatchException; expectedType is already checked
                case ArrayAst array -> {
                    if (!expectedType.isArray()) {
                        throw new TypeMismatchException("array", expectedType);
                    }
                    Type expectedElementType = expectedType.decreaseDimension();
                    array.elements.forEach(child -> matchExpression(child, expectedElementType));
                    yield expectedType;
                }
            };
        }

        public Type build(ExpressionAst expression) {
            PositionRecord.set(expression.getPosition());
            boolean isLvalueTemp = false;
            Type returnType = switch (expression) {
                case ExpressionAst.This ignored -> {
                    if (scope.classType == null) {
                        throw new CompileException("Use of 'this' outside of class");
                    }
                    yield new Type(scope.classType);
                }
                case ExpressionAst.Literal literal -> switch (literal.value) {
                    case Constant.Int ignored -> BuiltinFeatures.INT;
                    case Constant.Bool ignored -> BuiltinFeatures.BOOL;
                    case Constant.Str ignored -> BuiltinFeatures.STRING;
                    case Constant.Null ignored -> NULL;
                };
                case ExpressionAst.FormatString formatString -> {
                    formatString.expressions.forEach(child -> build(child).test(Type::canFormat, "string, int or bool"));
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
                        yield matchExpression(arrayCreator.initializer, type);
                    }
                    if (arrayCreator.dimensions.getFirst() == null) {
                        throw new CompileException("Empty array initializer should be used with at least the first dimension length specified");
                    }
                    boolean startEmpty = false;
                    for (ExpressionAst dimensionLength : arrayCreator.dimensions) {
                        if (startEmpty) {
                            if (dimensionLength != null) {
                                throw new CompileException("No dimension length should be specified after an empty dimension");
                            }
                        } else {
                            if (dimensionLength == null) {
                                startEmpty = true;
                            } else {
                                build(dimensionLength).test(BuiltinFeatures.INT);
                                arrayCreator.presentDimensions.add(dimensionLength);
                            }
                        }
                    }
                    yield type;
                }
                case ExpressionAst.Subscript subscript -> {
                    isLvalueTemp = true;
                    Type arrayType = build(subscript.expression);
                    if (!arrayType.isArray()) {
                        throw new DimensionOutOfBoundException();
                    }
                    build(subscript.index).test(BuiltinFeatures.INT);
                    yield arrayType.decreaseDimension();
                }
                case ExpressionAst.Variable variable -> {
                    isLvalueTemp = true;
                    Symbol.Variable symbol = variable.expression == null ?
                            scope.resolveVariable(variable.name) : build(variable.expression).resolveTypeName().variables.resolve(variable.name);
                    variable.symbol = symbol;
                    yield symbol.type;
                }
                case ExpressionAst.Function function -> {
                    Symbol.Function symbol = function.expression == null ?
                            scope.resolveFunction(function.name) :
                            build(function.expression).resolveTypeName().functions.resolve(function.name);
                    if (symbol.parameters.size() != function.arguments.size()) {
                        throw new CompileException(String.format("Function '%s' expects %d arguments, but %d given",
                                function.name, symbol.parameters.size(), function.arguments.size()));
                    }
                    for (int i = 0; i < function.arguments.size(); i++) {
                        matchExpression(function.arguments.get(i), symbol.parameters.list.get(i).type);
                    }
                    function.symbol = symbol;
                    yield symbol.returnType;
                }
                case ExpressionAst.PostUnaryAssign postUnaryAssign ->
                        buildLvalue(postUnaryAssign.expression).getOperationResult(postUnaryAssign.operator);
                case ExpressionAst.PreUnaryAssign preUnaryAssign -> {
                    isLvalueTemp = true;
                    yield buildLvalue(preUnaryAssign.expression).getOperationResult(preUnaryAssign.operator);
                }
                case ExpressionAst.PreUnary preUnary ->
                        build(preUnary.expression).getOperationResult(preUnary.operator);
                case ExpressionAst.Binary binary ->
                        build(binary.left).match(build(binary.right)).getOperationResult(binary.operator);
                case ExpressionAst.Conditional conditional -> {
                    build(conditional.condition).test(BuiltinFeatures.BOOL);
                    yield build(conditional.trueExpression).match(build(conditional.falseExpression));
                }
                case ExpressionAst.Assign assign -> {
                    matchExpression(assign.right, buildLvalue(assign.left));
                    yield BuiltinFeatures.VOID;
                }
            };
            isLvalue = isLvalueTemp;
            expression.type = returnType;
            return returnType;
        }
    }
}