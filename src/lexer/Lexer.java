package lexer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

public class Lexer {
    private int line = 1;
    private char currentChar = ' ';
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
        // operators
        reserve(new Word('>', ">"));
        reserve(new Word('<', "<"));
        reserve(new Word(Tag.GEQ, ">="));
        reserve(new Word(Tag.LEQ, "<="));
        reserve(new Word(Tag.NOT, "not"));
        reserve(new Word('=', "="));
        reserve(new Word('+', "+"));
        reserve(new Word('-', "-"));
        reserve(new Word('*', "*"));
        reserve(new Word('/', "/"));
        reserve(new Word(Tag.REM, "rem"));
        reserve(new Word(Tag.AND, "and"));
        reserve(new Word(Tag.OR, "or"));
        reserve(new Word(Tag.ASSIGNMENT, ":="));
        // punctuation
        reserve(new Word('(', "("));
        reserve(new Word(')', ")"));
        reserve(new Word('\'', "'"));
        reserve(new Word(';', ";"));
        reserve(new Word(':', ":"));
        reserve(new Word('?', "?"));
        reserve(new Word(',', ","));
        reserve(new Word('.', "."));


        this.fileReader = new FileReader(fileName);
    }
    public Token scan() throws IOException, InvalidCharacterException {
        // handle whitespaces
        while ((character = fileReader.read()) != -1) {
            currentChar = (char) character;
            if (currentChar == '\n') {
                line++;
            }
            if ((currentChar != ' ') && (currentChar != '\n') && (currentChar != '\t')) {
                System.out.print(currentChar);
                break;
            }
        }
        // handle simple and double character tokens
        // switch case
        switch (currentChar) {
            case '=' : {
                return words.get("=");
            }
            case '/' : {
                char nextChar = (char) fileReader.read();
                if (nextChar == '=') return words.get("not");
                else if (nextChar == ' ') return words.get("/");
                else throw new InvalidCharacterException(nextChar, line);
            }
            case '<' : {
                char nextChar = (char) fileReader.read();
                if (nextChar == '=') return words.get("<=");
                else if (nextChar == ' ') return words.get("<");
                else throw new InvalidCharacterException(nextChar, line);
            }
            case '>' : {
                char nextChar = (char) fileReader.read();
                if (nextChar == '=') return words.get(">=");
                else if (nextChar == ' ') return words.get(">");
                else throw new InvalidCharacterException(nextChar, line);
            }
            case '(' : {
                return words.get("(");
            }
            case ')' : {
                return words.get(")");
            }
            case '\'' : {
                return words.get("'");
            }
            case ';' : {
                return words.get(";");
            }
            case ':' : {
                char nextChar = (char) fileReader.read();
                if (nextChar == '=') return words.get(":=");
                else if (nextChar == ' ') return words.get(":");
                else throw new InvalidCharacterException(nextChar, line);
            }
            case '?' : {
                return words.get("?");
            }
            case ',' : {
                return words.get(",");
            }
            case '.' : {
                return words.get(".");
            }
            case '+' : {
                return words.get("+");
            }
            case '*' : {
                return words.get("*");
            }
            case '-' : {
                char nextChar = (char) fileReader.read();
                if (nextChar != '-') return words.get("-");
                else {
                    do {
                        character = fileReader.read();
                        currentChar = (char) character;
                        System.out.println(currentChar);
                    } while (currentChar != '\n');
                    return scan();
                }
            }
        }
        // handle numbers
        if( Character.isDigit(currentChar)) {
            int v = 0;
            do {
                v = 10*v + Character.digit(currentChar, 10);
                character = fileReader.read();
                currentChar = (char) character;
            } while(Character.isDigit(currentChar));
            return new Num(v);
        }
        // handle reserved words
        if(Character.isLetter(currentChar)) {
            StringBuilder reading_word = new StringBuilder();

            do {  // identifiers are only made of letters / digit / _
                reading_word.append(currentChar);
                character = fileReader.read();
                currentChar = (char) character;
            } while(Character.isLetterOrDigit(currentChar) || currentChar == '_');

            String s = reading_word.toString().toLowerCase();  // case-insensitive language

            if (words.containsKey(s)) { // checking if the identifier is a reserved word
                return words.get(s);
            }
            else {
                Word w = new Word(Tag.ID, s);
                words.put(s, w);
                return w;
                }
            }
        // handle invalid characters
        if (words.get(Character.toString(currentChar)) == null) {
            throw new InvalidCharacterException(currentChar, line);
        }
        currentChar = ' ';
        return new Token(currentChar);
    }
}
