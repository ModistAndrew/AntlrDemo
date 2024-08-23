package modist.antlrdemo.frontend.ir;

import modist.antlrdemo.frontend.ast.metadata.LiteralEnum;
import modist.antlrdemo.frontend.ast.metadata.Operator;
import modist.antlrdemo.frontend.ast.node.*;
import modist.antlrdemo.frontend.ir.metadata.*;
import modist.antlrdemo.frontend.ir.node.*;
import modist.antlrdemo.frontend.BuiltinFeatures;
import modist.antlrdemo.frontend.semantic.Symbol;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// This class is responsible for building the intermediate representation (IR) of the program.
// It visits the AST, read data stored in the AST nodes, and generate IR nodes.
public class IrBuilder {
    private ProgramIr program;
    private final FunctionIr.Builder currentFunction = new FunctionIr.Builder();

    // visit a program and store definitions in the program
    public ProgramIr visitProgram(ProgramAst programNode) {
        program = new ProgramIr();
        addBuiltinFeatures();
        programNode.classes.forEach(this::visitDefinition);
        programNode.functions.forEach(this::visitDefinition);
        beginFunction(BuiltinFeatures._INIT);
        programNode.variables.forEach(this::visitDefinition);
        program.functions.add(currentFunction.build());
        return program;
    }

    private void addBuiltinFeatures() {
        addFunctionDeclaration(BuiltinFeatures.ARRAY_SIZE);
        addFunctionDeclaration(BuiltinFeatures.STRING_LENGTH);
        addFunctionDeclaration(BuiltinFeatures.STRING_SUBSTRING);
        addFunctionDeclaration(BuiltinFeatures.STRING_PARSE_INT);
        addFunctionDeclaration(BuiltinFeatures.STRING_ORD);
        addFunctionDeclaration(BuiltinFeatures.PRINT);
        addFunctionDeclaration(BuiltinFeatures.PRINTLN);
        addFunctionDeclaration(BuiltinFeatures.PRINT_INT);
        addFunctionDeclaration(BuiltinFeatures.PRINTLN_INT);
        addFunctionDeclaration(BuiltinFeatures.GET_STRING);
        addFunctionDeclaration(BuiltinFeatures.GET_INT);
        addFunctionDeclaration(BuiltinFeatures.TO_STRING);
        addFunctionDeclaration(BuiltinFeatures._TO_STRING_BOOL);
        addFunctionDeclaration(BuiltinFeatures._CONCAT_STRING_MULTI);
        addFunctionDeclaration(BuiltinFeatures._MALLOC_CLASS);
        addFunctionDeclaration(BuiltinFeatures._MALLOC_ARRAY);
        addFunctionDeclaration(BuiltinFeatures._MALLOC_ARRAY_MULTI);
        addFunctionDeclaration(BuiltinFeatures._COMPARE_STRING);
        addFunctionDeclaration(BuiltinFeatures._CONCAT_STRING);
    }

    // visit a definition and store definitions in the program
    private void visitDefinition(DefinitionAst definition) {
        switch (definition) {
            case DefinitionAst.Class classDefinition -> {
                program.classes.add(new ClassIr(classDefinition.symbol.irName,
                        classDefinition.symbol.variables.list.stream().map(variable -> variable.type.irType()).toList()));
                classDefinition.constructors.forEach(this::visitDefinition);
                classDefinition.functions.forEach(this::visitDefinition);
            }
            case DefinitionAst.Function functionDefinition -> {
                beginFunction(functionDefinition.symbol);
                if (functionDefinition.symbol.isMain) {
                    callGlobalFunction(BuiltinFeatures._INIT, List.of());
                }
                functionDefinition.parameters.forEach(this::visit);
                functionDefinition.body.forEach(this::visit);
                program.functions.add(currentFunction.build());
            }
            case DefinitionAst.Variable variableDefinition -> { // treat as global variable. use visit() for local variable
                Register result = new Register(variableDefinition.symbol.irName);
                program.globalVariables.add(new GlobalVariableIr(result, variableDefinition.symbol.type.irType()));
                if (variableDefinition.initializer != null) {
                    storePointer(variableDefinition.symbol.type.irType(), visitExpression(variableDefinition.initializer), result);
                }
            }
        }
    }

    // return the pointer to the lvalue rather that the value itself
    private Register visitExpressionLvalue(ExpressionAst expression) {
        return switch (expression) {
            case ExpressionAst.Subscript subscript -> {
                Register array = (Register) visitExpression(subscript.expression);
                Variable index = visitExpression(subscript.index);
                yield currentFunction.add(new InstructionIr.Subscript(currentFunction.createTemporary(),
                        subscript.type.irType(), array, index));
            }
            case ExpressionAst.Variable variable ->
                    variable.symbol.classType == null ? new Register(variable.symbol.irName) :
                            currentFunction.add(new InstructionIr.MemberVariable(currentFunction.createTemporary(),
                                    variable.symbol.classType.irName,
                                    variable.expression == null ? Register.THIS : (Register) visitExpression(variable.expression),
                                    variable.symbol.memberIndex));
            case ExpressionAst.PreUnaryAssign preUnaryAssign -> {
                Register pointer = visitExpressionLvalue(preUnaryAssign.expression);
                Register result = currentFunction.add(new InstructionIr.Bin(currentFunction.createTemporary(),
                        preUnaryAssign.operator.irOperator, IrType.I32, loadPointer(IrType.I32, pointer), new Constant.Int(1)));
                storePointer(IrType.I32, result, pointer);
                yield pointer;
            }
            default -> throw new UnsupportedOperationException();
        };
    }

    // visit an expression and insert instructions into the current block
    // return the register that holds the result with appropriate type, or a constant
    @Nullable
    private Variable visitExpression(ExpressionOrArrayAst expression) {
        return switch (expression) {
            case ArrayAst array -> {
                Register result = callGlobalFunction(BuiltinFeatures._MALLOC_ARRAY, List.of(new Constant.Int(array.elements.size())));
                for (int i = 0; i < array.elements.size(); i++) {
                    ExpressionOrArrayAst element = array.elements.get(i);
                    Register pointer = currentFunction.add(new InstructionIr.Subscript(currentFunction.createTemporary(),
                            element.type.irType(), result, new Constant.Int(i)));
                    storePointer(element.type.irType(), visitExpression(element), pointer);
                }
                yield result;
            }
            case ExpressionAst.This ignored -> Register.THIS;
            case ExpressionAst.Literal literal -> switch (literal.value) {
                case LiteralEnum.Int literalInt -> new Constant.Int(literalInt.value());
                case LiteralEnum.Bool literalBool -> new Constant.Bool(literalBool.value());
                case LiteralEnum.Str literalStr -> addStringConstant(literalStr.value());
                case LiteralEnum.Null ignored -> Constant.Null.INSTANCE;
            };
            case ExpressionAst.FormatString formatString -> {
                List<Variable> arguments = new ArrayList<>();
                arguments.add(new Constant.Int(formatString.texts.size() + formatString.expressions.size()));
                arguments.add(addStringConstant(formatString.texts.getFirst()));
                for (int i = 0; i < formatString.expressions.size(); i++) {
                    arguments.add(toStringExpression(formatString.expressions.get(i)));
                    arguments.add(addStringConstant(formatString.texts.get(i + 1)));
                }
                yield callGlobalFunction(BuiltinFeatures._CONCAT_STRING_MULTI, arguments);
            }
            case ExpressionAst.Creator creator -> {
                if (creator.arrayCreator == null) {
                    Symbol.TypeName typeName = creator.type.resolveTypeName();
                    Register result = callGlobalFunction(BuiltinFeatures._MALLOC_CLASS,
                            List.of(new Constant.Int(typeName.variables.size() * IrType.MAX_BYTE_SIZE)));
                    if (typeName.constructor != null) {
                        callFunction(typeName.constructor, List.of(), result);
                    }
                    yield result;
                }
                ArrayCreatorAst arrayCreator = creator.arrayCreator;
                if (arrayCreator.initializer != null) {
                    yield visitExpression(arrayCreator.initializer); // treat array itself as new
                } else {
                    List<Variable> arguments = new ArrayList<>();
                    arguments.add(new Constant.Int(IrType.MAX_BYTE_SIZE));
                    arguments.add(new Constant.Int(arrayCreator.presentDimensions.size()));
                    arrayCreator.presentDimensions.forEach(dimension -> arguments.add(visitExpression(dimension)));
                    yield callGlobalFunction(BuiltinFeatures._MALLOC_ARRAY_MULTI, arguments);
                }
            }
            case ExpressionAst.Subscript subscript ->
                    loadPointer(subscript.type.irType(), visitExpressionLvalue(subscript));
            case ExpressionAst.Variable variable ->
                    loadPointer(variable.type.irType(), visitExpressionLvalue(variable));
            case ExpressionAst.Function function -> callFunction(function.symbol,
                    function.arguments.stream().map(this::visitExpression).toList(),
                    function.symbol.classType == null ? null : function.expression == null ? Register.THIS : (Register) visitExpression(function.expression));
            case ExpressionAst.PostUnaryAssign postUnaryAssign -> {
                Register pointer = visitExpressionLvalue(postUnaryAssign.expression);
                Register data = loadPointer(IrType.I32, pointer);
                Register result = currentFunction.add(new InstructionIr.Bin(currentFunction.createTemporary(),
                        postUnaryAssign.operator.irOperator, IrType.I32, data, new Constant.Int(1)));
                storePointer(IrType.I32, result, pointer);
                yield data;
            }
            case ExpressionAst.PreUnaryAssign preUnaryAssign ->
                    loadPointer(preUnaryAssign.type.irType(), visitExpressionLvalue(preUnaryAssign));
            case ExpressionAst.PreUnary preUnary -> switch (preUnary.operator) {
                case SUB -> currentFunction.add(new InstructionIr.Bin(currentFunction.createTemporary(),
                        IrOperator.SUB, IrType.I32, new Constant.Int(0), visitExpression(preUnary.expression)));
                case NOT -> currentFunction.add(new InstructionIr.Icmp(currentFunction.createTemporary(),
                        IrOperator.XOR, IrType.I32, new Constant.Int(-1), visitExpression(preUnary.expression)));
                case LOGICAL_NOT -> currentFunction.add(new InstructionIr.Icmp(currentFunction.createTemporary(),
                        IrOperator.XOR, IrType.I1, new Constant.Bool(true), visitExpression(preUnary.expression)));
                default -> throw new IllegalArgumentException();
            };
            case ExpressionAst.Binary binary -> switch (binary.operator) {
                case AND, XOR, OR, SUB, MUL, DIV, MOD, SHL, SHR ->
                        currentFunction.add(new InstructionIr.Bin(currentFunction.createTemporary(),
                                binary.operator.irOperator, binary.type.irType(), visitExpression(binary.left), visitExpression(binary.right)));
                case ADD -> binary.type.isString() ?
                        callGlobalFunction(BuiltinFeatures._CONCAT_STRING, List.of(visitExpression(binary.left), visitExpression(binary.right))) :
                        currentFunction.add(new InstructionIr.Bin(currentFunction.createTemporary(),
                                IrOperator.ADD, binary.type.irType(), visitExpression(binary.left), visitExpression(binary.right)));
                case GT, LT, GE, LE, EQ, NE -> binary.type.isString() ?
                        currentFunction.add(new InstructionIr.Icmp(currentFunction.createTemporary(),
                                binary.operator.irOperator, IrType.I32,
                                callGlobalFunction(BuiltinFeatures._COMPARE_STRING,
                                        List.of(visitExpression(binary.left), visitExpression(binary.right))), new Constant.Int(0))) :
                        currentFunction.add(new InstructionIr.Icmp(currentFunction.createTemporary(),
                                binary.operator.irOperator, binary.type.irType(), visitExpression(binary.left), visitExpression(binary.right)));
                case LOGICAL_AND, LOGICAL_OR -> {
                    boolean isOr = binary.operator == Operator.LOGICAL_OR;
                    String labelName = currentFunction.irNamer.shortCircuit();
                    String rightLabel = IrNamer.appendRight(labelName);
                    String endLabel = IrNamer.appendEnd(labelName);
                    String currentLabel = currentFunction.currentLabel();
                    currentFunction.newBlock(isOr ? new InstructionIr.Br(visitExpression(binary.left), endLabel, rightLabel) :
                            new InstructionIr.Br(visitExpression(binary.left), rightLabel, endLabel), rightLabel);
                    Variable rightValue = visitExpression(binary.right);
                    currentFunction.newBlock(new InstructionIr.Jump(endLabel), endLabel);
                    yield currentFunction.add(new InstructionIr.Phi(currentFunction.createTemporary(),
                            IrType.I1, new Constant.Bool(isOr), currentLabel, rightValue, rightLabel));
                }
                default -> throw new IllegalArgumentException();
            };
            case ExpressionAst.Conditional conditional -> {
                String labelName = currentFunction.irNamer.conditional();
                String trueLabel = IrNamer.appendTrue(labelName);
                String falseLabel = IrNamer.appendFalse(labelName);
                String endLabel = IrNamer.appendEnd(labelName);
                currentFunction.newBlock(new InstructionIr.Br(visitExpression(conditional.condition), trueLabel, falseLabel), trueLabel);
                Variable trueValue = visitExpression(conditional.trueExpression);
                currentFunction.newBlock(new InstructionIr.Jump(endLabel), falseLabel);
                Variable falseValue = visitExpression(conditional.falseExpression);
                currentFunction.newBlock(new InstructionIr.Jump(endLabel), endLabel);
                yield currentFunction.add(new InstructionIr.Phi(currentFunction.createTemporary(),
                        conditional.type.irType(), trueValue, trueLabel, falseValue, falseLabel));
            }
            case ExpressionAst.Assign assign -> {
                Register pointer = visitExpressionLvalue(assign.left);
                storePointer(assign.type.irType(), visitExpression(assign.right), pointer);
                yield null;
            }
        };
    }

    // visit other nodes in a default way
    private void visit(@Nullable Ast node) {
        // TODO
    }

    // add a string constant to the program and return the global variable register
    private Register addStringConstant(String value) {
        Register register = Register.createConstantString();
        program.constantStrings.add(new ConstantStringIr(register, value));
        return register;
    }

    // used in format string
    private Register toStringExpression(ExpressionOrArrayAst expression) {
        Variable expressionResult = visitExpression(expression);
        if (expression.type.isInt()) {
            return callGlobalFunction(BuiltinFeatures.TO_STRING, List.of(expressionResult));
        }
        if (expression.type.isBool()) {
            return callGlobalFunction(BuiltinFeatures._TO_STRING_BOOL, List.of(expressionResult));
        }
        return (Register) expressionResult;
    }

    private Register loadPointer(IrType type, Register pointer) {
        return currentFunction.add(new InstructionIr.Load(currentFunction.createTemporary(), type, pointer));
    }

    private void storePointer(IrType type, Variable value, Register pointer) {
        currentFunction.add(new InstructionIr.Store(type, value, pointer));
    }

    // remember to add this ptr into arguments if necessary
    @Nullable
    private Register callFunction(Symbol.Function symbol, List<Variable> arguments, @Nullable Register thisPointer) {
        Register result = symbol.returnType.isVoid() ? null : currentFunction.createTemporary();
        return currentFunction.add(new InstructionIr.Call(result,
                symbol.returnType.irType(), symbol.irName, getParameterTypes(symbol), getArguments(arguments, thisPointer)));
    }

    @Nullable
    private Register callGlobalFunction(Symbol.Function symbol, List<Variable> arguments) {
        return callFunction(symbol, arguments, null);
    }

    private void beginFunction(Symbol.Function symbol) {
        currentFunction.begin(symbol.irName, symbol.returnType.irType(), getParameters(symbol), getParameterTypes(symbol));
    }

    private void addFunctionDeclaration(Symbol.Function symbol) {
        program.functionDeclarations.add(new FunctionDeclarationIr(symbol.irName, symbol.returnType.irType(), getParameterTypes(symbol)));
    }

    private List<Variable> getArguments(List<Variable> arguments, @Nullable Register thisPointer) {
        List<Variable> result = new ArrayList<>();
        if (thisPointer != null) {
            result.add(thisPointer);
        }
        result.addAll(arguments);
        return result;
    }

    private List<Register> getParameters(Symbol.Function symbol) {
        List<Register> result = new ArrayList<>();
        if (symbol.classType != null) {
            result.add(Register.THIS);
        }
        symbol.parameters.forEach(variable -> result.add(new Register(IrNamer.parameter(variable.name))));
        return result;
    }

    private List<IrType> getParameterTypes(Symbol.Function symbol) {
        List<IrType> result = new ArrayList<>();
        if (symbol.classType != null) {
            result.add(IrType.PTR);
        }
        symbol.parameters.forEach(variable -> result.add(variable.type.irType()));
        return result;
    }
}

