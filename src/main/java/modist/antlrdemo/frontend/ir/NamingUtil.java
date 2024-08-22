package modist.antlrdemo.frontend.ir;

public class NamingUtil {
    public static final String INIT_FUNCTION = "@.init";
    public static final String FUNCTION_ENTRY = "entry";
    public static final String THIS_VAR = "%this";
    private int ifCounter;
    private int loopCounter;
    private int temporaryVariableCounter;
    private static int CONSTANT_STRING_COUNTER; // global counter for constant strings

    public String temporaryVariable() {
        return percent(dot("", temporaryVariableCounter++));
    }

    public String ifLabel(String suffix) {
        return dot(dot("if", ifCounter++), suffix);
    }

    public String loopLabel(String suffix) {
        return dot(dot("loop", loopCounter++), suffix);
    }

    public static String parameter(String name) {
        return percent(dot("param", name));
    }

    public static String constantString() {
        return at(dot("str", CONSTANT_STRING_COUNTER++));
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
