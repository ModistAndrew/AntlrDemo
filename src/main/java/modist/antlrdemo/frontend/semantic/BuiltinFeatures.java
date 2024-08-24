package modist.antlrdemo.frontend.semantic;

import modist.antlrdemo.frontend.grammar.MxLexer;
import modist.antlrdemo.frontend.ast.TokenUtil;

public class BuiltinFeatures {
    public static final Symbol.TypeName INT_TYPE_NAME = new Symbol.TypeName(TokenUtil.getLiteralName(MxLexer.INT));
    public static final Symbol.TypeName BOOL_TYPE_NAME = new Symbol.TypeName(TokenUtil.getLiteralName(MxLexer.BOOL));
    public static final Symbol.TypeName STRING_TYPE_NAME = new Symbol.TypeName(TokenUtil.getLiteralName(MxLexer.STRING));
    public static final Symbol.TypeName VOID_TYPE_NAME = new Symbol.TypeName(TokenUtil.getLiteralName(MxLexer.VOID));
    public static final Symbol.TypeName ARRAY_TYPE_NAME = new Symbol.TypeName(".array"); // a virtual type for arrays not stored in the symbol table

    // cache the type instances for convenience
    public static final Type INT = new Type(INT_TYPE_NAME);
    public static final Type BOOL = new Type(BOOL_TYPE_NAME);
    public static final Type STRING = new Type(STRING_TYPE_NAME);
    public static final Type VOID = new Type(VOID_TYPE_NAME);

    public static final Symbol.Function ARRAY_SIZE = new Symbol.Function("size", ARRAY_TYPE_NAME, INT);
    public static final Symbol.Function STRING_LENGTH = new Symbol.Function("length", STRING_TYPE_NAME, INT);
    public static final Symbol.Function STRING_SUBSTRING = new Symbol.Function("substring", STRING_TYPE_NAME, STRING, INT, INT);
    public static final Symbol.Function STRING_PARSE_INT = new Symbol.Function("parseInt", STRING_TYPE_NAME, INT);
    public static final Symbol.Function STRING_ORD = new Symbol.Function("ord", STRING_TYPE_NAME, INT, INT);
    public static final Symbol.Function PRINT = new Symbol.Function("print", VOID, STRING);
    public static final Symbol.Function PRINTLN = new Symbol.Function("println", VOID, STRING);
    public static final Symbol.Function PRINT_INT = new Symbol.Function("printInt", VOID, INT);
    public static final Symbol.Function PRINTLN_INT = new Symbol.Function("printlnInt", VOID, INT);
    public static final Symbol.Function GET_STRING = new Symbol.Function("getString", STRING);
    public static final Symbol.Function GET_INT = new Symbol.Function("getInt", INT);
    public static final Symbol.Function TO_STRING = new Symbol.Function("toString", STRING, INT);

    private static final Type PTR = new Type(VOID_TYPE_NAME, 1);

    public static final Symbol.Function _TO_STRING_BOOL = new Symbol.Function(".toStringBool", STRING, BOOL);
    public static final Symbol.Function _CONCAT_STRING_MULTI = new Symbol.Function(".concatStringMulti", STRING, INT);
    public static final Symbol.Function _MALLOC_CLASS = new Symbol.Function(".mallocClass", PTR, INT);
    public static final Symbol.Function _MALLOC_ARRAY = new Symbol.Function(".mallocArray", PTR, INT, INT);
    public static final Symbol.Function _MALLOC_ARRAY_MULTI = new Symbol.Function(".mallocArrayMulti", PTR, INT, INT);
    public static final Symbol.Function _COMPARE_STRING = new Symbol.Function(".compareString", INT, STRING, STRING);
    public static final Symbol.Function _CONCAT_STRING = new Symbol.Function(".concatString", STRING, STRING, STRING);

    public static final Symbol.Function _INIT = new Symbol.Function(".init", VOID);
}