package demo;

import asm.CodeGenerator;
import asm.visual.Launcher;
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
        CodeGenerator codeGenerator = new CodeGenerator(parser.getAst().getFilename());
        semanticAnalyzer.setCodeGen(codeGenerator);
        semanticAnalyzer.analyze();
//        Launcher.run(parser.getAst().getFilename()+ "-output.s");
    }
}
