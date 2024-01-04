package parser;

import ast.GraphViz;
import lexer.Lexer;

import java.io.IOException;
import java.util.Stack;

public class Parser {
    Stack<Integer> stack = new Stack<>();
    Lexer lexer;
    AnalyzeTable analyzeTable;
    GraphViz ast;
    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.analyzeTable = new AnalyzeTable(this);
        this.ast = new GraphViz("ast");
    }
    public void parse() throws IOException {
        analyzeTable.analyze();
        if (!stack.isEmpty()) {
            throw new RuntimeException("Stack is not empty");
        }
    }
}
