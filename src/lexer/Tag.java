package lexer;

public class Tag {
    public final static int
        // keywords
        ACCESS = 256,
        AND = 257,
        BEGIN = 258,
        ELSE = 259,
        ELSIF = 260,
        END = 261,
        FALSE = 262,
        FOR = 263,
        FUNCTION = 264,
        IF = 265,
        IN = 266,
        IS = 267,
        LOOP = 268,
        NEW = 269,
        NOT = 270,
        NULL = 271,
        OR = 272,
        OUT = 273,
        PROCEDURE = 274,
        RECORD = 275,
        REM = 276,
        RETURN = 277,
        REVERSE = 278,
        THEN = 279,
        TRUE = 280,
        TYPE = 281,
        USE = 282,
        WHILE = 283,
        WITH = 284,
        // operators
        GEQ= 285,
        LEQ = 286,
        ASSIGNMENT = 287,
        DIFFERENT = 288,
        // identifiers
        ID = 289,
        // constants
        CHARCONST = 290,
        NUMCONST = 291,
        // End of file
        CHARACTERVAL = 292,
        // character'val
        PUT = 293,
        // put
        DOUBLEPOINT = 294,
        // ..
        EOF = 295,
        // temp
        INVALID = 296;

}
