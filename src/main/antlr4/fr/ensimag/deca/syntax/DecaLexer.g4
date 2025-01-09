lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

// Deca lexer rules.
DUMMY_TOKEN: .; // A FAIRE : Règle bidon qui reconnait tous les caractères.
                // A FAIRE : Il faut la supprimer et la remplacer par les vraies règles.

               //Mots reserves

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
               RETURN : 'return ';
               THIS : 'this';
               TRUE : 'true';
               WHILE : 'while';


               //Identificateurs

               fragment LETTER : [a-z|A-Z];
               fragment DIGIT : [0-9];
               IDENT : (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')* ;

               //Symboles speciaux

               DIVIDE : '/';
               GREATER : '>';
               GREATEROREQUALS: '>=';
               LOWER : '<';
               LOWEROREQUALS: '<=';
               MODULO : '%';
               MULTIPLY : '*';
               NOT: '!';
               NOTEQUALS: '!=';
               AND : '&&';
               OR : '||';
               OPARENT : '(';
               CPARENT : ')';
               OBRACE: '{';
               CBRACE: '}';
               SEMI : ';';
               COMMA: ',';
               DOT : '.';
               EQUALS_EQUALS: '==';
               PLUS : '+';
               MINUS : '-';
               EQUALS : '=';

               //Littéraux entiers
               fragment POSITIVE_DIGIT : [1-9];
               INT : ('0' | POSITIVE_DIGIT DIGIT*);

               /*
               Une erreur de compilation est levée si un littéral entier n’est pas 
               codable comme un entier signé positif sur 32 bits
               */






               //Littéraux flottants

               fragment SIGN : [+-];    
               fragment DIGITHEX : [0-9a-fA-F];  
               NUM : DIGIT+ ;    
               EXP : ('E' | 'e') SIGN? NUM ;
               DEC : NUM '.' NUM ;
               FLOATDEC : DEC EXP? [Ff]?;
               NUMHEX : DIGITHEX+;
               FLOATHEX : ('0x' | '0X') NUMHEX '.' NUMHEX [Pp] SIGN? NUM [Ff]?;
               FLOAT : FLOATDEC | FLOATHEX;

               /* 
               Une erreur de compilation est levée si un littéral est trop grand et que l’arrondi se fait
vers l’infini, ou bien qu’un littéral non nul est trop petit et que l’arrondi se fait vers zéro.
*/

               //Chaînes de caractères

               fragment EOL : '\r'? '\n';
               fragment STRING_CAR : ~['"\\\r\n];
               STRING : '"' (STRING_CAR | '\\"' | '\\\\')* ;
               MULTI_LINE_STRING : '"' (STRING_CAR | EOL | '\\"' |'\\\\')* '"' ;












