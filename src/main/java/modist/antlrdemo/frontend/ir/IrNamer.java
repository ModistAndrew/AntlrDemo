package modist.antlrdemo.frontend.ir;

import java.util.HashMap;
import java.util.Map;

// class naming: %class.(name)
// function naming:
// global functions: @(name)
// member functions: @(class).(name)
// builtin functions: @.(name)
// register naming:
// global variables: @(name)
// constant strings: @string.[count]
// member variables: null
// parameters: %(name).parameter
// this parameter: %this
// parameter variables: %(name).addr
// local variables: %(name).[count]
// temporary variables: %.(prefix).[count]
// label naming: entry or (prefix).[count].(suffix)
// something is done here rather than in symbol namer because it is related to the IR generation
public class IrNamer {
    public static final String FUNCTION_ENTRY = "entry";
    public static final String THIS_VAR = percent("this");
    private int conditionalCounter;
    private int shortCircuitCounter;
    private static int constantStringCounter; // global counter for constant strings
    private final Map<String, Integer> temporaryVariableCounter = new HashMap<>(); // for variable renaming

    public String temporaryVariable(String prefix) {
        return percent(withTemporaryVariableCounter(dot(prefix)));
    }

    public static String parameter(String name) {
        return percent(dot(name, "parameter"));
    }

    public static String constantString() {
        return at(dot("string", constantStringCounter++));
    }

    public String conditional() {
        return dot("conditional", conditionalCounter++);
    }

    public String shortCircuit() {
        return dot("shortCircuit", shortCircuitCounter++);
    }

    public static String append(String name, String suffix) {
        return dot(name, suffix);
    }

    public static String appendCondition(String name) {
        return append(name, "condition");
    }

    public static String appendEnd(String name) {
        return append(name, "end");
    }

    private String withTemporaryVariableCounter(String name) {
        if (!temporaryVariableCounter.containsKey(name)) {
            temporaryVariableCounter.put(name, 0);
        }
        int count = temporaryVariableCounter.get(name);
        temporaryVariableCounter.put(name, count + 1);
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

    private static String dot(String name) {
        return "." + name;
    }
}
