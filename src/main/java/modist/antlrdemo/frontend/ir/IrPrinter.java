package modist.antlrdemo.frontend.ir;

import modist.antlrdemo.frontend.ir.metadata.IrType;
import modist.antlrdemo.frontend.ir.metadata.Variable;

import java.util.List;
import java.util.stream.IntStream;

public class IrPrinter {
    public static String toStringTypes(List<IrType> types) {
        return String.join(", ", types.stream().map(Object::toString).toList());
    }

    public static String toStringTypesVarargs(List<IrType> types) {
        return String.join(", ", types.stream().map(Object::toString).toList()) + ", ...";
    }

    public static String toStringArguments(List<IrType> argumentTypes, List<? extends Variable> arguments) {
        return String.join(", ",
                IntStream.range(0, argumentTypes.size()).mapToObj(i -> String.format("%s %s", argumentTypes.get(i), arguments.get(i)))
                        .toList());
    }

    public static String toStringPhiPairs(List<Variable> values, List<String> labels) {
        return String.join(", ",
                IntStream.range(0, values.size()).mapToObj(i -> String.format("[ %s, %s ]", values.get(i), labels.get(i)))
                        .toList());
    }

    public static String escape(String str) {
        return str.replace("\\", "\\\\").replace("\n", "\\0A").replace("\"", "\\22");
    }
}
