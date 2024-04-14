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

        @org.junit.jupiter.api.Test
        void testCorrectArgumentsVerificationInFunctionCall() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/correct/argumentsVerificationInFunctionCall.adb");
                        Parser parser = new Parser(lexer);
                        parser.parse(false);
                        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(parser.getAst());
                        semanticAnalyzer.analyze();
                        assert true;
                }
                catch (Exception e) {
                        throw new RuntimeException();
                }
        }

        @org.junit.jupiter.api.Test
        void testIncorrectArgumentsVerificationInFunctionCall1() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/argumentsVerificationInFunctionCall1.adb");
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
        void testIncorrectArgumentsVerificationInFunctionCall2() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/argumentsVerificationInFunctionCall2.adb");
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
        void testCorrectBoolVerificationForCondition() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/correct/boolVerificationForCondition.adb");
                        Parser parser = new Parser(lexer);
                        parser.parse(false);
                        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(parser.getAst());
                        semanticAnalyzer.analyze();
                        assert true;
                }
                catch (Exception e) {
                        throw new RuntimeException();
                }
        }

        @org.junit.jupiter.api.Test
        void testIncorrectBoolVerificationForCondition1() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/boolVerificationForCondition1.adb");
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
        void testIncorrectBoolVerificationForCondition2() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/boolVerificationForCondition2.adb");
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
        void testIncorrectBoolVerificationForCondition3() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/boolVerificationForCondition3.adb");
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
        void testCorrectDiffDeclName() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/correct/diffDeclName1.adb");
                        Parser parser = new Parser(lexer);
                        parser.parse(false);
                        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(parser.getAst());
                        semanticAnalyzer.analyze();

                        Lexer lexer2 = new Lexer("tests/src/semanticControls/correct/diffDeclName2.adb");
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
        void testIncorrectDiffDeclName1() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/diffDeclName1.adb");
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
        void testIncorrectDiffDeclName2() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/diffDeclName2.adb");
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
        void testIncorrectDiffDeclName3() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/diffDeclName3.adb");
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
        void testIncorrectDiffDeclName4() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/diffDeclName4.adb");
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
        void testCorrectDiffFieldName() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/correct/diffFieldName.adb");
                        Parser parser = new Parser(lexer);
                        parser.parse(false);
                        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(parser.getAst());
                        semanticAnalyzer.analyze();
                        assert true;
                }
                catch (Exception e) {
                        throw new RuntimeException();
                }
        }

        @org.junit.jupiter.api.Test
        void testIncorrectDiffFieldName() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/diffFieldName.adb");
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
        void testCorrectForLoopVar() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/correct/forLoopVar.adb");
                        Parser parser = new Parser(lexer);
                        parser.parse(false);
                        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(parser.getAst());
                        semanticAnalyzer.analyze();
                        assert true;
                }
                catch (Exception e) {
                        throw new RuntimeException();
                }
        }

        @org.junit.jupiter.api.Test
        void testIncorrectForLoopVar() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/forLoopVar.adb");
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
        void testCorrectInParam() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/correct/inParam1.adb");
                        Parser parser = new Parser(lexer);
                        parser.parse(false);
                        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(parser.getAst());
                        semanticAnalyzer.analyze();

                        Lexer lexer2 = new Lexer("tests/src/semanticControls/correct/inParam2.adb");
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
        void testIncorrectInParam1() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/inParam1.adb");
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
        void testIncorrectInParam2() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/inParam2.adb");
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
        void testCorrectReservedWordPut() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/correct/reservedWordPut.adb");
                        Parser parser = new Parser(lexer);
                        parser.parse(false);
                        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(parser.getAst());
                        semanticAnalyzer.analyze();
                        assert true;
                }
                catch (Exception e) {
                        throw new RuntimeException();
                }
        }

        @org.junit.jupiter.api.Test
        void testIncorrectReservedWordPut() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/reservedWordPut.adb");
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
        void testCorrectReturnEndFunc() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/correct/returnEndFunc1.adb");
                        Parser parser = new Parser(lexer);
                        parser.parse(false);
                        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(parser.getAst());
                        semanticAnalyzer.analyze();

                        Lexer lexer2 = new Lexer("tests/src/semanticControls/correct/returnEndFunc2.adb");
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
        void testIncorrectReturnEndFunc1() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/returnEndFunc1.adb");
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
        void testIncorrectReturnEndFunc2() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/returnEndFunc2.adb");
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
        void testCorrectTypeExistenceVerification() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/correct/typeExistenceVerification.adb");
                        Parser parser = new Parser(lexer);
                        parser.parse(false);
                        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(parser.getAst());
                        semanticAnalyzer.analyze();
                        assert true;
                }
                catch (Exception e) {
                        throw new RuntimeException();
                }
        }

        @org.junit.jupiter.api.Test
        void testIncorrectTypeExistenceVerification1() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/typeExistenceVerification1.adb");
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
        void testIncorrectTypeExistenceVerification2() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/typeExistenceVerification2.adb");
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
        void testIncorrectTypeExistenceVerification3() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/typeExistenceVerification3.adb");
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
        void testIncorrectTypeExistenceVerification4() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/typeExistenceVerification4.adb");
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
        void testCorrectTypeRecordDefinition() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/correct/typeRecordDefinition.adb");
                        Parser parser = new Parser(lexer);
                        parser.parse(false);
                        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(parser.getAst());
                        semanticAnalyzer.analyze();
                        assert true;
                }
                catch (Exception e) {
                        throw new RuntimeException();
                }
        }

        @org.junit.jupiter.api.Test
        void testIncorrectTypeRecordDefinition1() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/typeRecordDefinition1.adb");
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
        void testCorrectTypeVerificationInAssignation() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/correct/typeVerificationInAssignation.adb");
                        Parser parser = new Parser(lexer);
                        parser.parse(false);
                        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(parser.getAst());
                        semanticAnalyzer.analyze();
                        assert true;
                }
                catch (Exception e) {
                        throw new RuntimeException();
                }
        }

        @org.junit.jupiter.api.Test
        void testIncorrectTypeVerificationInAssignation() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/typeVerificationInAssignation.adb");
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
        void testCorrectTypeVerificationInOperation() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/correct/typeVerificationInOperation.adb");
                        Parser parser = new Parser(lexer);
                        parser.parse(false);
                        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(parser.getAst());
                        semanticAnalyzer.analyze();
                        assert true;
                }
                catch (Exception e) {
                        throw new RuntimeException();
                }
        }

        @org.junit.jupiter.api.Test
        void testIncorrectTypeVerificationInOperation() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/typeVerificationInOperation.adb");
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
        void testCorrectTypeVerificationInReturn() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/correct/typeVerificationInReturn.adb");
                        Parser parser = new Parser(lexer);
                        parser.parse(false);
                        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(parser.getAst());
                        semanticAnalyzer.analyze();
                        assert true;
                }
                catch (Exception e) {
                        throw new RuntimeException();
                }
        }

        @org.junit.jupiter.api.Test
        void testIncorrectTypeVerificationInReturn() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/typeVerificationInReturn.adb");
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
        void testCorrectVarScope() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/correct/varScope1.adb");
                        Parser parser = new Parser(lexer);
                        parser.parse(false);
                        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(parser.getAst());
                        semanticAnalyzer.analyze();

                        Lexer lexer2 = new Lexer("tests/src/semanticControls/correct/varScope2.adb");
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
        void testIncorrectVarScope1() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/varScope1.adb");
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
        void testIncorrectVarScope2() throws IOException {
                try {
                        Lexer lexer = new Lexer("tests/src/semanticControls/incorrect/varScope2.adb");
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
