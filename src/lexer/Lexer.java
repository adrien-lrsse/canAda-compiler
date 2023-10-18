package lexer;

import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

public class Lexer {
    public int line = 1;
    private char currentChar = ' ';
    private final Hashtable<String, Word> words = new Hashtable<>();
    void reserve(Word t) {
        words.put(t.lexeme, t);
    }

    FileReader fileReader = new FileReader(fileName);
    int character;
    int line = 0;
    public Lexer() {
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
    }
    public Token scan() throws IOException {
        boolean find_token = false
        while (((character = fileReader.read()) != -1) && (!find_token)) {
            char currentChar = (char) character;
            if (!((currentChar == ' ') || currentChar == '\n')) {
                System.out.print(currentChar);
                // handle numbers
                if( Character.isDigit(currentChar)) {
                    int v = 0;
                    do {
                        v = 10*v + Character.digit(currentChar, 10);
                        character = fileReader.read();
                        currentChar = (char) character;
                    } while(Character.isDigit(currentChar));
                    find_token = true;
                    return new Num(v);
                }
                // handle reserved words
                if(Character.isLetter(currentChar)) {
                    StringBuilder b = new StringBuilder();
                    do {
                        b.append(currentChar);
                        character = fileReader.read();
                        currentChar = (char) character;
                    } while(Character.isLetterOrDigit(currentChar) || currentChar == '_');
                    String s = b.toString();
                    Word w = words.get(s);
                    if (w != null) return w;
                    w = new Word(Tag.ID, s);
                    words.put(s, w);
                    find_token = true;
                    return w;
                }
            }
        }
        Token t = new Token(currentChar);
        currentChar = ' ';
        return t;

    }
}
