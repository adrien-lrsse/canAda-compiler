package lexer;

import org.junit.jupiter.api.Assertions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @org.junit.jupiter.api.Test
    void testLexerIgnoresCommentsAndWhitespace() throws IOException{
        Lexer lexer = new Lexer("tests/src/commentsAndWhitespace.adb");
        Token token;

        String[] expectedTokens = {"x", ":=", "42", ";", "y", ":=", "7", ";", ""};

        for (String expectedToken : expectedTokens) {
            token = lexer.scan();
            Assertions.assertNotNull(token, "Token is null");
            Assertions.assertEquals(expectedToken, token.getStringValue(), "Token mismatch");
        }
    }

    @org.junit.jupiter.api.Test
    void testLexerRecognizesCorrectTags() throws IOException {
        Lexer lexer = new Lexer("tests/src/tag.adb");
        Token token;

        List<Integer> expectedTags = new ArrayList<>();
        for (int i = 256; i <= 291; i++) {
            expectedTags.add(i);
        }

        for (int expectedTag : expectedTags) {
            token = lexer.scan();
            Assertions.assertNotNull(token, "Token is null");
            Assertions.assertEquals(expectedTag, token.getTag(), "Tag mismatch");
        }
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
