grammar Mx;

// PARSER

// program and class
program: (classDeclaration | variableDeclaration | functionDeclaration)*;
classDeclaration: CLASS Identifier classBody SEMI;
classBody: LBRACE memberDeclaration* RBRACE;
memberDeclaration: SEMI | variableDeclaration | constructorDeclaration | functionDeclaration;

// function
functionDeclaration: (VOID | type) Identifier parFormalParameters block;
constructorDeclaration: Identifier parFormalParameters block;
parFormalParameters: LPAREN formalParameters? RPAREN;
formalParameters: formalParameter (COMMA formalParameter)*;
formalParameter: type Identifier;
block: LBRACE statement* RBRACE;

// statement
statement:
    block
    | variableDeclaration
    | IF parExpression statement (ELSE statement)?
    | FOR LPAREN forControl RPAREN statement
    | WHILE parExpression statement
    | BREAK SEMI
    | CONTINUE SEMI
    | RETURN expression? SEMI
    | expression SEMI;
variableDeclaration: type variableDeclarators SEMI;
variableDeclarators: variableDeclarator (COMMA variableDeclarator)*;
variableDeclarator: Identifier (ASSIGN variableInitializer)?;
variableInitializer: arrayInitializer | expression;
forControl: (variableDeclaration | expressions)? SEMI expression? SEMI expressions?;

// expression
expression:
    LPAREN expression RPAREN
    | THIS
    | literal
    | Identifier // variable
    | expression LBRACK expression RBRACK // array access
    | expression DOT Identifier // member variable or function access
    | expression parExpressions // function call
    | NEW creator
    | expression (INC|DEC)
    | (ADD|SUB|INC|DEC) expression
    | (NOT|LOGICAL_NOT) expression
    | expression (MUL|DIV|MOD) expression
    | expression (ADD|SUB) expression
    | expression (LSHIFT|RSHIFT) expression
    | expression (GT|LT|GE|LE) expression
    | expression (NE|EQ) expression
    | expression AND expression
    | expression XOR expression
    | expression OR expression
    | expression LOGICAL_AND expression
    | expression LOGICAL_OR expression
    | expression QUESTION expression COLON expression
    | <assoc=right> expression
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
        | RSHIFT_ASSIGN)
        expression;
literal: IntegerLiteral | BoolLiteral | StringLiteral | NULL;
creator: createdName (arrayCreatorRest | parExpressions)?; // may omit constructor arguments
createdName: primitive | Identifier; // no array type
arrayCreatorRest: LBRACK (RBRACK (LBRACK RBRACK)* arrayInitializer | expression RBRACK (LBRACK expression RBRACK)* (LBRACK RBRACK)*);
arrayInitializer: LBRACE (variableInitializer (COMMA variableInitializer)* (COMMA)? )? RBRACE;
parExpressions: LPAREN expressions? RPAREN; // arguments
expressions: expression (COMMA expression)*;
parExpression: LPAREN expression RPAREN; // condition

// type
type: (primitive | Identifier) (LBRACK RBRACK)*;
primitive: INT | BOOL | STRING;

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
fragment StringCharacter: PrintableCharacter | ' ' | EscapeCharacter;
fragment PrintableCharacter: [!-~];
fragment EscapeCharacter: '\\' [n"\\];

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