package demo;

import ast.SemanticAnalyzer;
import lexer.Lexer;
import parser.Parser;

import java.io.IOException;

public class SemanticAnalyzerDemo {
    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer("tests/src/semanticControls/ReturnNeeded.adb");
        Parser parser = new Parser(lexer);
        parser.parse(false);
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(parser.getAst());
        semanticAnalyzer.analyze();
        // semanticAnalyzer.getTds().display();
    }
}
