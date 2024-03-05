package parser;

import ast.GraphViz;
import ast.Node;
import lexer.Lexer;
import tds.*;
import tds.Record;

import java.io.IOException;
import java.util.Objects;
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
        buildTds();
        tds.display();
    }

    public void printDepthFirstTraversal() {
        for (int i = 0; i < ast.getDepthFirstTraversal().size(); i++) {
            System.out.println(ast.getDepthFirstTraversal().get(i).getLabel());
        }
    }

    public void buildTds() {
        // init stack with imported functions
        stack.push(tds.newRegion());
        Func putInt = new Func(0, -1);
        putInt.setName("put");
        putInt.setReturnType("");
        putInt.addType("integer");
        tds.addSymbol(stack.lastElement(), putInt);
        Func putChar = new Func(0, -1);
        putChar.setName("put");
        putChar.setReturnType("");
        putChar.addType("character");
        tds.addSymbol(stack.lastElement(), putChar);
        Func characterVal = new Func(0, -1);
        characterVal.setName("character'val");
        characterVal.setReturnType("character");
        characterVal.addType("integer");
        tds.addSymbol(stack.lastElement(), characterVal);

        // DFT on AST
        int tmp;
        int currentDecl = 0;
        for (Node node : ast.getDepthFirstTraversal()) {
            switch (node.getLabel()) {
                case "ROOT":
                    stack.push(tds.newRegion());
                    break;
                case "PROCEDURE":
                    // insert procedure in TDS
                    tmp = stack.pop();
                    Proc proc = new Proc(stack.size(), stack.lastElement());
                    stack.push(tmp);
                    proc.setName(ast.getTree().nodes.get(node.getChildren().get(0)).getLabel());
                    currentDecl = tds.addSymbol(stack.lastElement(), proc);

                    // create new region
                    stack.push(tds.newRegion());
                    break;
                case "FUNCTION":
                    // insert function in TDS
                    tmp = stack.pop();
                    Func func = new Func(stack.size(), stack.lastElement());
                    stack.push(tmp);
                    func.setName(ast.getTree().nodes.get(node.getChildren().get(0)).getLabel());
                    func.setReturnType("");
                    currentDecl = tds.addSymbol(stack.lastElement(), func);

                    // create new region
                    stack.push(tds.newRegion());
                    break;
                case "PARAM":
                    // insert param in TDS
                    tmp = stack.pop();
                    Param param = new Param(stack.size(), stack.lastElement());
                    stack.push(tmp);
                    if (node.getChildren().size() == 2) {
                        param.setName(ast.getTree().nodes.get(node.getChildren().get(0)).getLabel());
                        param.setMode(0);
                        param.setType(ast.getTree().nodes.get(node.getChildren().get(1)).getLabel());
                    } else {
                        param.setName(ast.getTree().nodes.get(node.getChildren().get(0)).getLabel());
                        param.setMode(Objects.equals(ast.getTree().nodes.get(node.getChildren().get(2)).getLabel(), "IN") ? 0 : 1); // TODO: check mode OUT
                        param.setType(ast.getTree().nodes.get(node.getChildren().get(2)).getLabel());
                    }
                    param.setOffset(4);
                    tds.addSymbol(stack.lastElement(), param);

                    // update current declaration
                    tmp = stack.pop();
                    if (tds.getTds().get(stack.lastElement()).get(currentDecl) instanceof Func) {
                        ((Func) tds.getTds().get(stack.lastElement()).get(currentDecl)).addType(param.getType());
                    } else {
                        ((Proc) tds.getTds().get(stack.lastElement()).get(currentDecl)).addType(param.getType());
                    }
                    stack.push(tmp);
                    break;
                case "VARIABLE":
                    tmp = stack.pop();
                    Var var = new Var(stack.size(), stack.lastElement());
                    stack.push(tmp);
                    var.setName(ast.getTree().nodes.get(node.getChildren().get(0)).getLabel());
                    var.setType(ast.getTree().nodes.get(node.getChildren().get(1)).getLabel());
                    var.setOffset(4);
                    tds.addSymbol(stack.lastElement(), var);
                    break;
                case "RETURN_TYPE":
                    tmp = stack.pop();
                    if (tds.getTds().get(stack.lastElement()).get(currentDecl) instanceof Func) {
                        ((Func) tds.getTds().get(stack.lastElement()).get(currentDecl)).setReturnType(ast.getTree().nodes.get(node.getChildren().get(0)).getLabel());
                    }
                    stack.push(tmp);
                    break;
                case "STRUCTURE":
                    if (node.getChildren().size() < 2) {
                        break;
                    }
                    tmp = stack.pop();
                    Record record = new Record(stack.size(), stack.lastElement());
                    stack.push(tmp);
                    record.setName(ast.getTree().nodes.get(node.getChildren().get(0)).getLabel());
                    if (ast.getTree().nodes.get(node.getChildren().get(1)).getLabel().equals("RECORD")) {
                        Node child;
                        for (int i = 0; i < ast.getTree().nodes.get(node.getChildren().get(1)).getChildren().size(); i++) {
                            child = ast.getTree().nodes.get(ast.getTree().nodes.get(node.getChildren().get(1)).getChildren().get(i));
                            record.addField(ast.getTree().nodes.get(child.getChildren().get(0)).getLabel(), ast.getTree().nodes.get(child.getChildren().get(1)).getLabel());
                        }
                    }
                    tds.addSymbol(stack.lastElement(), record);
                    break;
                case "INSTRUCTIONS":
                    stack.pop();
                    break;
            }
        }
    }
}
