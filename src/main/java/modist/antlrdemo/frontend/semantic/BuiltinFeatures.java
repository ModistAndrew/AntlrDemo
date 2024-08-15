package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.grammar.MxLexer;
import modist.antlrdemo.frontend.syntax.TokenUtil;

public class BuiltinFeatures {
    public static final Symbol.TypeName INT_TYPE_NAME = new Symbol.TypeName(TokenUtil.getLiteralName(MxLexer.INT));
    public static final Symbol.TypeName BOOL_TYPE_NAME = new Symbol.TypeName(TokenUtil.getLiteralName(MxLexer.BOOL));
    public static final Symbol.TypeName STRING_TYPE_NAME = new Symbol.TypeName(TokenUtil.getLiteralName(MxLexer.STRING));
    public static final Symbol.TypeName VOID_TYPE_NAME = new Symbol.TypeName(TokenUtil.getLiteralName(MxLexer.VOID));

    // cache the type instances for convenience
    public static final Type INT = new Type(INT_TYPE_NAME);
    public static final Type BOOL = new Type(BOOL_TYPE_NAME);
    public static final Type STRING = new Type(STRING_TYPE_NAME);
    public static final Type VOID = new Type(VOID_TYPE_NAME);

    public static final Symbol.Function ARRAY_SIZE = new Symbol.Function("size", INT, new Symbol.Variable[]{});
    public static final Symbol.Function STRING_LENGTH = new Symbol.Function("length", INT, new Symbol.Variable[]{});
    public static final Symbol.Function STRING_SUBSTRING = new Symbol.Function("substring", STRING, new Symbol.Variable[]{
            new Symbol.Variable("left", INT),
            new Symbol.Variable("right", INT)
    });
    public static final Symbol.Function STRING_PARSE_INT = new Symbol.Function("parseInt", INT, new Symbol.Variable[]{});
    public static final Symbol.Function STRING_ORD = new Symbol.Function("ord", INT, new Symbol.Variable[]{
            new Symbol.Variable("pos", INT)
    });
    public static final Symbol.Function PRINT = new Symbol.Function("print", VOID, new Symbol.Variable[]{
            new Symbol.Variable("str", STRING)
    });
    public static final Symbol.Function PRINTLN = new Symbol.Function("println", VOID, new Symbol.Variable[]{
            new Symbol.Variable("str", STRING)
    });
    public static final Symbol.Function PRINT_INT = new Symbol.Function("printInt", VOID, new Symbol.Variable[]{
            new Symbol.Variable("n", INT)
    });
    public static final Symbol.Function PRINTLN_INT = new Symbol.Function("printlnInt", VOID, new Symbol.Variable[]{
            new Symbol.Variable("n", INT)
    });
    public static final Symbol.Function GET_STRING = new Symbol.Function("getString", STRING, new Symbol.Variable[]{});
    public static final Symbol.Function GET_INT = new Symbol.Function("getInt", INT, new Symbol.Variable[]{});
    public static final Symbol.Function TO_STRING = new Symbol.Function("toString", STRING, new Symbol.Variable[]{
            new Symbol.Variable("i", INT)
    });

    public static final Symbol.Class INT_CLASS = new Symbol.Class(INT_TYPE_NAME.name, null, new Symbol.Function[]{}, new Symbol.Variable[]{});
    public static final Symbol.Class BOOL_CLASS = new Symbol.Class(BOOL_TYPE_NAME.name, null, new Symbol.Function[]{}, new Symbol.Variable[]{});
    public static final Symbol.Class STRING_CLASS = new Symbol.Class(STRING_TYPE_NAME.name, null, new Symbol.Function[]{
            STRING_LENGTH,
            STRING_SUBSTRING,
            STRING_PARSE_INT,
            STRING_ORD
    }, new Symbol.Variable[]{});
    public static final Symbol.Class VOID_CLASS = new Symbol.Class(VOID_TYPE_NAME.name, null, new Symbol.Function[]{}, new Symbol.Variable[]{});
    public static final Symbol.Class ARRAY_CLASS = new Symbol.Class("array", null, new Symbol.Function[]{
            ARRAY_SIZE
    }, new Symbol.Variable[]{});
}