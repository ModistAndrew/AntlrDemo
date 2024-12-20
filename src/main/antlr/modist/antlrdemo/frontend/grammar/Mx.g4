grammar Mx;

@header {
    package modist.antlrdemo.frontend.grammar;
}

// PARSER

// program and class
program: (classDefinition | variableDefinitions | functionDefinition)*;
classDefinition: CLASS Identifier LBRACE (variableDefinitions | functionDefinition | constructorDefinition)* RBRACE SEMI; // must end with ';'

// function
functionDefinition: type Identifier LPAREN (parameterDefinition (COMMA parameterDefinition)*)? RPAREN block;
constructorDefinition: Identifier emptyParenthesisPair block; // constructor has no formal parameters
parameterDefinition: type Identifier;
block: LBRACE statement* RBRACE;

// statement
statement
    : block # blockStmt
    | variableDefinitions # variableDefinitionsStmt
    | IF condition ifThenStmt=statement (ELSE ifElseStmt=statement)? # ifStmt
    | FOR LPAREN forInit=forInitialization? SEMI forCondition=expression? SEMI forUpdate=expression? RPAREN statement # forStmt
    | WHILE condition statement # whileStmt
    | BREAK SEMI # breakStmt
    | CONTINUE SEMI # continueStmt
    | RETURN expressionOrArray? SEMI # returnStmt
    | expression SEMI # expressionStmt
    | SEMI # emptyStmt
    ;
variableDefinitionsBody: type variableDeclarator (COMMA variableDeclarator)*;
variableDefinitions: variableDefinitionsBody SEMI;
variableDeclarator: Identifier (ASSIGN expressionOrArray)?;
forInitialization: variableDefinitionsBody | expression;

// expression
expression
    : LPAREN expression RPAREN # parenExpr
    | THIS # thisExpr
    | literal # literalExpr
    | formatString # formatStringExpr
    | NEW typeName arrayCreator # creatorExpr
    | NEW Identifier emptyParenthesisPair? # creatorExpr
    | expression expressionBracketPair # subscriptExpr
    | Identifier # variableExpr
    | expression DOT Identifier # variableExpr
    | Identifier argumentList # functionExpr
    | expression DOT Identifier argumentList # functionExpr
    | expression op=(INC|DEC) # postUnaryAssignExpr
    | op=(INC|DEC) expression # preUnaryAssignExpr
    | op=(SUB|NOT|LOGICAL_NOT) expression # preUnaryExpr
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
    | <assoc=right> expression QUESTION expression COLON expression # conditionalExpr
    | <assoc=right> expression ASSIGN expressionOrArray # assignExpr
    ;
arrayCreator: possibleBracketPair+ array?;
expressionOrArray: expression | array;
array: LBRACE (expressionOrArray (COMMA expressionOrArray)*)? RBRACE;
formatString
    : formatStringText+=FormatStringAtom
    | formatStringText+=FormatStringBegin expression (formatStringText+=FormatStringMiddle expression)* formatStringText+=FormatStringEnd
    ;
literal: IntegerLiteral | BooleanLiteral | StringLiteral | NULL;
argumentList: LPAREN (expressionOrArray (COMMA expressionOrArray)*)? RPAREN;
condition: LPAREN expression RPAREN;

// type
type
    : VOID
    | typeName emptyBracketPair*;
typeName: INT | BOOL | STRING | Identifier;
emptyBracketPair: LBRACK RBRACK;
expressionBracketPair: LBRACK expression RBRACK;
possibleBracketPair: LBRACK expression? RBRACK;
emptyParenthesisPair: LPAREN RPAREN;

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
WhiteSpace: [ \t\r\n]+ -> skip;
LineComment: '//' ~[\r\n]* -> skip;
BlockComment: '/*' .*? '*/' -> skip;