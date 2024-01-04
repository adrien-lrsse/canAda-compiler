package parser;

import lexer.Lexer;

import java.io.IOException;
import java.util.Stack;

public class Parser {
    Stack<Integer> stack = new Stack<>();
    Lexer lexer;
    AnalyzeTable analyzeTable;
    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.analyzeTable = new AnalyzeTable(this);
    }
    public void parse() throws IOException {
        analyzeTable.analyze();
        assert stack.empty();
    }
}
