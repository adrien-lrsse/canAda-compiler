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
    void testLexerRecognizesCorrectTags() throws IOException {
        Lexer lexer = new Lexer("tests/src/tag.adb");
        Token token;

        List<Integer> tagList = new ArrayList<>();
        for (int i = 256; i <= 292; i++) {
            tagList.add(i);
        }

        for (int expectedTag : tagList) {
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
