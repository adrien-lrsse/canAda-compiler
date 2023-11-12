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
        for (int i = 256; i <= 292; i++) {
            expectedTags.add(i);
        }

        for (int expectedTag : expectedTags) {
            token = lexer.scan();
            Assertions.assertNotNull(token, "Token is null");
            Assertions.assertEquals(expectedTag, token.getTag(), "Tag mismatch");
        }
    }

    @org.junit.jupiter.api.Test
    void testLexerRecognizesCorrectLexicalUnits() throws IOException {
        Lexer lexer = new Lexer("tests/src/unDebut.adb");
        Token token;

        String[] expectedLexicalUnits = {
                "Word{284, with}",
                "Word{289, ada}",
                "Char{290, .}",
                "Word{289, text_io}",
                "Char{290, ;}",
                "Word{282, use}",
                "Word{289, ada}",
                "Char{290, .}",
                "Word{289, text_io}",
                "Char{290, ;}",
                "Word{274, procedure}",
                "Word{289, undebut}",
                "Word{267, is}",
                "Word{264, function}",
                "Word{289, airerectangle}",
                "Char{290, (}",
                "Word{289, larg}",
                "Char{290, :}",
                "Word{289, integer}",
                "Char{290, ;}",
                "Word{289, long}",
                "Char{290, :}",
                "Word{289, integer}",
                "Char{290, )}",
                "Word{277, return}",
                "Word{289, integer}",
                "Word{267, is}",
                "Word{289, aire}",
                "Char{290, :}",
                "Word{289, integer}",
                "Char{290, ;}",
                "Word{258, begin}",
                "Word{289, aire}",
                "Word{287, :=}",
                "Word{289, larg}",
                "Char{290, *}",
                "Word{289, long}",
                "Char{290, ;}",
                "Word{277, return}",
                "Word{289, aire}",
                "Word{261, end}",
                "Word{289, airerectangle}",
                "Char{290, ;}",
                "Word{264, function}",
                "Word{289, perimetrerectangle}",
                "Char{290, (}",
                "Word{289, larg}",
                "Char{290, :}",
                "Word{289, integer}",
                "Char{290, ;}",
                "Word{289, long}",
                "Char{290, :}",
                "Word{289, integer}",
                "Char{290, )}",
                "Word{277, return}",
                "Word{289, integer}",
                "Word{267, is}",
                "Word{289, p}",
                "Char{290, :}",
                "Word{289, integer}",
                "Word{258, begin}",
                "Word{289, p}",
                "Word{287, :=}",
                "Word{289, larg}",
                "Char{290, *}",
                "Num{291, 2}",
                "Char{290, +}",
                "Word{289, long}",
                "Char{290, *}",
                "Num{291, 2}",
                "Char{290, ;}",
                "Word{277, return}",
                "Word{289, p}",
                "Word{261, end}",
                "Word{289, perimetrerectangle}",
                "Char{290, ;}",
                "Word{289, choix}",
                "Char{290, :}",
                "Word{289, integer}",
                "Char{290, ;}",
                "Word{258, begin}",
                "Word{289, choix}",
                "Word{287, :=}",
                "Num{291, 2}",
                "Char{290, ;}",
                "Word{265, if}",
                "Word{289, choix}",
                "Char{290, =}",
                "Num{291, 1}",
                "Word{279, then}",
                "Word{289, valeur}",
                "Word{287, :=}",
                "Word{289, perimetrerectangle}",
                "Char{290, (}",
                "Num{291, 2}",
                "Char{290, ,}",
                "Num{291, 3}",
                "Char{290, )}",
                "Char{290, ;}",
                "Word{289, put}",
                "Char{290, (}",
                "Word{289, valeur}",
                "Char{290, )}",
                "Char{290, ;}",
                "Word{259, else}",
                "Word{289, valeur}",
                "Word{287, :=}",
                "Word{289, airerectangale}",
                "Char{290, (}",
                "Num{291, 2}",
                "Char{290, ,}",
                "Num{291, 3}",
                "Char{290, )}",
                "Char{290, ;}",
                "Word{289, put}",
                "Char{290, (}",
                "Word{289, valeur}",
                "Char{290, )}",
                "Char{290, ;}",
                "Word{261, end}",
                "Word{265, if}",
                "Char{290, ;}",
                "Word{261, end}",
                "Word{289, undebut}",
                "Char{290, ;}",
                "<292>"
        };

        for (String expectedLexicalUnit : expectedLexicalUnits) {
            token = lexer.scan();
            Assertions.assertNotNull(token, "Token is null");
            Assertions.assertEquals(expectedLexicalUnit, token.toString(), "Lexical unit mismatch");
        }
    }

    @org.junit.jupiter.api.Test
    void testLexerThrowsInvalidCharacterException() throws IOException {
        try {
            Lexer lexer = new Lexer("tests/src/invalidCharacter.adb");
            for (int i = 0; i < 100; i++) {
                lexer.scan();
            }
            Assertions.fail("Lexer should have thrown an InvalidCharacterException");
        } catch (InvalidCharacterException e) {
            Assertions.assertEquals("lexer.InvalidCharacterException: Invalid character: 字 at line 5", e.toString(), "InvalidCharacterException mismatch");
        }
    }
}
