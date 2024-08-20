package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.grammar.MxLexer;
import modist.antlrdemo.frontend.ast.TokenUtil;

import java.util.List;

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

    public static final Symbol.Function ARRAY_SIZE = new Symbol.Function("size", INT, List.of());
    public static final Symbol.Function STRING_LENGTH = new Symbol.Function("length", INT, List.of());
    public static final Symbol.Function STRING_SUBSTRING = new Symbol.Function("substring", STRING, List.of(new Symbol.Variable("left", INT), new Symbol.Variable("right", INT)));
    public static final Symbol.Function STRING_PARSE_INT = new Symbol.Function("parseInt", INT, List.of());
    public static final Symbol.Function STRING_ORD = new Symbol.Function("ord", INT, List.of(new Symbol.Variable("pos", INT)));
    public static final Symbol.Function PRINT = new Symbol.Function("print", VOID, List.of(new Symbol.Variable("str", STRING)));
    public static final Symbol.Function PRINTLN = new Symbol.Function("println", VOID, List.of(new Symbol.Variable("str", STRING)));
    public static final Symbol.Function PRINT_INT = new Symbol.Function("printInt", VOID, List.of(new Symbol.Variable("n", INT)));
    public static final Symbol.Function PRINTLN_INT = new Symbol.Function("printlnInt", VOID, List.of(new Symbol.Variable("n", INT)));
    public static final Symbol.Function GET_STRING = new Symbol.Function("getString", STRING, List.of());
    public static final Symbol.Function GET_INT = new Symbol.Function("getInt", INT, List.of());
    public static final Symbol.Function TO_STRING = new Symbol.Function("toString", STRING, List.of(new Symbol.Variable("i", INT)));

    public static final Symbol.Class INT_CLASS = new Symbol.Class(null, List.of(), List.of());
    public static final Symbol.Class BOOL_CLASS = new Symbol.Class(null, List.of(), List.of());
    public static final Symbol.Class STRING_CLASS = new Symbol.Class(null, List.of(STRING_LENGTH, STRING_SUBSTRING, STRING_PARSE_INT, STRING_ORD), List.of());
    public static final Symbol.Class VOID_CLASS = new Symbol.Class(null, List.of(), List.of());
    public static final Symbol.Class ARRAY_CLASS = new Symbol.Class(null, List.of(ARRAY_SIZE), List.of());
}