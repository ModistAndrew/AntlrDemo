package modist.antlrdemo.frontend.ir;

import modist.antlrdemo.frontend.ast.metadata.LiteralEnum;
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
    private final ProgramIr program = new ProgramIr();
    private final FunctionIr.Builder currentFunction = new FunctionIr.Builder();

    // add a string constant to the program and return the global variable register
    private Register addStringConstant(String value) {
        Register register = Register.createConstantString();
        program.constantStrings.add(new ConstantStringIr(register, value));
        return register;
    }

    private Register toStringExpression(ExpressionOrArrayAst expression) {
        Variable expressionResult = visitExpression(expression);
        if (expression.type.isInt()) {
            return callFunction(BuiltinFeatures.TO_STRING, List.of(expressionResult));
        }
        if (expression.type.isBool()) {
            return callFunction(BuiltinFeatures._TO_STRING_BOOL, List.of(expressionResult));
        }
        return (Register) expressionResult; // string must be a register
    }

    private Register callFunction(Symbol.Function function, List<Variable> arguments) {
        return currentFunction.add(new InstructionIr.Call(currentFunction.createTemporary(),
                function.returnType.irType(), function.irName,
                function.parameters.list.stream().map(variable -> variable.type.irType()).toList(), arguments));
    }

    private Register loadPointer(IrType type, Register pointer) {
        return currentFunction.add(new InstructionIr.Load(currentFunction.createTemporary(), type, pointer));
    }

    // return the pointer to the lvalue rather that the value itself
    private Register visitExpressionLvalue(ExpressionAst expression) {
        return switch (expression) {
            case ExpressionAst.Subscript subscript -> {
                Register array = (Register) visitExpression(subscript.expression); // array must be a register
                Variable index = visitExpression(subscript.index);
                yield currentFunction.add(new InstructionIr.Subscript(currentFunction.createTemporary(),
                        subscript.expression.type.irTypePointingTo(), array, index));
            }
            case ExpressionAst.Variable variable -> variable.classType == null ? new Register(variable.symbol.irName) :
                    currentFunction.add(new InstructionIr.MemberVariable(currentFunction.createTemporary(),
                            variable.classType.irTypePointingTo(),
                            variable.expression == null ? Register.THIS : (Register) visitExpression(variable.expression),
                            variable.symbol.memberIndex));
            case ExpressionAst.PreUnaryAssign preUnaryAssign -> {
                Register pointer = visitExpressionLvalue(preUnaryAssign.expression);
                Register data = loadPointer(IrType.I32, pointer);
                Register result = currentFunction.add(new InstructionIr.Bin(currentFunction.createTemporary(),
                        preUnaryAssign.operator.irOperator, IrType.I32, data, new Constant.Int(1)));
                currentFunction.add(new InstructionIr.Store(IrType.I32, result, pointer));
                yield pointer;
            }
            default -> throw new UnsupportedOperationException();
        };
    }

    // visit an expression and insert instructions into the current block
    // return the register that holds the result with appropriate type, or a constant
    private Variable visitExpression(ExpressionOrArrayAst expression) {
        return switch (expression) {
            case ArrayAst array -> {
                Register result = callFunction(BuiltinFeatures._MALLOC_ARRAY, List.of(new Constant.Int(array.elements.size())));
                for (ExpressionOrArrayAst element : array.elements) {
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
                arguments.add(addStringConstant(formatString.texts.getFirst()));
                for (int i = 0; i < formatString.expressions.size(); i++) {
                    arguments.add(toStringExpression(formatString.expressions.get(i)));
                    arguments.add(addStringConstant(formatString.texts.get(i + 1)));
                }
                arguments.addFirst(new Constant.Int(arguments.size())); // number of strings to be concatenated
                yield callFunction(BuiltinFeatures._CONCAT_STRINGS, arguments);
            }
            case ExpressionAst.Creator creator -> {
                if (creator.arrayCreator == null) {
                    Register result = callFunction(BuiltinFeatures._MALLOC_CLASS,
                            List.of(new Constant.Int(creator.type.typeName().variables.size() * IrType.MAX_BYTE_SIZE)));
                }
                ArrayCreatorAst arrayCreator = creator.arrayCreator;
                if (arrayCreator.initializer != null) {
                    yield visitExpression(arrayCreator.initializer); // treat array itself as new
                } else {
                    List<Variable> arguments = new ArrayList<>();
                    arguments.add(new Constant.Int(IrType.MAX_BYTE_SIZE));
                    arguments.add(new Constant.Int(arrayCreator.presentDimensions.size()));
                    arrayCreator.presentDimensions.forEach(dimension -> arguments.add(visitExpression(dimension)));
                    yield callFunction(BuiltinFeatures._MALLOC_ARRAY_MULTI, arguments);
                }
            }
            case ExpressionAst.Subscript subscript -> null;
            case ExpressionAst.Variable variable -> null;
            default -> null;
        };
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
                List<Register> parameters = new ArrayList<>();
                List<IrType> parameterTypes = new ArrayList<>();
                if (functionDefinition.symbol.thisType != null) {
                    parameters.add(Register.THIS);
                    parameterTypes.add(IrType.PTR);
                }
                for (Symbol.Variable variable : functionDefinition.symbol.parameters.list) {
                    parameters.add(new Register(NamingUtil.parameter(variable.name)));
                    parameterTypes.add(variable.type.irType());
                }
                currentFunction.begin(functionDefinition.symbol.irName, functionDefinition.symbol.returnType.irType(), parameters, parameterTypes);
                functionDefinition.parameters.forEach(this::visit);
                functionDefinition.body.forEach(this::visit);
                program.functions.add(currentFunction.build());
            }
            case DefinitionAst.Variable variableDefinition -> { // treat as global variable. use visit() for local variable
                GlobalVariableIr globalVariableIr = new GlobalVariableIr(new Register(variableDefinition.symbol.irName), variableDefinition.symbol.type.irType());
                program.globalVariables.add(globalVariableIr);
                if (variableDefinition.initializer != null) {
                    currentFunction.add(new InstructionIr.Store(variableDefinition.symbol.type.irType(), visitExpression(variableDefinition.initializer), currentFunction.createTemporary()));
                }
            }
        }
    }

    // visit a program and store definitions in the program
    public ProgramIr visitProgram(ProgramAst programNode) {
        addBuiltinFeatures();
        programNode.classes.forEach(this::visitDefinition);
        programNode.functions.forEach(this::visitDefinition);
        currentFunction.begin(NamingUtil.INIT_FUNCTION, IrType.VOID, List.of(), List.of());
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
    }

    private void addFunctionDeclaration(Symbol.Function function) {
        program.functionDeclarations.add(new FunctionDeclarationIr(function.irName, function.returnType.irType(),
                function.parameters.list.stream().map(variable -> variable.type.irType()).toList()));
    }

    // visit other nodes in a default way
    private void visit(@Nullable Ast node) {
        // TODO
    }
}

