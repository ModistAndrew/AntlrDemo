grammar Mx;

@header {
    package modist.antlrdemo.frontend.grammar;
}

// PARSER

// program and class
program: (SEMI | classDeclaration | variableDeclaration | functionDeclaration)*;
classDeclaration: CLASS Identifier LBRACE (SEMI | variableDeclaration | constructorDeclaration | functionDeclaration)* RBRACE SEMI;

// function
functionDeclaration: (VOID | type) Identifier LPAREN (parameterDeclaration (COMMA parameterDeclaration)*)? RPAREN block;
constructorDeclaration: Identifier emptyParenthesisPair block; // constructor has no formal parameters
parameterDeclaration: type Identifier;
block: LBRACE statement* RBRACE;

// statement
statement
    : block # blockStmt
    | variableDeclaration # variableDeclarationStmt
    | IF condition ifThenStmt=statement (ELSE ifElseStmt=statement)? # ifStmt
    | FOR LPAREN forInit=forInitialization? SEMI forCondition=expression? SEMI forUpdate=expression? RPAREN statement # forStmt
    | WHILE condition statement # whileStmt
    | BREAK SEMI # breakStmt
    | CONTINUE SEMI # continueStmt
    | RETURN expression? SEMI # returnStmt
    | expression SEMI # expressionStmt
    | SEMI # emptyStmt
    ;
variableDeclarationBody: type variableDeclarator (COMMA variableDeclarator)*;
variableDeclaration: variableDeclarationBody SEMI;
variableDeclarator: Identifier (ASSIGN variableInitializer)?;
variableInitializer: arrayInitializer | expression;
forInitialization: variableDeclarationBody | expression;

// expression
expression
    : LPAREN expression RPAREN # parenExpr
    | THIS # thisExpr
    | literal=(IntegerLiteral | BooleanLiteral | StringLiteral | NULL) # literalExpr
    | formatString # formatStringExpr
    | Identifier # identifierExpr
    | NEW creator # newExpr
    | expression expressionBracketPair # arrayAccessExpr
    | expression DOT Identifier # memberAccessExpr
    | expression argumentList # functionCallExpr
    | expression op=(INC|DEC) # postUnaryExpr
    | op=(ADD|SUB|INC|DEC) expression # preUnaryExpr
    | op=(NOT|LOGICAL_NOT) expression # preUnaryExpr
    | expression op=(MUL|DIV|MOD) expression # binaryExpr
    | expression op=(ADD|SUB) expression # binaryExpr
    | expression op=(SHL|SHR) expression # binaryExpr
    | expression op=(GT|LT|GE|LE) expression # binaryExpr
    | expression op=(NE|EQ) expression # binaryExpr
    | expression op=AND expression # binaryExpr
    | expression op=XOR expression # binaryExpr
    | expression op=OR expression # binaryExpr
    | expression op=LOGICAL_AND expression # binaryExpr
    | expression op=LOGICAL_OR expression # binaryExpr
    | expression QUESTION expression COLON expression # conditionalExpr
    | <assoc=right> expression
        op=
        (ASSIGN
        | ADD_ASSIGN
        | SUB_ASSIGN
        | MUL_ASSIGN
        | DIV_ASSIGN
        | MOD_ASSIGN
        | AND_ASSIGN
        | OR_ASSIGN
        | XOR_ASSIGN
        | SHL_ASSIGN
        | SHR_ASSIGN
        )
        expression # assignExpr
    ;
creator: typeName (arrayCreator | emptyParenthesisPair)?; // may omit constructor arguments
arrayCreator
    : expressionBracketPair+ emptyBracketPair* # emptyArrayCreator
    | emptyBracketPair+ arrayInitializer # literalArrayCreator
    ;
arrayInitializer: LBRACE (variableInitializer (COMMA variableInitializer)* (COMMA)? )? RBRACE;
argumentList: LPAREN (expression (COMMA expression)*)? RPAREN;
condition: LPAREN expression RPAREN;

// type
type: typeName emptyBracketPair*;
typeName: typeNameToken=(INT | BOOL | STRING | Identifier);
emptyBracketPair: LBRACK RBRACK;
expressionBracketPair: LBRACK expression RBRACK;
emptyParenthesisPair: LPAREN RPAREN;

// format string
formatString
    : formatStringToken+=FormatStringAtom
    | formatStringToken+=FormatStringBegin expression (formatStringToken+=FormatStringMiddle expression)* formatStringToken+=FormatStringEnd
    ;

// LEXER

// keywords
VOID: 'void';
BOOL: 'bool';
INT: 'int';
STRING: 'string';
NEW: 'new';
CLASS: 'class';
NULL: 'null';
THIS: 'this';
IF: 'if';
ELSE: 'else';
FOR: 'for';
WHILE: 'while';
BREAK: 'break';
CONTINUE: 'continue';
RETURN: 'return';

// literals
IntegerLiteral: '0' | [1-9][0-9]*;

BooleanLiteral: 'true' | 'false';

StringLiteral: '"' StringCharacter* '"';
FormatStringBegin: 'f"' FormatStringCharacter* '$';
FormatStringEnd: '$' FormatStringCharacter* '"';
FormatStringMiddle: '$' FormatStringCharacter* '$';
FormatStringAtom: 'f"' FormatStringCharacter* '"';
fragment StringCharacter: PrintableCharacter | ' ' | EscapeCharacter;
fragment FormatStringCharacter: FormatPrintableCharacter | ' ' | FormatEscapeCharacter;
fragment PrintableCharacter: [!#-~];
fragment FormatPrintableCharacter: [!#%-~];
fragment EscapeCharacter: '\\' [n"\\];
fragment FormatEscapeCharacter
    : '\\' [n"\\]
    | '$$'
    ;

// identifier
Identifier: [a-zA-Z][a-zA-Z0-9_]*;

// operators and punctuators
ADD: '+';
SUB: '-';
MUL: '*';
DIV: '/';
MOD: '%';

GT: '>';
LT: '<';
GE: '>=';
LE: '<=';
NE: '!=';
EQ: '==';

LOGICAL_AND: '&&';
LOGICAL_OR: '||';
LOGICAL_NOT: '!';

SHL: '<<';
SHR: '>>';
AND: '&';
OR: '|';
XOR: '^';
NOT: '~';

ASSIGN: '=';
ADD_ASSIGN: '+=';
SUB_ASSIGN: '-=';
MUL_ASSIGN: '*=';
DIV_ASSIGN: '/=';
MOD_ASSIGN: '%=';
AND_ASSIGN: '&=';
OR_ASSIGN: '|=';
XOR_ASSIGN: '^=';
SHL_ASSIGN: '<<=';
SHR_ASSIGN: '>>=';

INC: '++';
DEC: '--';

DOT: '.';

LBRACK: '[';
RBRACK: ']';

LPAREN: '(';
RPAREN: ')';

QUESTION: '?';
COLON: ':';

SEMI: ';';
COMMA: ',';
LBRACE: '{';
RBRACE: '}';

// whitespace and comments
WhiteSpace: [ \t\n]+ -> skip;
LineComment: '//' ~[\r\n]* -> skip;
BlockComment: '/*' .*? '*/' -> skip;