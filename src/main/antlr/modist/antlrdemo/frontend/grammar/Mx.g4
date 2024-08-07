grammar Mx;

@header {
    package modist.antlrdemo.frontend.grammar;
}

// PARSER

// program and class
program: (classDeclaration | variableDeclaration | functionDeclaration)*;
classDeclaration: CLASS Identifier classBody SEMI;
classBody: LBRACE memberDeclaration* RBRACE;
memberDeclaration: SEMI | variableDeclaration | constructorDeclaration | functionDeclaration;

// function
functionDeclaration: returnType Identifier formalParameterList block;
constructorDeclaration: Identifier formalParameterList block;
formalParameterList: LPAREN (formalParameter (COMMA formalParameter)*)? RPAREN;
formalParameter: type Identifier;
block: LBRACE statement* RBRACE;

// statement
statement
    : block # blockStmt
    | variableDeclaration # variableDeclarationStmt
    | IF condition trueStatement=statement (ELSE falseStatement=statement)? # ifStmt
    | FOR LPAREN forControl RPAREN statement # forStmt
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
forControl: forInit=forInitialization? SEMI forCondition=expression? SEMI forUpdate=expressions?;
forInitialization: variableDeclarationBody | expressions;

// expression
expression
    : LPAREN expression RPAREN # parenExpr
    | THIS # thisExpr
    | literal # literalExpr
    | formatString # formatStringExpr
    | Identifier # identifierExpr
    | NEW creator # newExpr
    | expression expressionBrackets # arrayAccessExpr
    | expression DOT Identifier # memberAccessExpr
    | expression arguments # functionCallExpr
    | expression op=(INC|DEC) # postUnaryExpr
    | op=(ADD|SUB|INC|DEC) expression # preUnaryExpr
    | op=(NOT|LOGICAL_NOT) expression # preUnaryExpr
    | expression op=(MUL|DIV|MOD) expression # binaryExpr
    | expression op=(ADD|SUB) expression # binaryExpr
    | expression op=(LSHIFT|RSHIFT) expression # binaryExpr
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
        | LSHIFT_ASSIGN
        | RSHIFT_ASSIGN
        )
        expression # assignExpr
    ;
literal: IntegerLiteral | BoolLiteral | StringLiteral | NULL;
creator: typeName creatorBody?; // may omit constructor arguments
creatorBody: arrayCreatorBody | arguments;
arrayCreatorBody
    : emptyBrackets+ arrayInitializer # literalArrayCreator
    | expressionBrackets+ emptyBrackets* # emptyArrayCreator
    ;
arrayInitializer: LBRACE (variableInitializer (COMMA variableInitializer)* (COMMA)? )? RBRACE;
arguments: LPAREN expressions? RPAREN;
expressions: expression (COMMA expression)*;
condition: LPAREN expression RPAREN;

// type
type: typeName emptyBrackets*;
typeName: INT | BOOL | STRING | Identifier;
returnType: VOID | type;
emptyBrackets: LBRACK RBRACK;
expressionBrackets: LBRACK expression RBRACK;

// format string
formatString
    : FormatStringAtom # atomFormatString
    | FormatStringBegin expression (FormatStringMiddle expression)* FormatStringEnd # complexFormatString
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

BoolLiteral: 'true' | 'false';

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
    : '\\' [n"\\$]
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

RSHIFT: '>>';
LSHIFT: '<<';
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
LSHIFT_ASSIGN: '<<=';
RSHIFT_ASSIGN: '>>=';

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
WS: [ \t\n]+ -> skip;
LineComment: '//' ~[\r\n]* -> skip;
BlockComment: '/*' .*? '*/' -> skip;