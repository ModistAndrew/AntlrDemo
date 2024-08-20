package modist.antlrdemo.frontend.ir;

public class NamingUtil {
    public static final String INIT_FUNCTION = ".init";
    public static final String FUNCTION_ENTRY = "entry";
    private int ifCounter;
    private int loopCounter;
    private int temporaryVariableCounter;

    public String temporaryVariable() {
        return percent(dot("", temporaryVariableCounter++));
    }

    public String ifLabel(String suffix) {
        return dot(dot("if", ifCounter++), suffix);
    }

    public String loopLabel(String suffix) {
        return dot(dot("loop", loopCounter++), suffix);
    }

    public String parameter(String name) {
        return percent(dot(".param", name));
    }

    public void clear() {
        ifCounter = 0;
        loopCounter = 0;
        temporaryVariableCounter = 0;
    }

    private String dot(String prefix, String name) {
        return prefix + "." + name;
    }

    private String dot(String name, int count) {
        return name + "." + count;
    }

    private String percent(String name) {
        return "%" + name;
    }
}
