package parser;

import lexer.Lexer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ParserTest {

    @Test
    public void testBaseStruct() {
        try {
            Lexer lexer1 = new Lexer("tests/src/parser/correct/testBaseStruct1.adb");
            Parser parser1 = new Parser(lexer1);
            parser1.parse();

            Lexer lexer2 = new Lexer("tests/src/parser/correct/testBaseStruct2.adb");
            Parser parser2 = new Parser(lexer2);
            parser2.parse();

            Lexer lexer3 = new Lexer("tests/src/parser/correct/testBaseStruct3.adb");
            Parser parser3 = new Parser(lexer3);
            parser3.parse();

            // Si le parsing se déroule sans exception, le test réussit
            assertTrue(true);
        } catch (Exception e) {
            // En cas d'exception, le test échoue
            fail("Le parsing a généré une exception : " + e.getMessage());
        }
    }



    @Test
    public void testDeclStruct() {
        try {
            Lexer lexer1 = new Lexer("tests/src/parser/correct/testDeclStruct1.adb");
            Parser parser1 = new Parser(lexer1);
            parser1.parse();

            Lexer lexer2 = new Lexer("tests/src/parser/correct/testDeclStruct2.adb");
            Parser parser2 = new Parser(lexer2);
            parser2.parse();

            Lexer lexer3 = new Lexer("tests/src/parser/correct/testDeclStruct3.adb");
            Parser parser3 = new Parser(lexer3);
            parser3.parse();

            Lexer lexer4 = new Lexer("tests/src/parser/correct/testDeclStruct4.adb");
            Parser parser4 = new Parser(lexer4);
            parser4.parse();

            Lexer lexer5 = new Lexer("tests/src/parser/correct/testDeclStruct5.adb");
            Parser parser5 = new Parser(lexer5);
            parser5.parse();

            Lexer lexer6 = new Lexer("tests/src/parser/correct/testDeclStruct6.adb");
            Parser parser6 = new Parser(lexer6);
            parser6.parse();

            Lexer lexer7 = new Lexer("tests/src/parser/correct/testDeclStruct7.adb");
            Parser parser7 = new Parser(lexer7);
            parser7.parse();

            Lexer lexer8 = new Lexer("tests/src/parser/correct/testDeclStruct8.adb");
            Parser parser8 = new Parser(lexer8);
            parser8.parse();
        }
        catch (Exception e) {
            fail("Le parsing a généré une exception : " + e.getMessage());
        }
    }
}
