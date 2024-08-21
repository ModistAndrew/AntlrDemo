package modist.antlrdemo.frontend.ir;

import modist.antlrdemo.frontend.ast.node.*;
import modist.antlrdemo.frontend.ir.metadata.IrType;
import modist.antlrdemo.frontend.ir.metadata.Register;
import modist.antlrdemo.frontend.ir.node.*;
import modist.antlrdemo.frontend.semantic.BuiltinFeatures;
import modist.antlrdemo.frontend.semantic.Symbol;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// This class is responsible for building the intermediate representation (IR) of the program.
// It visits the AST, read data stored in the AST nodes, and generate IR nodes.
public class IrBuilder {
    private final NamingUtil namingUtil = new NamingUtil();
    private final ProgramIr program = new ProgramIr();
    private final FunctionIr.Builder currentFunction = new FunctionIr.Builder();

    // visit an expression and insert instructions into the current block
    // return the register that holds the result with appropriate type
    private Register visitExpression(ExpressionOrArrayAst expression) {
        return null;
    }

    // visit a definition and store definitions in the program
    private void visitDefinition(DefinitionAst definition) {
        switch (definition) {
            case DefinitionAst.Class classDefinition -> {
                ClassIr classIr = new ClassIr(classDefinition.symbol.irName);
                classDefinition.symbol.definition.variables.getList().forEach(variable -> classIr.members.add(variable.type.irType()));
                program.definitions.add(classIr);
                classDefinition.variables.forEach(this::visitDefinition);
                classDefinition.constructors.forEach(this::visitDefinition);
                classDefinition.functions.forEach(this::visitDefinition);
            }
            case DefinitionAst.Function functionDefinition -> {
                currentFunction.begin(functionDefinition.symbol.irName, functionDefinition.symbol.returnType.irType(),
                        functionDefinition.symbol.parameters.getList().stream().map(variable -> variable.type.irType()).toList());
                functionDefinition.body.forEach(this::visit);
                program.definitions.add(currentFunction.build());
            }
            case DefinitionAst.Variable variableDefinition -> {
                GlobalVariableIr globalVariableIr = new GlobalVariableIr(variableDefinition.symbol.irName, variableDefinition.symbol.type.irType());
                program.definitions.add(globalVariableIr);
                if (variableDefinition.initializer != null) {
                    currentFunction.add(new InstructionIr.Store(visitExpression(variableDefinition.initializer), new Register(variableDefinition.symbol)));
                }
            }
        }
    }

    public void visitProgram(ProgramAst programNode) {
        addBuiltinFeatures();
        currentFunction.begin(NamingUtil.INIT_FUNCTION, IrType.VOID, List.of());
        programNode.variables.forEach(this::visitDefinition);
        program.definitions.add(currentFunction.build()); // add init function
        programNode.functions.forEach(this::visitDefinition);
        programNode.classes.forEach(this::visitDefinition);
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
                function.parameters.getList().stream().map(variable -> variable.type.irType()).toList()));
    }

    private void visit(@Nullable Ast node) {
        // TODO
    }
}

