package lexer;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

    @org.junit.jupiter.api.Test
    void scan() throws IOException {
        Lexer lexer = new Lexer("tests/unDebut.txt");
        Token token;
        do {
            token = lexer.scan();
            System.out.println(token);
        } while (token.getTag() != Tag.EOF);
    }
}
