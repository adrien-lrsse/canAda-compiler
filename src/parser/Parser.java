package parser;

import ast.GraphViz;
import ast.Node;
import lexer.Lexer;
import tds.TDS;

import java.io.IOException;
import java.util.Stack;

public class Parser {
    Stack<Integer> stack = new Stack<>();
    Lexer lexer;
    AnalyzeTable analyzeTable;
    GraphViz ast;
    TDS tds;
    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.analyzeTable = new AnalyzeTable(this);
        this.ast = new GraphViz(lexer.getFileName());
        this.tds = new TDS();
    }
    public void parse(boolean export) throws IOException {
        // build AST
        analyzeTable.analyze(export);
        if (!stack.isEmpty()) {
            throw new RuntimeException("Stack is not empty");
        }

        // build TDS
        int id = 0;
        int nesting = 0;
        for (Node node : ast.getDepthFirstTraversal()) {
            System.out.println(node.getLabel());
            if (node.getLabel() == "ROOT" || node.getLabel() == "PACKAGE_BODY") {
                continue;
            }
        }
    }

    public void printDepthFirstTraversal() {
        for (int i = 0; i < ast.getDepthFirstTraversal().size(); i++) {
            System.out.println(ast.getDepthFirstTraversal().get(i).getLabel());
        }
    }
}
