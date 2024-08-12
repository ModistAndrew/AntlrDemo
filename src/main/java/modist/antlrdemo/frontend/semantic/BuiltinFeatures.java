package modist.antlrdemo.frontend.semantic;

public class BuiltinFeatures {
    public static final Symbol.TypeName INT = new Symbol.TypeName("int");
    public static final Symbol.TypeName BOOL = new Symbol.TypeName("bool");
    public static final Symbol.TypeName STRING = new Symbol.TypeName("string");

    // cache the type instances for convenience
    public static final Type INT_TYPE = new Type(INT);
    public static final Type BOOL_TYPE = new Type(BOOL);
    public static final Type STRING_TYPE = new Type(STRING);

    public static final Symbol.Function ARRAY_SIZE = new Symbol.Function("size", INT_TYPE, new Symbol.Variable[] {});
    public static final Symbol.Function STRING_LENGTH = new Symbol.Function("length", INT_TYPE, new Symbol.Variable[] {});
    public static final Symbol.Function STRING_SUBSTRING = new Symbol.Function("substring", STRING_TYPE, new Symbol.Variable[] {
        new Symbol.Variable("left", INT_TYPE),
        new Symbol.Variable("right", INT_TYPE)
    });
    public static final Symbol.Function STRING_PARSE_INT = new Symbol.Function("parseInt", INT_TYPE, new Symbol.Variable[] {});
    public static final Symbol.Function STRING_ORD = new Symbol.Function("ord", INT_TYPE, new Symbol.Variable[] {
        new Symbol.Variable("pos", INT_TYPE)
    });
    public static final Symbol.Function PRINT = new Symbol.Function("print", null, new Symbol.Variable[] {
        new Symbol.Variable("str", STRING_TYPE)
    });
    public static final Symbol.Function PRINTLN = new Symbol.Function("println", null, new Symbol.Variable[] {
        new Symbol.Variable("str", STRING_TYPE)
    });
    public static final Symbol.Function PRINT_INT = new Symbol.Function("printInt", null, new Symbol.Variable[] {
        new Symbol.Variable("n", INT_TYPE)
    });
    public static final Symbol.Function PRINTLN_INT = new Symbol.Function("printlnInt", null, new Symbol.Variable[] {
        new Symbol.Variable("n", INT_TYPE)
    });
    public static final Symbol.Function GET_STRING = new Symbol.Function("getString", STRING_TYPE, new Symbol.Variable[] {});
    public static final Symbol.Function GET_INT = new Symbol.Function("getInt", INT_TYPE, new Symbol.Variable[] {});
    public static final Symbol.Function TO_STRING = new Symbol.Function("toString", STRING_TYPE, new Symbol.Variable[] {
        new Symbol.Variable("i", INT_TYPE)
    });

    public static final Symbol.Class INT_CLASS = new Symbol.Class("int", null, new Symbol.Function[] {}, new Symbol.Variable[] {});
    public static final Symbol.Class BOOL_CLASS = new Symbol.Class("bool", null, new Symbol.Function[] {}, new Symbol.Variable[] {});
    public static final Symbol.Class STRING_CLASS = new Symbol.Class("string", null, new Symbol.Function[] {
        STRING_LENGTH,
        STRING_SUBSTRING,
        STRING_PARSE_INT,
        STRING_ORD
    }, new Symbol.Variable[] {});
}