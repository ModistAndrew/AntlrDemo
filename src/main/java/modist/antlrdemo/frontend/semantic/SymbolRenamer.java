package modist.antlrdemo.frontend.semantic;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

// rename symbol and store some other information
public class SymbolRenamer {
    private final Map<String, Integer> variableCounter = new HashMap<>(); // for variable renaming

    public static void setClass(Symbol.TypeName symbol) {
        symbol.irName = percent(dot("class", symbol.name));
    }

    public static void setFunction(Symbol.Function symbol, @Nullable Symbol.TypeName thisType) {
        symbol.thisType = thisType;
        symbol.irName = thisType != null ? at(dot(thisType.name, symbol.name)) : at(symbol.name);
    }

    // used in SemanticChecker
    public void setLocalVariable(Symbol.Variable symbol) {
        symbol.irName = percent(withVariableCounter(symbol.name));
        symbol.memberIndex = -1;
    }

    public static void setParamVariable(Symbol.Variable symbol) {
        symbol.irName = percent(dot("paramAddr", symbol.name));
        symbol.memberIndex = -1;
    }

    public static void setGlobalVariable(Symbol.Variable symbol) {
        symbol.irName = at(symbol.name);
        symbol.memberIndex = -1;
    }

    public static void setMemberVariable(Symbol.Variable symbol, int memberId) {
        symbol.irName = null;
        symbol.memberIndex = memberId;
    }

    private String withVariableCounter(String name) {
        if (!variableCounter.containsKey(name)) {
            variableCounter.put(name, 0);
        }
        int count = variableCounter.get(name);
        variableCounter.put(name, count + 1);
        return dot(name, count);
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
