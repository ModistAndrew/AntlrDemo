package modist.antlrdemo.frontend.ir;

// something is done here rather than in symbol namer
public class IrNamer {
    public static final String FUNCTION_ENTRY = "entry";
    public static final String THIS_VAR = percent("this");
    private int temporaryVariableCounter;
    private int conditionalCounter;
    private int shortCircuitCounter;
    private static int constantStringCounter; // global counter for constant strings

    public String temporaryVariable() {
        return percent(dot("", temporaryVariableCounter++));
    }

    public String conditional() {
        return percent(dot("conditional", conditionalCounter++));
    }

    public String shortCircuit() {
        return percent(dot("shortCircuit", shortCircuitCounter++));
    }

    public static String parameter(String name) {
        return percent(dot("parameter", name));
    }

    public static String constantString() {
        return at(dot("str", constantStringCounter++));
    }

    public static String appendTrue(String name) {
        return dot(name, "true");
    }

    public static String appendFalse(String name) {
        return dot(name, "false");
    }

    public static String appendRight(String name) {
        return dot(name, "right");
    }

    public static String appendEnd(String name) {
        return dot(name, "else");
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
