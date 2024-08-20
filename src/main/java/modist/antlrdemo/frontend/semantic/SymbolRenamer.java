package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.semantic.scope.Scope;

import java.util.HashMap;
import java.util.Map;

public class SymbolRenamer {
    public static final String INIT_FUNCTION = ".init";
    public static final String FUNCTION_ENTRY = "entry";
    private final Map<String, Integer> variableCounter = new HashMap<>(); // for variable renaming
    private int ifCounter;
    private int loopCounter;

    public String className(Symbol.TypeName symbol) {
        symbol.irName = dot("class", symbol.name);
        return symbol.irName;
    }

    public String functionName(Scope scope, String name) {
        return scope.thisType == null ? name : dot(scope.thisType.name, name);
    }

    public String parameterName(String name) {
        return dot(".param", name);
    }

    public String localVariableName(String name) {
        return withVariableCounter(name);
    }

    public String temporaryVariableName() {
        return withVariableCounter(".");
    }

    public String ifLabelName(String suffix) {
        return dot(dot("if", ifCounter++), suffix);
    }

    public String loopLabelName(String suffix) {
        return dot(dot("loop", loopCounter++), suffix);
    }

    public void clear() {
        variableCounter.clear();
        ifCounter = 0;
        loopCounter = 0;
    }

    private String dot(Object prefix, Object name) {
        return prefix + "." + name;
    }

    // return name.previous_count and increment counter[name]
    private String withVariableCounter(String name) {
        if (!variableCounter.containsKey(name)) {
            variableCounter.put(name, 0);
        }
        int count = variableCounter.get(name);
        variableCounter.put(name, count + 1);
        return dot(name, count);
    }
}
