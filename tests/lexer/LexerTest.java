package lexer;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

    @org.junit.jupiter.api.Test
    void testFileDoesNotExistException() {
        assertThrows(FileNotFoundException.class, () -> new Lexer("tests/src/doesNotExist.adb"));
    }

    @org.junit.jupiter.api.Test
    void testFileExists() {
        assertDoesNotThrow(() -> new Lexer("tests/src/doesExist.adb"));
    }

/*
    @org.junit.jupiter.api.Test
    void scan() throws IOException {
        Lexer lexer = new Lexer("tests/unDebut.adb");
        Token token;
        do {
            token = lexer.scan();
            System.out.println(token);
        } while (token.getTag() != Tag.EOF);
    }*/
}
