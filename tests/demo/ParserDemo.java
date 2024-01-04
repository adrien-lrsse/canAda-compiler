package demo;

import lexer.Lexer;
import parser.Parser;

import java.io.IOException;

public class ParserDemo {
    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer("tests/src/parser/correct/testDeclStruct2.adb");
        Parser parser = new Parser(lexer);
        parser.parse();
    }
}
