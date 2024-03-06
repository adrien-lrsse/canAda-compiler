package ast;

import tds.*;
import tds.Record;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class SemanticAnalyzer {
    private Stack<Integer> stack;
    private GraphViz ast;
    private TDS tds;

    public SemanticAnalyzer(GraphViz ast) {
        this.stack = new Stack<>();
        this.ast = ast;
        this.tds = new TDS();
    }

    public void analyze() throws SemanticException {
        // init stack with imported functions
        stack.push(tds.newRegion());
        Proc putInt = new Proc(0, -1);
        putInt.setName("put");
        putInt.addType("integer");
        tds.addSymbol(stack.lastElement(), putInt);
        Proc putChar = new Proc(0, -1);
        putChar.setName("put");
        putChar.addType("character");
        tds.addSymbol(stack.lastElement(), putChar);
        Func characterVal = new Func(0, -1);
        characterVal.setName("character'val");
        characterVal.setReturnType("character");
        characterVal.addType("integer");
        tds.addSymbol(stack.lastElement(), characterVal);

        // DFT on AST
        int tmp;
        Stack<Integer> currentDecl = new Stack<>();
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
                    currentDecl.push(tds.addSymbol(stack.lastElement(), proc));

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
                    currentDecl.push(tds.addSymbol(stack.lastElement(), func));

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
                        param.setMode(Objects.equals(ast.getTree().nodes.get(node.getChildren().get(2)).getLabel(), "IN") ? 0 : 1);
                        param.setType(ast.getTree().nodes.get(node.getChildren().get(2)).getLabel());
                    }
                    param.setOffset(4);
                    tds.addSymbol(stack.lastElement(), param);

                    // update current declaration
                    tmp = stack.pop();
                    if (tds.getTds().get(stack.lastElement()).get(currentDecl.lastElement()) instanceof Func) {
                        ((Func) tds.getTds().get(stack.lastElement()).get(currentDecl.lastElement())).addType(param.getType());
                    } else {
                        ((Proc) tds.getTds().get(stack.lastElement()).get(currentDecl.lastElement())).addType(param.getType());
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
                    if (tds.getTds().get(stack.lastElement()).get(currentDecl.lastElement()) instanceof Func) {
                        ((Func) tds.getTds().get(stack.lastElement()).get(currentDecl.lastElement())).setReturnType(ast.getTree().nodes.get(node.getChildren().get(0)).getLabel());
                    }
                    stack.push(tmp);
                    break;
                case "STRUCTURE":
                    if (node.getChildren().size() < 2) {
                        break;
                    }
                    tmp = stack.pop();
                    tds.Record record = new Record(stack.size(), stack.lastElement());
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
                    analyzeInstructions(node.getId(), currentDecl.lastElement());
                    stack.pop();
                    currentDecl.pop();
                    break;
            }
        }
    }


    public void analyzeInstructions(int instructionNode,int currentDecl) throws SemanticException {
        try {
            List<Integer> childrens = ast.getTree().nodes.get(instructionNode).getChildren();
            for (Integer children : childrens) {
                Node node = ast.getTree().nodes.get(children);
                switch (node.getLabel()) {
                    case ":=":
                        analyzeAssignation(children);
                        break;
                    case "IF":
                        analyzeIf(children);
                        break;
                    case "END":
                        analyseEnd(children, currentDecl);
                        break;
                }
            }
        } catch (SemanticException e) {
            StringBuilder error = new StringBuilder("SEMANTIC ERROR: " + e.getMessage() + "\n");
            while (stack.size() > 1) {
                error.append("  ├in ").append(ast.getTree().nodes.get(stack.pop()).getLabel()).append("\n");
            }
            error.append("  └in ").append(ast.getTree().nodes.get(stack.pop()).getLabel()).append("\n");
            throw new SemanticException(error.toString());
        }
    }

    private void analyzeAssignation(Integer nodeInt) throws SemanticException {
        Node node = ast.getTree().nodes.get(nodeInt);
        if (!(isDeclerationInMyParents(node.getChildren().get(0), stack.lastElement()))){
            throw new SemanticException(ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + " is not defined") ;
        }
    }

    private void analyzeIf(Integer node) throws SemanticException{
    }

    private void analyseEnd(Integer node, int currentDecl) throws SemanticException{
        String endLabel = "";
        if (!ast.getTree().nodes.get(node).getChildren().isEmpty()){
            endLabel = ast.getTree().nodes.get(ast.getTree().nodes.get(node).getChildren().get(0)).getLabel();
        }
        if (!(endLabel.isEmpty())){
            int tmp = stack.pop();
            String wanted = tds.getTds().get(stack.lastElement()).get(currentDecl).getName();
            if (!(endLabel.equals(wanted))){
                throw new SemanticException("End label ("+endLabel+") does not match the declaration ("+tds.getTds().get(stack.lastElement()).get(currentDecl).getName()+")");
            }
            stack.push(tmp);
        }
    }


    public boolean isDeclerationInMyParents(int node, int region){
        int father = 0;
        for (Symbol symbol : tds.getTds().get(region)){
            father = symbol.getFather();
            if (symbol.getName().equals(ast.getTree().nodes.get(node).getLabel())){
                return true;
            }
        }
        if (region != 0){
            return isDeclerationInMyParents(node, father);
        } else {
            return false;
        }
    }



    public TDS getTds() {
        return tds;
    }
}
