package modist.antlrdemo.frontend.ir;

import modist.antlrdemo.frontend.ast.metadata.Constant;
import modist.antlrdemo.frontend.ast.metadata.Operator;
import modist.antlrdemo.frontend.ast.node.*;
import modist.antlrdemo.frontend.ir.metadata.*;
import modist.antlrdemo.frontend.ir.node.*;
import modist.antlrdemo.frontend.semantic.BuiltinFeatures;
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
    public ProgramIr visitProgram(ProgramAst programAst) {
        program = new ProgramIr();
        addBuiltinFeatures();
        programAst.classes.forEach(this::visitGlobalDefinition);
        programAst.functions.forEach(this::visitGlobalDefinition);
        beginFunction(BuiltinFeatures.INIT);
        programAst.variables.forEach(this::visitGlobalDefinition);
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
        addFunctionDeclaration(BuiltinFeatures.TO_STRING_BOOL);
        addFunctionVarargsDeclaration(BuiltinFeatures.CONCAT_STRING_MULTI);
        addFunctionDeclaration(BuiltinFeatures.MALLOC_CLASS);
        addFunctionDeclaration(BuiltinFeatures.MALLOC_ARRAY);
        addFunctionVarargsDeclaration(BuiltinFeatures.MALLOC_ARRAY_MULTI);
        addFunctionDeclaration(BuiltinFeatures.COMPARE_STRING);
        addFunctionDeclaration(BuiltinFeatures.CONCAT_STRING);
    }

    // visit a definition and store definitions in the program
    private void visitGlobalDefinition(DefinitionAst definition) {
        switch (definition) {
            case DefinitionAst.Class classDefinition -> {
                program.classDefinitions.add(new ClassDefinitionIr(classDefinition.symbol.irName,
                        classDefinition.symbol.variables.list.stream().map(variable -> variable.type.irType()).toList()));
                classDefinition.constructors.forEach(this::visitGlobalDefinition);
                classDefinition.functions.forEach(this::visitGlobalDefinition);
            }
            case DefinitionAst.Function functionDefinition -> {
                beginFunction(functionDefinition.symbol);
                if (functionDefinition.symbol.isMain) {
                    callGlobalFunction(BuiltinFeatures.INIT);
                }
                functionDefinition.parameters.forEach(parameter -> {
                    this.visit(parameter);
                    storePointer(parameter.symbol.type.irType(), new IrRegister(IrNamer.parameter(parameter.name)), new VariableUse(parameter.symbol.irName));
                });
                functionDefinition.body.forEach(this::visit);
                program.functions.add(currentFunction.build());
            }
            case DefinitionAst.Variable variableDefinition -> { // treat as global variable. use visit() for local variable
                IrGlobal pointer = new IrGlobal(variableDefinition.symbol.irName);
                program.globalVariables.add(new GlobalVariableIr(pointer, variableDefinition.symbol.type.irType()));
                if (variableDefinition.initializer != null) {
                    storePointer(variableDefinition.symbol.type.irType(), visitExpression(variableDefinition.initializer), pointer);
                }
            }
        }
    }

    // return the pointer to the lvalue rather that the value itself
    private IrDynamic visitExpressionLvalue(ExpressionAst expression) {
        return switch (expression) {
            case ExpressionAst.Subscript subscript -> {
                IrDynamic array = (IrDynamic) visitExpression(subscript.expression);
                IrOperand index = visitExpression(subscript.index);
                yield add(new InstructionIr.Subscript(temp("subscript"),
                        subscript.type.irType(), array, index));
            }
            case ExpressionAst.Variable variable -> {
                if (variable.symbol.classType == null) {
                    String irName = variable.symbol.irName;
                    yield IrNamer.isGlobal(irName) ? new IrGlobal(irName) : new VariableUse(irName);
                }
                yield add(new InstructionIr.MemberVariable(temp("member"),
                        variable.symbol.classType.irName,
                        variable.expression == null ? IrRegister.THIS : (IrDynamic) visitExpression(variable.expression),
                        variable.symbol.memberIndex));
            }
            case ExpressionAst.PreUnaryAssign preUnaryAssign -> {
                IrDynamic pointer = visitExpressionLvalue(preUnaryAssign.expression);
                IrRegister result = add(new InstructionIr.Bin(temp(preUnaryAssign.operator.getIrPrefix()),
                        preUnaryAssign.operator.irOperator, IrType.I32, loadPointer(IrType.I32, pointer), new IrConstant.Int(1)));
                storePointer(IrType.I32, result, pointer);
                yield pointer;
            }
            default -> throw new UnsupportedOperationException();
        };
    }

    // visit an expression and insert instructions into the current block
    // return the register that holds the result with appropriate type, or a constant
    // return null for void expressions
    @Nullable
    private IrOperand visitExpression(ExpressionOrArrayAst expression) {
        return switch (expression) {
            case ArrayAst array -> {
                IrRegister result = callGlobalFunction(BuiltinFeatures.MALLOC_ARRAY, new IrConstant.Int(array.elements.size()));
                for (int i = 0; i < array.elements.size(); i++) {
                    ExpressionOrArrayAst element = array.elements.get(i);
                    IrRegister pointer = add(new InstructionIr.Subscript(temp("element"),
                            element.type.irType(), result, new IrConstant.Int(i)));
                    storePointer(element.type.irType(), visitExpression(element), pointer);
                }
                yield result;
            }
            case ExpressionAst.This ignored -> IrRegister.THIS;
            case ExpressionAst.Literal literal -> switch (literal.value) {
                case Constant.Int constantInt -> new IrConstant.Int(constantInt.value());
                case Constant.Bool constantBool -> new IrConstant.Bool(constantBool.value());
                case Constant.Str constantStr -> addStringConstant(constantStr.value());
                case Constant.Null ignored -> IrConstant.Null.NULL;
            };
            case ExpressionAst.FormatString formatString -> {
                List<IrType> argumentTypes = new ArrayList<>();
                List<IrOperand> arguments = new ArrayList<>();
                argumentTypes.add(IrType.I32);
                arguments.add(new IrConstant.Int(formatString.texts.size() + formatString.expressions.size()));
                argumentTypes.add(IrType.PTR);
                arguments.add(addStringConstant(formatString.texts.getFirst()));
                for (int i = 0; i < formatString.expressions.size(); i++) {
                    argumentTypes.add(IrType.PTR);
                    arguments.add(toStringExpression(formatString.expressions.get(i)));
                    argumentTypes.add(IrType.PTR);
                    arguments.add(addStringConstant(formatString.texts.get(i + 1)));
                }
                yield callFunctionVarargs(BuiltinFeatures.CONCAT_STRING_MULTI, argumentTypes, arguments);
            }
            case ExpressionAst.Creator creator -> {
                if (creator.arrayCreator == null) {
                    Symbol.TypeName typeName = creator.type.resolveTypeName();
                    IrRegister result = callGlobalFunction(BuiltinFeatures.MALLOC_CLASS,
                            new IrConstant.Int(typeName.variables.size() * IrType.MAX_BYTE_SIZE));
                    if (typeName.constructor != null) {
                        callFunction(typeName.constructor, List.of(), result);
                    }
                    yield result;
                }
                ArrayCreatorAst arrayCreator = creator.arrayCreator;
                if (arrayCreator.initializer != null) {
                    yield visitExpression(arrayCreator.initializer); // treat array itself as new
                } else {
                    if (arrayCreator.presentDimensions.size() == 1) {
                        yield callGlobalFunction(BuiltinFeatures.MALLOC_ARRAY, visitExpression(arrayCreator.presentDimensions.getFirst()));
                    }
                    List<IrType> argumentTypes = new ArrayList<>();
                    List<IrOperand> arguments = new ArrayList<>();
                    argumentTypes.add(IrType.I32);
                    arguments.add(new IrConstant.Int(arrayCreator.presentDimensions.size()));
                    arrayCreator.presentDimensions.forEach(dimension -> {
                        argumentTypes.add(IrType.I32);
                        arguments.add(visitExpression(dimension));
                    });
                    yield callFunctionVarargs(BuiltinFeatures.MALLOC_ARRAY_MULTI, argumentTypes, arguments);
                }
            }
            case ExpressionAst.Subscript subscript ->
                    loadPointer(subscript.type.irType(), visitExpressionLvalue(subscript));
            case ExpressionAst.Variable variable ->
                    loadPointer(variable.type.irType(), visitExpressionLvalue(variable));
            case ExpressionAst.Function function -> callFunction(function.symbol,
                    function.arguments.stream().map(this::visitExpression).toList(),
                    function.symbol.classType == null ? null :
                            function.expression == null ? IrRegister.THIS : (IrDynamic) visitExpression(function.expression));
            case ExpressionAst.PostUnaryAssign postUnaryAssign -> {
                IrDynamic pointer = visitExpressionLvalue(postUnaryAssign.expression);
                IrOperand data = loadPointer(IrType.I32, pointer);
                IrRegister result = add(new InstructionIr.Bin(temp(postUnaryAssign.operator.getIrPrefix()),
                        postUnaryAssign.operator.irOperator, IrType.I32, data, new IrConstant.Int(1)));
                storePointer(IrType.I32, result, pointer);
                yield data;
            }
            case ExpressionAst.PreUnaryAssign preUnaryAssign ->
                    loadPointer(preUnaryAssign.type.irType(), visitExpressionLvalue(preUnaryAssign));
            case ExpressionAst.PreUnary preUnary -> switch (preUnary.operator) {
                case SUB -> add(new InstructionIr.Bin(temp(preUnary.operator.getIrPrefix()),
                        IrOperator.SUB, IrType.I32, new IrConstant.Int(0), visitExpression(preUnary.expression)));
                case NOT -> add(new InstructionIr.Bin(temp(preUnary.operator.getIrPrefix()),
                        IrOperator.XOR, IrType.I32, new IrConstant.Int(-1), visitExpression(preUnary.expression)));
                case LOGICAL_NOT -> add(new InstructionIr.Bin(temp(preUnary.operator.getIrPrefix()),
                        IrOperator.XOR, IrType.I1, new IrConstant.Bool(true), visitExpression(preUnary.expression)));
                default -> throw new IllegalArgumentException();
            };
            case ExpressionAst.Binary binary -> switch (binary.operator) {
                case AND, XOR, OR, SUB, MUL, DIV, MOD, SHL, SHR ->
                        add(new InstructionIr.Bin(temp(binary.operator.getIrPrefix()),
                                binary.operator.irOperator, binary.left.type.irType(), visitExpression(binary.left), visitExpression(binary.right)));
                case ADD -> binary.left.type.isString() ?
                        callGlobalFunction(BuiltinFeatures.CONCAT_STRING, visitExpression(binary.left), visitExpression(binary.right)) :
                        add(new InstructionIr.Bin(temp(binary.operator.getIrPrefix()),
                                IrOperator.ADD, binary.left.type.irType(), visitExpression(binary.left), visitExpression(binary.right)));
                case GT, LT, GE, LE, EQ, NE -> binary.left.type.isString() ?
                        add(new InstructionIr.Icmp(temp(binary.operator.getIrPrefix()),
                                binary.operator.irOperator, IrType.I32,
                                callGlobalFunction(BuiltinFeatures.COMPARE_STRING,
                                        visitExpression(binary.left), visitExpression(binary.right)), new IrConstant.Int(0))) :
                        add(new InstructionIr.Icmp(temp(binary.operator.getIrPrefix()),
                                binary.operator.irOperator, binary.left.type.irType(), visitExpression(binary.left), visitExpression(binary.right)));
                case LOGICAL_AND, LOGICAL_OR -> {
                    boolean isOr = binary.operator == Operator.LOGICAL_OR;
                    String labelName = currentFunction.irNamer.shortCircuit();
                    String rightLabel = IrNamer.append(labelName, "right");
                    String endLabel = IrNamer.append(labelName, "end");
                    VariableUse resultPointer = new VariableUse(temp("shortCircuitPointer").name());
                    IrOperand leftValue = visitExpression(binary.left);
                    storePointer(IrType.I1, leftValue, resultPointer);
                    currentFunction.newBlock(isOr ?
                            new InstructionIr.Br(leftValue, endLabel, rightLabel, false) :
                            new InstructionIr.Br(leftValue, rightLabel, endLabel, true), rightLabel);
                    storePointer(IrType.I1, visitExpression(binary.right), resultPointer);
                    currentFunction.newBlock(new InstructionIr.Jump(endLabel), endLabel);
                    yield loadPointer(IrType.I1, resultPointer);
                }
                default -> throw new IllegalArgumentException();
            };
            case ExpressionAst.Conditional conditional -> {
                String labelName = currentFunction.irNamer.conditional();
                String trueLabel = IrNamer.append(labelName, "true");
                String falseLabel = IrNamer.append(labelName, "false");
                String endLabel = IrNamer.append(labelName, "end");
                if (!conditional.type.isVoid()) {
                    VariableUse resultPointer = new VariableUse(temp("conditionalPointer").name());
                    currentFunction.newBlock(new InstructionIr.Br(visitExpression(conditional.condition), trueLabel, falseLabel, true), trueLabel);
                    storePointer(conditional.type.irType(), visitExpression(conditional.trueExpression), resultPointer);
                    currentFunction.newBlock(new InstructionIr.Jump(endLabel), falseLabel);
                    storePointer(conditional.type.irType(), visitExpression(conditional.falseExpression), resultPointer);
                    currentFunction.newBlock(new InstructionIr.Jump(endLabel), endLabel);
                    yield loadPointer(conditional.type.irType(), resultPointer);
                } else {
                    currentFunction.newBlock(new InstructionIr.Br(visitExpression(conditional.condition), trueLabel, falseLabel, true), trueLabel);
                    visitExpression(conditional.trueExpression);
                    currentFunction.newBlock(new InstructionIr.Jump(endLabel), falseLabel);
                    visitExpression(conditional.falseExpression);
                    currentFunction.newBlock(new InstructionIr.Jump(endLabel), endLabel);
                    yield null;
                }
            }
            case ExpressionAst.Assign assign -> {
                storePointer(assign.right.type.irType(), visitExpression(assign.right), visitExpressionLvalue(assign.left));
                yield null;
            }
        };
    }

    // visit other nodes in a default way
    private void visit(Ast ast) {
        switch (ast) {
            case ProgramAst ignored -> throw new UnsupportedOperationException();
            case ArrayCreatorAst ignored -> throw new UnsupportedOperationException();
            case DefinitionAst.Variable variableDefinition -> {
                VariableUse pointer = new VariableUse(variableDefinition.symbol.irName);
                IrType type = variableDefinition.symbol.type.irType();
                storePointer(type, variableDefinition.initializer == null ? IrUndefined.UNDEFINED : visitExpression(variableDefinition.initializer), pointer);
            }
            case DefinitionAst.Class ignored -> throw new UnsupportedOperationException();
            case DefinitionAst.Function ignored -> throw new UnsupportedOperationException();
            case ExpressionAst expression -> visitExpression(expression);
            case StatementAst.Block blockStatement -> blockStatement.statements.forEach(this::visit);
            case StatementAst.VariableDefinitions variableDefinitionsStatement ->
                    variableDefinitionsStatement.variables.forEach(this::visit);
            case StatementAst.If ifStatement -> {
                String labelName = ifStatement.labelName;
                String thenLabel = IrNamer.append(labelName, "then");
                String endLabel = IrNamer.append(labelName, "end");
                String elseLabel = ifStatement.elseStatements == null ? endLabel : IrNamer.append(labelName, "else");
                currentFunction.newBlock(new InstructionIr.Br(visitExpression(ifStatement.condition), thenLabel, elseLabel, true), thenLabel);
                ifStatement.thenStatements.forEach(this::visit);
                currentFunction.newBlock(new InstructionIr.Jump(endLabel), elseLabel);
                if (ifStatement.elseStatements != null) {
                    ifStatement.elseStatements.forEach(this::visit);
                    currentFunction.newBlock(new InstructionIr.Jump(endLabel), endLabel);
                }
            }
            case StatementAst.For forStatement -> {
                if (forStatement.initialization != null) {
                    visit(forStatement.initialization);
                }
                String labelName = forStatement.labelName;
                String conditionLabel = IrNamer.append(labelName, "condition");
                String bodyLabel = IrNamer.append(labelName, "body");
                String updateLabel = IrNamer.appendContinue(labelName);
                String endLabel = IrNamer.appendBreak(labelName);
                currentFunction.newBlock(new InstructionIr.Jump(conditionLabel), conditionLabel);
                if (forStatement.condition != null) {
                    currentFunction.newBlock(new InstructionIr.Br(visitExpression(forStatement.condition), bodyLabel, endLabel, true), bodyLabel);
                }
                forStatement.statements.forEach(this::visit);
                if (forStatement.update != null) {
                    currentFunction.newBlock(new InstructionIr.Jump(updateLabel), updateLabel);
                    visitExpression(forStatement.update);
                }
                currentFunction.newBlock(new InstructionIr.Jump(conditionLabel), endLabel);
            }
            case StatementAst.While whileStatement -> {
                String labelName = whileStatement.labelName;
                String conditionLabel = IrNamer.appendContinue(labelName);
                String bodyLabel = IrNamer.append(labelName, "body");
                String endLabel = IrNamer.appendBreak(labelName);
                currentFunction.newBlock(new InstructionIr.Jump(conditionLabel), conditionLabel);
                currentFunction.newBlock(new InstructionIr.Br(visitExpression(whileStatement.condition), bodyLabel, endLabel, true), bodyLabel);
                whileStatement.statements.forEach(this::visit);
                currentFunction.newBlock(new InstructionIr.Jump(conditionLabel), endLabel);
            }
            case StatementAst.Break breakStatement ->
                    add(new InstructionIr.Jump(IrNamer.appendBreak(breakStatement.loopLabelName)));
            case StatementAst.Continue continueStatement ->
                    add(new InstructionIr.Jump(IrNamer.appendContinue(continueStatement.loopLabelName)));
            case StatementAst.Return returnStatement ->
                    add(returnStatement.expression == null ? new InstructionIr.Ret(IrType.VOID, null) :
                            new InstructionIr.Ret(returnStatement.expression.type.irType(), visitExpression(returnStatement.expression)));
            case StatementAst.Expression expressionStatement -> visitExpression(expressionStatement.expression);
            case StatementAst.Empty ignored -> {
            }
            case TypeAst ignored -> throw new UnsupportedOperationException();
            case ArrayAst ignored -> throw new UnsupportedOperationException();
        }
    }

    // add a string constant to the program and return the global variable register
    private IrGlobal addStringConstant(String value) {
        IrGlobal pointer = IrGlobal.createConstantString();
        program.constantStrings.add(new ConstantStringIr(pointer, value));
        return pointer;
    }

    // used in format string
    private IrDynamic toStringExpression(ExpressionOrArrayAst expression) {
        IrOperand expressionResult = visitExpression(expression);
        if (expression.type.isInt()) {
            return callGlobalFunction(BuiltinFeatures.TO_STRING, expressionResult);
        }
        if (expression.type.isBool()) {
            return callGlobalFunction(BuiltinFeatures.TO_STRING_BOOL, expressionResult);
        }
        return (IrDynamic) expressionResult;
    }

    private IrDynamic loadPointer(IrType type, IrDynamic pointer) {
        return switch (pointer) {
            case IrRegister register -> add(new InstructionIr.Load(temp("load"), type, register));
            case IrGlobal global -> add(new InstructionIr.Load(temp("load"), type, global));
            case VariableUse variable -> addVariableReference(new VariableUse(variable.name)); // use a copy every time
        };
    }

    private void storePointer(IrType type, IrOperand value, IrDynamic pointer) {
        switch (pointer) {
            case IrRegister register -> add(new InstructionIr.Store(type, value, register));
            case IrGlobal global -> add(new InstructionIr.Store(type, value, global));
            case VariableUse variable -> addVariableReference(new VariableDef(variable.name, type, value));
        }
    }

    // remember to add this ptr into arguments if necessary
    @Nullable
    private IrRegister callFunction(Symbol.Function symbol, List<IrOperand> arguments, @Nullable IrDynamic thisPointer) {
        IrRegister result = symbol.returnType.isVoid() ? null : temp("call");
        return add(new InstructionIr.Call(result,
                symbol.returnType.irType(), symbol.irName, getParameterTypes(symbol), getArguments(arguments, thisPointer)));
    }

    @Nullable
    private IrRegister callGlobalFunction(Symbol.Function symbol, IrOperand... arguments) {
        return callFunction(symbol, List.of(arguments), null);
    }

    @Nullable
    private IrRegister callFunctionVarargs(Symbol.Function symbol, List<IrType> argumentTypes, List<IrOperand> arguments) {
        IrRegister result = symbol.returnType.isVoid() ? null : temp("call");
        return add(new InstructionIr.CallVarargs(result,
                symbol.returnType.irType(), symbol.irName, getParameterTypes(symbol), argumentTypes, arguments));
    }

    private void beginFunction(Symbol.Function symbol) {
        currentFunction.begin(symbol.irName, symbol.returnType.irType(), getParameters(symbol), getParameterTypes(symbol));
    }

    private void addFunctionDeclaration(Symbol.Function symbol) {
        program.functionDeclarations.add(new FunctionDeclarationIr(symbol.irName, symbol.returnType.irType(), getParameterTypes(symbol)));
    }

    private void addFunctionVarargsDeclaration(Symbol.Function symbol) {
        program.functionVarargsDeclarations.add(new FunctionVarargsDeclarationIr(symbol.irName, symbol.returnType.irType(), getParameterTypes(symbol)));
    }

    private List<IrOperand> getArguments(List<IrOperand> arguments, @Nullable IrDynamic thisPointer) {
        List<IrOperand> result = new ArrayList<>();
        if (thisPointer != null) { // add this ptr into arguments if necessary
            result.add(thisPointer);
        }
        result.addAll(arguments);
        return result;
    }

    private List<IrRegister> getParameters(Symbol.Function symbol) {
        List<IrRegister> result = new ArrayList<>();
        if (symbol.classType != null) { // add this ptr into arguments if necessary
            result.add(IrRegister.THIS);
        }
        symbol.parameters.list.forEach(variable -> result.add(new IrRegister(IrNamer.parameter(variable.name))));
        return result;
    }

    private List<IrType> getParameterTypes(Symbol.Function symbol) {
        List<IrType> result = new ArrayList<>();
        if (symbol.classType != null) { // add this ptr into arguments if necessary
            result.add(IrType.PTR);
        }
        symbol.parameters.list.forEach(variable -> result.add(variable.type.irType()));
        return result;
    }

    private void add(InstructionIr instruction) {
        currentFunction.add(instruction);
    }

    private IrRegister add(InstructionIr.Result instruction) {
        currentFunction.add(instruction);
        return instruction.result();
    }

    private void addVariableReference(VariableDef variableDef) {
        currentFunction.addVariableReference(variableDef);
    }

    private VariableUse addVariableReference(VariableUse variableUse) {
        currentFunction.addVariableReference(variableUse);
        return variableUse; // return the same object to provide a link to the variable
    }

    private IrRegister temp(String prefix) {
        return currentFunction.createTemporary(prefix);
    }
}

