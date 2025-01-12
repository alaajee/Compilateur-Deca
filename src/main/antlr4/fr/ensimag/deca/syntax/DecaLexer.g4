lexer grammar DecaLexer;

options {
   language=Java;
   superClass = AbstractDecaLexer;
}

@members {
    // Membres personnalisés si nécessaire
}

// Mots réservés
ASM : 'asm' ;
CLASS : 'class' ;
EXTENDS : 'extends' ;
ELSE : 'else' ;
FALSE : 'false';
IF : 'if';
INSTANCEOF : 'instanceof';
NEW :'new';
NULL : 'null';
READINT : 'readint';
READFLOAT :'readFloat';
PRINT : 'print';
PRINTLN : 'println';
PRINTLNX: 'printlnx';
PRINTX : 'printx';
PROTECTED : 'protected';
PRIVATE : 'private';
RETURN : 'return';
THIS : 'this';
TRUE : 'true';
WHILE : 'while';

// Symboles spéciaux
AND: '&&';
OR: '||';
SLASH : '/';
GT : '>';
GEQ: '>=';
LT : '<';
LEQ: '<=';
PERCENT : '%';
TIMES : '*';
EXCLAM: '!';
NEQ: '!=';
OPARENT: '(';
CPARENT: ')';
OBRACE: '{';
CBRACE: '}';
SEMI : ';';
COMMA: ',';
DOT : '.';
EQEQ: '==';
PLUS : '+';
MINUS : '-';
EQUALS : '=';

// Identificateurs
fragment LETTER : [a-zA-Z];
fragment DIGIT : [0-9];
IDENT : (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')* ;

// Littéraux entiers
fragment POSITIVE_DIGIT : [1-9];
INT : ('0' | POSITIVE_DIGIT DIGIT*);

// Littéraux flottants
fragment SIGN : [+-];
fragment NUM : DIGIT+ ;
fragment DEC : NUM '.' NUM ;
fragment EXP : ('E' | 'e') SIGN? NUM ;
FLOAT : DEC EXP? [Ff]?;

// Chaînes de caractères
fragment EOL : '\r'? '\n';
fragment STRING_CAR : ~['"\\\r\n];
STRING : '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING : '"' (STRING_CAR | EOL | '\\"' | '\\\\')* '"' ;

// Espaces et commentaires
WS : [ \t\r\n]+ -> skip ;
LINE_COMMENT : '//' ~[\r\n]* -> skip ;
BLOCK_COMMENT : '/*' .*? '*/' -> skip ;