import lexer.Lexer;
import lexer.Token;

import java.io.IOException;

public class LexerTest {
    public LexerTest() throws IOException {
        Lexer lex = new Lexer("tests.txt");
        Token test = lex.scan();
    }
}
