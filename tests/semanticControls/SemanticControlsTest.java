package semanticControls;

import ast.SemanticAnalyzer;
import lexer.Lexer;
import parser.Parser;
import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SemanticControlsTest {

        @org.junit.jupiter.api.Test
        void testCorrectEndKeyword() throws IOException {
                try {
                        Lexer lexer1 = new Lexer("tests/src/semanticControls/correct/endKeyword1.adb");
                        Parser parser1 = new Parser(lexer1);
                        parser1.parse(false);
                        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(parser1.getAst());
                        semanticAnalyzer.analyze();

                        Lexer lexer2 = new Lexer("tests/src/semanticControls/correct/endKeyword2.adb");
                        Parser parser2 = new Parser(lexer2);
                        parser2.parse(false);
                        SemanticAnalyzer semanticAnalyzer2 = new SemanticAnalyzer(parser2.getAst());
                        semanticAnalyzer2.analyze();

                        assert true;
                }
                catch (Exception e) {
                        throw new RuntimeException();
                }
        }

        @org.junit.jupiter.api.Test
        void testIncorrectEndKeyword() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/endKeyword.adb");
                        Parser parser = new Parser(lexer);
                        parser.parse(false);
                        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(parser.getAst());
                        semanticAnalyzer.analyze();
                        throw new RuntimeException();
                }
                catch (Exception e) {
                        assert true;
                }
        }

        @org.junit.jupiter.api.Test
        void testCorrectInOutParam() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/correct/inOutParam1.adb");
                        Parser parser = new Parser(lexer);
                        parser.parse(false);
                        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(parser.getAst());
                        semanticAnalyzer.analyze();

                        Lexer lexer2 = new Lexer("tests/src/semanticControls/correct/inOutParam2.adb");
                        Parser parser2 = new Parser(lexer2);
                        parser2.parse(false);
                        SemanticAnalyzer semanticAnalyzer2 = new SemanticAnalyzer(parser2.getAst());
                        semanticAnalyzer2.analyze();

                        assert true;
                }
                catch (Exception e) {
                        throw new RuntimeException();
                }
        }

        @org.junit.jupiter.api.Test
        void testIncorrectInOutParam() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/inOutParam.adb");
                        Parser parser = new Parser(lexer);
                        parser.parse(false);
                        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(parser.getAst());
                        semanticAnalyzer.analyze();
                        throw new RuntimeException();
                }
                catch (Exception e) {
                        assert true;
                }
        }
}
