package demo;

import asm.CodeGenerator;
import ast.SemanticAnalyzer;
import lexer.Lexer;
import parser.Parser;

import java.io.IOException;

public class AsmDemo {
    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer("tests/src/unDebut.adb");
        Parser parser = new Parser(lexer);
        parser.parse(true);
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(parser.getAst());
        semanticAnalyzer.setCodeGen(new CodeGenerator(parser.getAst()));
        semanticAnalyzer.analyze();
    }
}
