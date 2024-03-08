package ast;

import tds.*;
import tds.Record;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class SemanticAnalyzer {
    private Stack<Integer> stack;
    private GraphViz ast;
    private TDS tds;
    private Stack<Integer> currentDecl;

    public SemanticAnalyzer(GraphViz ast) {
        this.stack = new Stack<>();
        this.ast = ast;
        this.tds = new TDS();
        this.currentDecl = new Stack<>();
    }

    public void analyze() throws SemanticException {
        try {
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
            List<String> undefinedTypes = new ArrayList<>();
            for (Node node : ast.getDepthFirstTraversal()) {
                switch (node.getLabel()) {
                    case "ROOT":
                        stack.push(tds.newRegion());
                        break;
                    case "PROCEDURE":
                        // check if all the declared types are defined
                        if (!undefinedTypes.isEmpty()) {
                            throw new SemanticException("Following types are declared but not defined (or defined too late): " + undefinedTypes);
                        }

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
                        // check if all the declared types are defined
                        if (!undefinedTypes.isEmpty()) {
                            throw new SemanticException("Undefined types: " + undefinedTypes);
                        }

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
                    case "TYPE":
                        String name = ast.getTree().nodes.get(node.getChildren().get(0)).getLabel();
                        // if type is only declared
                        if (node.getChildren().size() < 2) {
                            if (undefinedTypes.contains(name)) {
                                throw new SemanticException("Type '" + name + "' already declared");
                            }
                            undefinedTypes.add(name);
                            break;
                        }

                        // if type is declared and defined
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
                        // remove type from undefined types
                        undefinedTypes.remove(name);
                        break;
                    case "INSTRUCTIONS":
                        // check if all the declared types are defined
                        if (!undefinedTypes.isEmpty()) {
                            throw new SemanticException("Undefined types: " + undefinedTypes);
                        }

                        analyzeInstructions(node.getId(), currentDecl.lastElement());
                        stack.pop();
                        currentDecl.pop();
                        break;
                }
            }
        } catch (SemanticException e) {
            StringBuilder error = new StringBuilder("SEMANTIC ERROR: " + e.getMessage() + "\n");
            if (stack.size() == currentDecl.size() + 2) {stack.pop();}
            while (!currentDecl.empty()) {
                error.append("  ├in ").append(tds.getTds().get(stack.pop()).get(currentDecl.pop()).getName()).append("\n");
            }
            error.append("  └in ").append(ast.getTree().nodes.get(stack.pop()).getLabel()).append("\n");
            throw new SemanticException(error.toString());
        }
    }


    public void analyzeInstructions(int instructionNode,int currentDecl) throws SemanticException {
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
                case "FOR":
                    analyzeFor(children);
                    break;
                case "END":
                    analyseEnd(children, currentDecl);
                    break;
            }
        }
    }

    private void analyzeAssignation(Integer nodeInt) throws SemanticException {
        Node node = ast.getTree().nodes.get(nodeInt);
        if (isDeclerationInMyParents(node.getChildren().get(0), stack.lastElement()) == 1){
            throw new SemanticException("'" + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + "' is not defined") ;
        } else if (isDeclerationInMyParents(node.getChildren().get(0), stack.lastElement()) == 2){
            throw new SemanticException("'" + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + "' is a loop index and cannot be modified") ;
        }
        String rightType = typeOfOperands(node.getChildren().get(1));
        String leftType = getTypeOfLabel(node.getChildren().get(0), stack.lastElement());
        if (typeOfOperands(node.getChildren().get(1)).equals("undefined")){
            throw new SemanticException(ast.getTree().nodes.get(node.getChildren().get(1)).getLabel()+" operation between different types") ;
        } else if (!(rightType.equals(leftType))) {
            throw new SemanticException(ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + " is type " + leftType + " and cannot be assigned to type " + rightType) ;
        }
    }

    private void analyzeIf(Integer node) throws SemanticException{
    }

    private void analyzeFor(Integer nodeInt) throws SemanticException{
        List<Integer> childrens = ast.getTree().nodes.get(nodeInt).getChildren();
        Node node = ast.getTree().nodes.get(childrens.get(0));
        Var incr = new Var(stack.size(), stack.lastElement());
        incr.setProtected(true);
        incr.setName(node.getLabel());
        incr.setType("integer");
        incr.setOffset(4);
        tds.addSymbol(stack.lastElement(), incr);
        analyzeInstructions(childrens.get(childrens.size()-1), currentDecl.lastElement());
        tds.getTds().get(stack.lastElement()).remove(incr);
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
                throw new SemanticException("End label ('"+endLabel+"') does not match the declaration ('"+tds.getTds().get(stack.lastElement()).get(currentDecl).getName()+"')");
            }
            stack.push(tmp);
        }
    }


    public int isDeclerationInMyParents(int node, int region){
        int father = 0;
        for (Symbol symbol : tds.getTds().get(region)){
            father = symbol.getFather();
            if (symbol.getName().equals(ast.getTree().nodes.get(node).getLabel())){
                if (((Var)symbol).isProtected()) {
                    return 2;
                } else {
                    return 0;
                }
            }
        }
        if (region != 0){
            return isDeclerationInMyParents(node, father);
        } else {
            return 1;
        }
    }

    public String typeOfOperands(int nodeInt){
        Node node = ast.getTree().nodes.get(nodeInt);
        // Cas de base
        if (node.getChildren().isEmpty()){
            if (node.getLabel().matches("\\d+")) {
                return "integer";
            } else if (node.getLabel().charAt(0) == '\'') {
                return "character";
            } else if (node.getLabel().equals("true") || node.getLabel().equals("false")) {
                return "boolean";
            } else {
            return getTypeOfLabel(nodeInt, stack.lastElement());
            }
        }
        if (node.getChildren().size() == 1 && node.getLabel().equals("CALL")){
            List<Integer> childrens = ast.getTree().nodes.get(nodeInt).getChildren();
            return getTypeOfLabel(childrens.get(0), stack.lastElement());
        }
        else {
            // Cas récursif
            Node nodeLeft = ast.getTree().nodes.get(node.getChildren().get(0));
            Node nodeRight = ast.getTree().nodes.get(node.getChildren().get(1));
            return Objects.equals(typeOfOperands(nodeLeft.getId()), typeOfOperands(nodeRight.getId())) ? typeOfOperands(nodeLeft.getId()) : "undefined";
        }
    }

    public String getTypeOfLabel(int nodeInt, int region) {
        int father = 0;
        for (Symbol symbol : tds.getTds().get(region)){
            father = symbol.getFather();
            if (symbol.getName().equals(ast.getTree().nodes.get(nodeInt).getLabel())) {
                return switch (symbol) {
                    case Var var -> var.getType();
                    case Param param -> param.getType();
                    case Record record -> record.getName();
                    case Func func -> func.getReturnType();
                    default -> "undefined";
                };
            }
        }
        if (region != 0){
            return getTypeOfLabel(nodeInt, father);
        } else {
            return "undefined";
        }
    }




    public TDS getTds() {
        return tds;
    }
}
