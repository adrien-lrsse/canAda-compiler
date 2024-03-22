package demo;

import lexer.Lexer;
import lexer.Tag;
import lexer.Token;

import java.io.IOException;

public class LexerDemo {
    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer("tests/src/parser/correct/Issue1.adb");
        Token token;
        do {
            token = lexer.scan();
            System.out.println(token);
        } while (token.getTag() != Tag.EOF);
    }
}
