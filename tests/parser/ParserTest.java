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

            Lexer lexer4 = new Lexer("tests/src/parser/correct/testBaseStruct4.adb");
            Parser parser4 = new Parser(lexer4);
            parser4.parse();

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

            Lexer lexer9 = new Lexer("tests/src/parser/correct/testDeclStruct9.adb");
            Parser parser9 = new Parser(lexer9);
            parser9.parse();

            Lexer lexer10 = new Lexer("tests/src/parser/correct/testDeclStruct10.adb");
            Parser parser10 = new Parser(lexer10);
            parser10.parse();

            Lexer lexer11 = new Lexer("tests/src/parser/correct/testDeclStruct11.adb");
            Parser parser11 = new Parser(lexer11);
            parser11.parse();

            Lexer lexer12 = new Lexer("tests/src/parser/correct/testDeclStruct12.adb");
            Parser parser12 = new Parser(lexer12);
            parser12.parse();

            Lexer lexer13 = new Lexer("tests/src/parser/correct/testDeclStruct13.adb");
            Parser parser13 = new Parser(lexer13);
            parser13.parse();

            Lexer lexer14 = new Lexer("tests/src/parser/correct/testDeclStruct14.adb");
            Parser parser14 = new Parser(lexer14);
            parser14.parse();

            Lexer lexer15 = new Lexer("tests/src/parser/correct/testDeclStruct15.adb");
            Parser parser15 = new Parser(lexer15);
            parser15.parse();

            Lexer lexer16 = new Lexer("tests/src/parser/correct/testDeclStruct16.adb");
            Parser parser16 = new Parser(lexer16);
            parser16.parse();

            Lexer lexer17 = new Lexer("tests/src/parser/correct/testDeclStruct17.adb");
            Parser parser17 = new Parser(lexer17);
            parser17.parse();

            Lexer lexer18 = new Lexer("tests/src/parser/correct/testDeclStruct18.adb");
            Parser parser18 = new Parser(lexer18);
            parser18.parse();

            Lexer lexer19 = new Lexer("tests/src/parser/correct/testDeclStruct19.adb");
            Parser parser19 = new Parser(lexer19);
            parser19.parse();

            Lexer lexer20 = new Lexer("tests/src/parser/correct/testDeclStruct20.adb");
            Parser parser20 = new Parser(lexer20);
            parser20.parse();
        }
        catch (Exception e) {
            fail("Le parsing a généré une exception : " + e.getMessage());
        }
    }

    @Test
    public void testChampsStruct() {
        try {
            Lexer lexer1 = new Lexer("tests/src/parser/correct/testChampsStruct1.adb");
            Parser parser1 = new Parser(lexer1);
            parser1.parse();

            Lexer lexer2 = new Lexer("tests/src/parser/correct/testChampsStruct2.adb");
            Parser parser2 = new Parser(lexer2);
            parser2.parse();
        } catch (Exception e) {
            fail("Le parsing a généré une exception : " + e.getMessage());
        }
    }

    @Test
    public void testTypeStruct() {
        try {
            Lexer lexer1 = new Lexer("tests/src/parser/correct/testTypeStruct1.adb");
            Parser parser1 = new Parser(lexer1);
            parser1.parse();

            Lexer lexer2 = new Lexer("tests/src/parser/correct/testTypeStruct2.adb");
            Parser parser2 = new Parser(lexer2);
            parser2.parse();
        } catch (Exception e) {
            fail("Le parsing a généré une exception : " + e.getMessage());
        }
    }

    @Test
    public void testParamsStruct() {
        try {
            Lexer lexer1 = new Lexer("tests/src/parser/correct/testParamsStruct1.adb");
            Parser parser1 = new Parser(lexer1);
            parser1.parse();

            Lexer lexer2 = new Lexer("tests/src/parser/correct/testParamsStruct2.adb");
            Parser parser2 = new Parser(lexer2);
            parser2.parse();
        } catch (Exception e) {
            fail("Le parsing a généré une exception : " + e.getMessage());
        }
    }

    @Test
    public void testParamStruct() {
        try {
            Lexer lexer1 = new Lexer("tests/src/parser/correct/testParamStruct1.adb");
            Parser parser1 = new Parser(lexer1);
            parser1.parse();

            Lexer lexer2 = new Lexer("tests/src/parser/correct/testParamStruct2.adb");
            Parser parser2 = new Parser(lexer2);
            parser2.parse();

            Lexer lexer3 = new Lexer("tests/src/parser/correct/testParamStruct3.adb");
            Parser parser3 = new Parser(lexer3);
            parser3.parse();
        } catch (Exception e) {
            fail("Le parsing a généré une exception : " + e.getMessage());
        }
    }

    @Test
    public void testModeStruct() {
        try {
            Lexer lexer1 = new Lexer("tests/src/parser/correct/testModeStruct1.adb");
            Parser parser1 = new Parser(lexer1);
            parser1.parse();

            Lexer lexer2 = new Lexer("tests/src/parser/correct/testModeStruct2.adb");
            Parser parser2 = new Parser(lexer2);
            parser2.parse();
        } catch (Exception e) {
            fail("Le parsing a généré une exception : " + e.getMessage());
        }
    }

    @Test
    public void testExpr() {
        try {
            Lexer lexer1 = new Lexer("tests/src/parser/correct/testExprStruct1.adb");
            Parser parser1 = new Parser(lexer1);
            parser1.parse();

            Lexer lexer2 = new Lexer("tests/src/parser/correct/testExprStruct2.adb");
            Parser parser2 = new Parser(lexer2);
            parser2.parse();

            Lexer lexer3 = new Lexer("tests/src/parser/correct/testExprStruct3.adb");
            Parser parser3 = new Parser(lexer3);
            parser3.parse();

            Lexer lexer4 = new Lexer("tests/src/parser/correct/testExprStruct4.adb");
            Parser parser4 = new Parser(lexer4);
            parser4.parse();

            Lexer lexer5 = new Lexer("tests/src/parser/correct/testExprStruct5.adb");
            Parser parser5 = new Parser(lexer5);
            parser5.parse();

            Lexer lexer6 = new Lexer("tests/src/parser/correct/testExprStruct6.adb");
            Parser parser6 = new Parser(lexer6);
            parser6.parse();

            Lexer lexer7 = new Lexer("tests/src/parser/correct/testExprStruct7.adb");
            Parser parser7 = new Parser(lexer7);
            parser7.parse();

            Lexer lexer8 = new Lexer("tests/src/parser/correct/testExprStruct8.adb");
            Parser parser8 = new Parser(lexer8);
            parser8.parse();

            Lexer lexer9 = new Lexer("tests/src/parser/correct/testExprStruct9.adb");
            Parser parser9 = new Parser(lexer9);
            parser9.parse();

            Lexer lexer10 = new Lexer("tests/src/parser/correct/testExprStruct10.adb");
            Parser parser10 = new Parser(lexer10);
            parser10.parse();

            Lexer lexer11 = new Lexer("tests/src/parser/correct/testExprStruct11.adb");
            Parser parser11 = new Parser(lexer11);
            parser11.parse();

            Lexer lexer12 = new Lexer("tests/src/parser/correct/testExprStruct12.adb");
            Parser parser12 = new Parser(lexer12);
            parser12.parse();

            Lexer lexer13 = new Lexer("tests/src/parser/correct/testExprStruct13.adb");
            Parser parser13 = new Parser(lexer13);
            parser13.parse();

            Lexer lexer14 = new Lexer("tests/src/parser/correct/testExprStruct14.adb");
            Parser parser14 = new Parser(lexer14);
            parser14.parse();

            Lexer lexer15 = new Lexer("tests/src/parser/correct/testExprStruct15.adb");
            Parser parser15 = new Parser(lexer15);
            parser15.parse();
        } catch (Exception e) {
            fail("Le parsing a généré une exception : " + e.getMessage());
        }
    }

    @Test
    public void testInstrStruct() {
        try {
            Lexer lexer1 = new Lexer("tests/src/parser/correct/testInstrStruct1.adb");
            Parser parser1 = new Parser(lexer1);
            parser1.parse();

            Lexer lexer2 = new Lexer("tests/src/parser/correct/testInstrStruct2.adb");
            Parser parser2 = new Parser(lexer2);
            parser2.parse();

            Lexer lexer3 = new Lexer("tests/src/parser/correct/testInstrStruct3.adb");
            Parser parser3 = new Parser(lexer3);
            parser3.parse();

            Lexer lexer4 = new Lexer("tests/src/parser/correct/testInstrStruct4.adb");
            Parser parser4 = new Parser(lexer4);
            parser4.parse();

            Lexer lexer5 = new Lexer("tests/src/parser/correct/testInstrStruct5.adb");
            Parser parser5 = new Parser(lexer5);
            parser5.parse();

            Lexer lexer6 = new Lexer("tests/src/parser/correct/testInstrStruct6.adb");
            Parser parser6 = new Parser(lexer6);
            parser6.parse();

            Lexer lexer7 = new Lexer("tests/src/parser/correct/testInstrStruct7.adb");
            Parser parser7 = new Parser(lexer7);
            parser7.parse();

            Lexer lexer8 = new Lexer("tests/src/parser/correct/testInstrStruct8.adb");
            Parser parser8 = new Parser(lexer8);
            parser8.parse();

            Lexer lexer9 = new Lexer("tests/src/parser/correct/testInstrStruct9.adb");
            Parser parser9 = new Parser(lexer9);
            parser9.parse();

            Lexer lexer10 = new Lexer("tests/src/parser/correct/testInstrStruct10.adb");
            Parser parser10 = new Parser(lexer10);
            parser10.parse();

            Lexer lexer11 = new Lexer("tests/src/parser/correct/testInstrStruct11.adb");
            Parser parser11 = new Parser(lexer11);
            parser11.parse();

            Lexer lexer12 = new Lexer("tests/src/parser/correct/testInstrStruct12.adb");
            Parser parser12 = new Parser(lexer12);
            parser12.parse();

            Lexer lexer13 = new Lexer("tests/src/parser/correct/testInstrStruct13.adb");
            Parser parser13 = new Parser(lexer13);
            parser13.parse();

            Lexer lexer14 = new Lexer("tests/src/parser/correct/testInstrStruct14.adb");
            Parser parser14 = new Parser(lexer14);
            parser14.parse();

            Lexer lexer15 = new Lexer("tests/src/parser/correct/testInstrStruct15.adb");
            Parser parser15 = new Parser(lexer15);
            parser15.parse();

            Lexer lexer16 = new Lexer("tests/src/parser/correct/testInstrStruct16.adb");
            Parser parser16 = new Parser(lexer16);
            parser16.parse();

            Lexer lexer17 = new Lexer("tests/src/parser/correct/testInstrStruct17.adb");
            Parser parser17 = new Parser(lexer17);
            parser17.parse();

            Lexer lexer18 = new Lexer("tests/src/parser/correct/testInstrStruct18.adb");
            Parser parser18 = new Parser(lexer18);
            parser18.parse();

            Lexer lexer19 = new Lexer("tests/src/parser/correct/testInstrStruct19.adb");
            Parser parser19 = new Parser(lexer19);
            parser19.parse();

            Lexer lexer20 = new Lexer("tests/src/parser/correct/testInstrStruct20.adb");
            Parser parser20 = new Parser(lexer20);
            parser20.parse();
        } catch (Exception e) {
            fail("Le parsing a généré une exception : " + e.getMessage());
        }
    }

    @Test
    public void testOperateurStruct() {
        try {
            Lexer lexer1 = new Lexer("tests/src/parser/correct/testOperateurStruct1.adb");
            Parser parser1 = new Parser(lexer1);
            parser1.parse();

            Lexer lexer2 = new Lexer("tests/src/parser/correct/testOperateurStruct2.adb");
            Parser parser2 = new Parser(lexer2);
            parser2.parse();

            Lexer lexer3 = new Lexer("tests/src/parser/correct/testOperateurStruct3.adb");
            Parser parser3 = new Parser(lexer3);
            parser3.parse();

            Lexer lexer4 = new Lexer("tests/src/parser/correct/testOperateurStruct4.adb");
            Parser parser4 = new Parser(lexer4);
            parser4.parse();

            Lexer lexer5 = new Lexer("tests/src/parser/correct/testOperateurStruct5.adb");
            Parser parser5 = new Parser(lexer5);
            parser5.parse();

            Lexer lexer6 = new Lexer("tests/src/parser/correct/testOperateurStruct6.adb");
            Parser parser6 = new Parser(lexer6);
            parser6.parse();

            Lexer lexer7 = new Lexer("tests/src/parser/correct/testOperateurStruct7.adb");
            Parser parser7 = new Parser(lexer7);
            parser7.parse();

            Lexer lexer8 = new Lexer("tests/src/parser/correct/testOperateurStruct8.adb");
            Parser parser8 = new Parser(lexer8);
            parser8.parse();

            Lexer lexer9 = new Lexer("tests/src/parser/correct/testOperateurStruct9.adb");
            Parser parser9 = new Parser(lexer9);
            parser9.parse();

            Lexer lexer10 = new Lexer("tests/src/parser/correct/testOperateurStruct10.adb");
            Parser parser10 = new Parser(lexer10);
            parser10.parse();

            Lexer lexer11 = new Lexer("tests/src/parser/correct/testOperateurStruct11.adb");
            Parser parser11 = new Parser(lexer11);
            parser11.parse();

            Lexer lexer12 = new Lexer("tests/src/parser/correct/testOperateurStruct12.adb");
            Parser parser12 = new Parser(lexer12);
            parser12.parse();

            Lexer lexer13 = new Lexer("tests/src/parser/correct/testOperateurStruct13.adb");
            Parser parser13 = new Parser(lexer13);
            parser13.parse();

            Lexer lexer14 = new Lexer("tests/src/parser/correct/testOperateurStruct14.adb");
            Parser parser14 = new Parser(lexer14);
            parser14.parse();

            Lexer lexer15 = new Lexer("tests/src/parser/correct/testOperateurStruct15.adb");
            Parser parser15 = new Parser(lexer15);
            parser15.parse();
        } catch (Exception e) {
            fail("Le parsing a généré une exception : " + e.getMessage());
        }
    }

    @Test
    public void testAccesStruct() {
        try {
            Lexer lexer1 = new Lexer("tests/src/parser/correct/testAccesStruct1.adb");
            Parser parser1 = new Parser(lexer1);
            parser1.parse();

            Lexer lexer2 = new Lexer("tests/src/parser/correct/testAccesStruct2.adb");
            Parser parser2 = new Parser(lexer2);
            parser2.parse();

            Lexer lexer3 = new Lexer("tests/src/parser/correct/testAccesStruct3.adb");
            Parser parser3 = new Parser(lexer3);
            parser3.parse();
        } catch (Exception e) {
            fail("Le parsing a généré une exception : " + e.getMessage());
        }
    }
}
