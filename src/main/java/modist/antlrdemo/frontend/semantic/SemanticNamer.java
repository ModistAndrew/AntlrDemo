package modist.antlrdemo.frontend.semantic;

import java.util.HashMap;
import java.util.Map;

// rename symbol and label names
public class SemanticNamer {
    private final Map<String, Integer> variableCounter = new HashMap<>(); // for variable renaming
    private int ifCounter;
    private int loopCounter;

    public static String typeName(String name) {
        return percent(dot("class", name));
    }

    public static String globalFunction(String name) {
        return at(name);
    }

    public static String memberFunction(String name, String prefix) {
        return at(dot(prefix, name));
    }

    public static String globalVariable(String name) {
        return at(name);
    }

    public static String parameterVariable(String name) {
        return percent(dot(name, "addr"));
    }

    // used in SemanticChecker
    public String localVariable(String name) {
        return percent(withVariableCounter(name));
    }

    private String withVariableCounter(String name) {
        if (!variableCounter.containsKey(name)) {
            variableCounter.put(name, 0);
        }
        int count = variableCounter.get(name);
        variableCounter.put(name, count + 1);
        return dot(name, count);
    }

    public String ifLabel() {
        return dot("if", ifCounter++);
    }

    public String loopLabel() {
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
