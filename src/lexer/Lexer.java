package lexer;

import java.io.IOException;
import java.util.Hashtable;

public class Lexer {
    public int line = 1;
    private char peek = ' ';
    private final Hashtable<String, Word> words = new Hashtable<>();
    void reserve(Word t) {
        words.put(t.lexeme, t);
    }
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
        // skip white space
        for ( ; ; peek = (char)System.in.read()) {
            if(peek == ' ' || peek == '\t') continue;
            else if (peek == '\n') line++;
            else break;
        }
        // handle numbers
        if( Character.isDigit(peek)) {
            int v = 0;
            do {
                v = 10*v + Character.digit(peek, 10);
                peek = (char)System.in.read();
            } while(Character.isDigit(peek));
            return new Num(v);
        }
        // handle reserved words
        if(Character.isLetter(peek)) {
            StringBuilder b = new StringBuilder();
            do {
                b.append(peek);
                peek = (char)System.in.read();
            } while(Character.isLetterOrDigit(peek) || peek == '_');
            String s = b.toString();
            Word w = words.get(s);
            if (w != null) return w;
            w = new Word(Tag.ID, s);
            words.put(s, w);
            return w;
        }
        Token t = new Token(peek);
        peek = ' ';
        return t;
    }
}
