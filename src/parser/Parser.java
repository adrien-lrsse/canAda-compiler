package parser;

import lexer.Lexer;
import lexer.Tag;

import java.io.IOException;
import java.util.Stack;

public class Parser {
    Stack<Integer> stack = new Stack<Integer>();
    Lexer lexer;
    AnalyzeTable analyzeTable;
    public Parser(Lexer lexer) throws IOException {
        this.lexer = lexer;
        this.analyzeTable = new AnalyzeTable(this);
    }
    public void parse() throws IOException {
        analyzeTable.analyze();
    }
}
