package modist.antlrdemo.frontend.ir;

import modist.antlrdemo.frontend.ast.node.*;
import modist.antlrdemo.frontend.ir.metadata.Register;
import modist.antlrdemo.frontend.ir.node.*;
import modist.antlrdemo.frontend.semantic.BuiltinFeatures;
import modist.antlrdemo.frontend.semantic.SymbolRenamer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// This class is responsible for building the intermediate representation (IR) of the program.
// It visits the AST, read data stored in the AST nodes, and generate IR nodes.
public class IrBuilder {
    private final ProgramIr program = new ProgramIr();
    private final FunctionIr.Builder currentFunction = new FunctionIr.Builder();

    // visit an expression, insert instructions into the current block, and return the register that holds the result
    private Register visitExpression(ExpressionOrArrayAst expression) {
        return null;
    }

    // visit a definition and store definitions in the program
    private void visitDefinition(DefinitionAst definition) {
        switch (definition) {
            case DefinitionAst.Class classDefinition -> {
                ClassIr classIr = new ClassIr(classDefinition.symbol.irName);
                classDefinition.symbol.definition.variables.getList().forEach(variable -> classIr.members.add(variable.type));
                program.definitions.add(classIr);
                classDefinition.variables.forEach(this::visitDefinition);
                classDefinition.constructors.forEach(this::visitDefinition);
                classDefinition.functions.forEach(this::visitDefinition);
            }
            case DefinitionAst.Function functionDefinition -> {
                currentFunction.begin(functionDefinition.symbol.irName, functionDefinition.symbol.returnType,
                        functionDefinition.symbol.parameters.getList().stream().map(variable -> variable.type).toList());
                functionDefinition.body.forEach(this::visit);
                program.definitions.add(currentFunction.build());
            }
            case DefinitionAst.Variable variableDefinition -> {
                GlobalVariableIr globalVariableIr = new GlobalVariableIr(variableDefinition.symbol.irName, variableDefinition.symbol.type);
                program.definitions.add(globalVariableIr);
                if (variableDefinition.initializer != null) {
                    currentFunction.add(new InstructionIr.Store(visitExpression(variableDefinition.initializer), new Register(variableDefinition.symbol)));
                }
            }
        }
    }

    public void visitProgram(ProgramAst programNode) {
        // TODO: add built-in function declarations
        currentFunction.begin(SymbolRenamer.INIT_FUNCTION, BuiltinFeatures.VOID, List.of());
        programNode.variables.forEach(this::visitDefinition);
        program.definitions.add(currentFunction.build()); // add init function
        programNode.functions.forEach(this::visitDefinition);
        programNode.classes.forEach(this::visitDefinition);
    }

    private void visit(@Nullable Ast node) {
        // TODO
    }
}

