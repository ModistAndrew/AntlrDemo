package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.ast.node.StatementAst;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

// rename symbol and label names
public class SemanticNamer {
    private final Map<String, Integer> variableCounter = new HashMap<>(); // for variable renaming
    private int ifCounter;
    private int loopCounter;

    public static void setClass(Symbol.TypeName symbol) {
        symbol.irName = percent(dot("class", symbol.name));
    }

    public static void setFunction(Symbol.Function symbol, @Nullable Symbol.TypeName classType) {
        symbol.irName = classType != null ? at(dot(classType.name, symbol.name)) : at(symbol.name);
    }

    public static void setGlobalVariable(Symbol.Variable symbol) {
        symbol.irName = at(symbol.name);
    }

    // used in SemanticChecker
    public void setLocalVariable(Symbol.Variable symbol) {
        symbol.irName = percent(withVariableCounter(symbol.name));
    }

    private String withVariableCounter(String name) {
        if (!variableCounter.containsKey(name)) {
            variableCounter.put(name, 0);
        }
        int count = variableCounter.get(name);
        variableCounter.put(name, count + 1);
        return dot(name, count);
    }

    public void setIf(StatementAst.If ifNode) {
        ifNode.labelName = ifLabel();
    }

    public void setFor(StatementAst.For forNode) {
        forNode.labelName = loopLabel();
    }

    public void setWhile(StatementAst.While whileNode) {
        whileNode.labelName = loopLabel();
    }

    private String ifLabel() {
        return dot("if", ifCounter++);
    }

    private String loopLabel() {
        return dot("loop", loopCounter++);
    }

    private static String dot(String prefix, String name) {
        return prefix + "." + name;
    }

    private static String dot(String name, int count) {
        return name + "." + count;
    }

    private static String percent(String name) {
        return "%" + name;
    }

    private static String at(String name) {
        return "@" + name;
    }
}
