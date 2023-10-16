import lexer.Lexer;
import lexer.Token;

import java.io.IOException;

public class LexerTest {
    public LexerTest() throws IOException {
        Lexer lex = new Lexer();
        Token test = lex.scan();
    }
}
