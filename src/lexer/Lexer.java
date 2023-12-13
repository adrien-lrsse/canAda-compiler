package lexer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

public class Lexer {
    private int line = 1;
    private char currentChar = ' ';
    private boolean invalidToken = false;
    private final Hashtable<String, Word> words = new Hashtable<>();
    void reserve(Word t) {
        words.put(t.lexeme, t);
    }
    FileReader fileReader;
    int character;
    public Lexer(String fileName) throws FileNotFoundException {
        // keywords
        reserve(new Word(Tag.ACCESS, "access"));
        reserve(new Word(Tag.BEGIN, "begin"));
        reserve(new Word(Tag.ELSE, "else"));
        reserve(new Word(Tag.ELSIF, "elsif"));
        reserve(new Word(Tag.END, "end"));
        reserve(new Word(Tag.FALSE, "false"));
        reserve(new Word(Tag.FOR, "for"));
        reserve(new Word(Tag.FUNCTION, "function"));
        reserve(new Word(Tag.IF, "if"));
        reserve(new Word(Tag.IN, "in"));
        reserve(new Word(Tag.IS, "is"));
        reserve(new Word(Tag.LOOP, "loop"));
        reserve(new Word(Tag.NEW, "new"));
        reserve(new Word(Tag.NULL, "null"));
        reserve(new Word(Tag.OUT, "out"));
        reserve(new Word(Tag.PROCEDURE, "procedure"));
        reserve(new Word(Tag.RECORD, "record"));
        reserve(new Word(Tag.RETURN, "return"));
        reserve(new Word(Tag.REVERSE, "reverse"));
        reserve(new Word(Tag.THEN, "then"));
        reserve(new Word(Tag.TRUE, "true"));
        reserve(new Word(Tag.TYPE, "type"));
        reserve(new Word(Tag.USE, "use"));
        reserve(new Word(Tag.WHILE, "while"));
        reserve(new Word(Tag.WITH, "with"));
        reserve(new Word(Tag.NOT, "not"));
        reserve(new Word(Tag.CHARACTERVAL, "character'val"));
        reserve(new Word(Tag.PUT, "put"));
        reserve(new Word(Tag.DOUBLEPOINT, ".."));
        // operators
        reserve(new Word(Tag.GEQ, ">="));
        reserve(new Word(Tag.LEQ, "<="));
        reserve(new Word(Tag.DIFFERENT, "/="));
        reserve(new Word(Tag.REM, "rem"));
        reserve(new Word(Tag.AND, "and"));
        reserve(new Word(Tag.OR, "or"));
        reserve(new Word(Tag.ASSIGNMENT, ":="));

        this.fileReader = new FileReader(fileName);
    }
    public Token scan() throws IOException {
        // handle whitespaces
        while (((currentChar == ' ') || (currentChar == '\n') || (currentChar == '\t')) && ((character = fileReader.read()) != -1)) {
            currentChar = (char) character;
            if (currentChar == '\n') {
                line++;
            }
            if ((currentChar != ' ') && (currentChar != '\n') && (currentChar != '\t')) {
                // System.out.print(currentChar);
                break;
            }
        }
        // handle end of file
        if (character == -1) {
            return new Token(Tag.EOF);
        }
        // handle simple and double character tokens
        // switch case
        boolean isCharacter = false;
        boolean moinsUnaireCase = false;
        Token t = null;
        switch (currentChar) {
            case '/' : {
                isCharacter = true;
                char nextChar = (char) fileReader.read();
                if (nextChar == '=') t = words.get("/=");
                else {
                    t = new Char(currentChar);
                    moinsUnaireCase = true;
                    currentChar = nextChar;
                }
                break;
            }
            case '<' : {
                isCharacter = true;
                char nextChar = (char) fileReader.read();
                if (nextChar == '=') t = words.get("<=");
                else {
                    t = new Char(currentChar);
                    moinsUnaireCase = true;
                    currentChar = nextChar;
                }
                break;
            }
            case '>' : {
                isCharacter = true;
                char nextChar = (char) fileReader.read();
                if (nextChar == '=') t = words.get(">=");
                else {
                    t = new Char(currentChar);
                    moinsUnaireCase = true;
                    currentChar = nextChar;
                }
                break;
            }
            case ':' : {
                isCharacter = true;
                char nextChar = (char) fileReader.read();
                if (nextChar == '=') t = words.get(":=");
                else {
                    t = new Char(currentChar);
                    moinsUnaireCase = true;
                    currentChar = nextChar;
                }
                break;
            }
            case '-' : {
                isCharacter = true;
                char nextChar = (char) fileReader.read();
                if (nextChar != '-') {
                    t = new Char(currentChar);
                    moinsUnaireCase = true;
                    currentChar = nextChar;
                }
                else {
                    do {
                        character = fileReader.read();
                        currentChar = (char) character;
                        // System.out.println(currentChar);
                    } while (currentChar != '\n');
                    line++;
                    return scan();
                }
                break;
            }
            case '.' : {
                isCharacter = true;
                char nextChar = (char) fileReader.read();
                if (nextChar == '.') { t = words.get(".."); }
                else {
                    t = new Char(currentChar);
                    moinsUnaireCase = true;
                    currentChar = nextChar;
                }
                break;
            }
            case '\'' : {
                isCharacter = true;
                char nextChar = (char) fileReader.read();
                if (AsciiPrintableCharacters.isLetter(nextChar)) {
                    t = new Char(nextChar);
                    character = fileReader.read();
                    currentChar = (char) character;
                    if (currentChar != '\'') {
                        System.out.println("Invalid character: " + currentChar + " at line " + line);
                        invalidToken = true;
                    }
                }
                else {
                    System.out.println("Invalid character: " + nextChar + " at line " + line);
                    invalidToken = true;
                }
            }
        }
        if (isCharacter) {
            if (!moinsUnaireCase) {
                currentChar = ' ';
            }
            if (invalidToken) {
                invalidToken = false;
                return new Invalid();
            }
            return t;
        }


        // handle numbers
        if( AsciiPrintableCharacters.isDigit(currentChar)) {
            int v = 0;
            int v_tmp;
            do {
                v_tmp = 10*v + Character.digit(currentChar, 10);
                if (v_tmp < v) {
                    System.out.println("Integer overflow at line " + line);
                }
                character = fileReader.read();
                currentChar = (char) character;
                v = v_tmp;
            } while(AsciiPrintableCharacters.isDigit(currentChar));
            if (character == '\n') {
                line++;
            }
            return new Num(v);
        }
        // handle reserved words
        if(AsciiPrintableCharacters.isLetter(currentChar)) {
            StringBuilder reading_word = new StringBuilder();

            do {  // identifiers are only made of letters / digit / _
                reading_word.append(currentChar);
                character = fileReader.read();
                currentChar = (char) character;
            } while(AsciiPrintableCharacters.isLetterOrDigit(currentChar) || currentChar == '_');
            if (character == '\n') {
                line++;
            }

            String s = reading_word.toString().toLowerCase();  // case-insensitive language

            // handle character'val
            if (s.equals("character")) {
                if (currentChar == '\'') {
                    reading_word.append(currentChar);
                    character = fileReader.read();
                    currentChar = (char) character;
                    if (currentChar == 'v') {
                        reading_word.append(currentChar);
                        character = fileReader.read();
                        currentChar = (char) character;
                        if (currentChar == 'a') {
                            reading_word.append(currentChar);
                            character = fileReader.read();
                            currentChar = (char) character;
                            if (currentChar == 'l') {
                                reading_word.append(currentChar);
                                currentChar = ' ';
                                s = reading_word.toString().toLowerCase();
                                return words.get(s);
                            }
                            else {
                                s = reading_word.toString().toLowerCase();
                                System.out.println("Invalid identifier or reserved word: " + s + " at line " + line);
                            }
                        }
                        else {
                            s = reading_word.toString().toLowerCase();
                            System.out.println("Invalid identifier or reserved word: " + s + " at line " + line);
                        }
                    }
                    else {
                        s = reading_word.toString().toLowerCase();
                        System.out.println("Invalid identifier or reserved word: " + s + " at line " + line);
                    }
                }
                else {
                    Word w2 = new Word(Tag.ID, s);
                    words.put(s, w2);
                    return w2;
                }
            }

            if (words.containsKey(s)) { // checking if the identifier is a reserved word
                return words.get(s);
            }
            else {
                Word w2 = new Word(Tag.ID, s);
                words.put(s, w2);
                return w2;
                }
            }
        // handle characters
        if (AsciiPrintableCharacters.isAsciiPrintable(currentChar)) {
            Token tmp = new Char(currentChar);
            currentChar = ' ';
            return tmp;
        }
        // handle invalid characters
        if (words.get(Character.toString(currentChar)) == null) {
            System.out.println("Invalid character: " + currentChar + " at line " + line);
            invalidToken = true;
        }
        currentChar = ' ';
        if (invalidToken) {
            invalidToken = false;
            return new Invalid();
        }
        return new Token(currentChar);
    }
}
