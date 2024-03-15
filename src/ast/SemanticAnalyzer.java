package ast;

import tds.*;
import tds.Record;

import java.io.IOException;
import java.util.*;

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
                case "CALL":
                    analyzeCall(children);
                    break;
                case "END":
                    analyseEnd(children, currentDecl);
                    break;
            }
        }
    }

    private void analyzeAssignation(Integer nodeInt) throws SemanticException {
        Node node = ast.getTree().nodes.get(nodeInt);
        Symbol symbol = getSymbolFromLabel(ast.getTree().nodes.get(node.getChildren().get(0)).getLabel(),stack.lastElement());
        // Not defined
        if (symbol == null){
            throw new SemanticException("'" + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + "' is not defined") ;
        }
        // Procedure and funtions cannot be assigned
        if (symbol instanceof Func){
            throw new SemanticException("'"+ast.getTree().nodes.get(node.getChildren().get(0)).getLabel()+ "' is a function  and cannot be assigned");
        }
        if (symbol instanceof Proc){
            throw new SemanticException("'"+ast.getTree().nodes.get(node.getChildren().get(0)).getLabel()+ "' is a procedure  and cannot be assigned");
        }
        // Left side of assignation is variable
        if (symbol instanceof Var){
            if (((Var)(symbol)).isProtected()){
                throw new SemanticException("'" + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + "' is a loop index and cannot be modified") ;
            }
        }
        // Left side of the assignation is a record
        // TODO

        // Control the matching of type between left and right value

        String rightType = typeOfOperands(node.getChildren().get(1));
        String leftType = typeOfOperands(node.getChildren().get(0));
        if (typeOfOperands(node.getChildren().get(1)).equals("undefined")){
            throw new SemanticException(ast.getTree().nodes.get(node.getChildren().get(1)).getLabel()+" operation between different types") ;
        } else if (!(rightType.equals(leftType))) {
            throw new SemanticException(leftType + " cannot be assigned to type " + rightType);
        }
    }

    private void analyzeIf(Integer node) throws SemanticException{
    }

    private void analyzeFor(Integer nodeInt) throws SemanticException{
        List<Integer> children = ast.getTree().nodes.get(nodeInt).getChildren();
        Node node = ast.getTree().nodes.get(children.get(0));
        int temp = stack.pop();
        Var incr = new Var(stack.size(), stack.lastElement());
        stack.push(temp);
        incr.setProtected(true);
        incr.setName(node.getLabel());
        incr.setType("integer");
        incr.setOffset(4);
        tds.addSymbol(stack.lastElement(), incr);
        analyzeInstructions(children.get(children.size()-1), currentDecl.lastElement());
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

    public void analyzeCall(int nodeInt) throws SemanticException {
        Node callNode = ast.getTree().nodes.get(nodeInt);
        Node labelNode = ast.getTree().nodes.get(callNode.getChildren().get(0));
        Symbol symbol = getSymbolFromLabel(labelNode.getLabel(), stack.lastElement());
        if (symbol == null){
            throw new SemanticException("Function or procedure '" + labelNode.getLabel() + "' is not defined") ;
        } else if (symbol instanceof Func) {
            if (labelNode.getChildren().size() != ((Func) symbol).getTypes().size()) {
                throw new SemanticException("Expected " + ((Func) symbol).getTypes().size() + " parameters, got " + labelNode.getChildren().size() + " for function '" + labelNode.getLabel() + "'");
            }
            for (int i = 0; i < labelNode.getChildren().size(); i++) {
                if (!typeOfOperands(labelNode.getChildren().get(i)).equals(((Func) symbol).getTypes().get(i))) {
                    throw new SemanticException("Expected type " + ((Func) symbol).getTypes().get(i) + " for parameter " + (i + 1) + " of function '" + labelNode.getLabel() + "', got " + typeOfOperands(labelNode.getChildren().get(i)));
                }
            }
        } else if (symbol instanceof Proc) {
            if (labelNode.getChildren().size() != ((Proc) symbol).getTypes().size()) {
                throw new SemanticException("Expected " + ((Proc) symbol).getTypes().size() + " parameters, got " + labelNode.getChildren().size() + " for procedure '" + labelNode.getLabel() + "'");
            }
            for (int i = 0; i < labelNode.getChildren().size(); i++) {
                if (!typeOfOperands(labelNode.getChildren().get(i)).equals(((Proc) symbol).getTypes().get(i))) {
                    throw new SemanticException("Expected type " + ((Proc) symbol).getTypes().get(i) + " for parameter " + (i + 1) + " of procedure '" + labelNode.getLabel() + "', got " + typeOfOperands(labelNode.getChildren().get(i)));
                }
            }
        } else {
            throw new SemanticException("Symbol '" + labelNode.getLabel() + "' is not callable");
        }
    }


    public int isDeclerationInMyParents(int node, int region){
        int father = 0;
        for (Symbol symbol : tds.getTds().get(region)){
            father = symbol.getFather();
            if (symbol.getName().equals(ast.getTree().nodes.get(node).getLabel())){
                if ((symbol instanceof Var)) {
                    if (((Var)(symbol)).isProtected()){
                        return 2;
                    }
                }
                return 0;

            }
        }
        if (region != 0){
            return isDeclerationInMyParents(node, father);
        } else {
            return 1;
        }
    }

    public String typeOfOperands(int nodeInt) throws SemanticException {
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
            analyzeCall(nodeInt);
            List<Integer> childrens = ast.getTree().nodes.get(nodeInt).getChildren();
            Node nodeCall = ast.getTree().nodes.get(childrens.get(0));
            Symbol symbol = getSymbolFromLabel(nodeCall.getLabel(), stack.lastElement());
            if (symbol instanceof Proc){
                throw new SemanticException("'"+ast.getTree().nodes.get(childrens.get(0)).getLabel()+"' is a procedure and return nothing");
            }
            return getTypeOfLabel(childrens.get(0), stack.lastElement());
        }
        if (node.getChildren().size() == 1 && node.getLabel().equals("NOT")){
            // TODO
            return "boolean";
        }
        Node nodeSon = ast.getTree().nodes.get(node.getChildren().get(0));
        if (node.getChildren().size() == 1 && nodeSon.getLabel().equals("ACCESS_IDENT")){
            Symbol symbol = getSymbolFromLabel(node.getLabel(), stack.lastElement());
            if (symbol == null){
                throw new SemanticException("Symbol '" + node.getLabel() + "' is not defined") ;
            } else if (symbol instanceof Var){
                Symbol record = getSymbolFromLabel(((Var) symbol).getType(), stack.lastElement());
                if (record instanceof Record){
                    return typeOfField((Record) record, nodeSon.getChildren().get(0));
                } else {
                    throw new SemanticException("'"+((Var) symbol).getType()+"' is not a record");
                }
            }  else if (symbol instanceof Param){
                Symbol record = getSymbolFromLabel(((Param) symbol).getType(), stack.lastElement());
                if (record instanceof Record){
                    return typeOfField((Record) record, nodeSon.getChildren().get(0));
                } else {
                    throw new SemanticException("'"+((Param) symbol).getType()+"' is not a record");
                }
            } else {
                throw new SemanticException("Symbol '" + node.getLabel() + "' is not a record") ;
            }
        }
        // Cas récursif
        else {
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
                if (symbol instanceof Record){
                    return symbol.getName();
                } else if (symbol instanceof Func){
                    return ((Func) symbol).getReturnType();
                } else if (symbol instanceof Var){
                    return ((Var) symbol).getType();
                } else if (symbol instanceof Param) {
                    return ((Param) symbol).getType();
                } else {
                    return "undefined";
                }
            }
        }
        if (region != 0){
            return getTypeOfLabel(nodeInt, father);
        } else {
            return "undefined";
        }
    }

    public Symbol getSymbolFromLabel(String label, int region){
        int father = 0;
        for (Symbol symbol : tds.getTds().get(region)){
            father = symbol.getFather();
            if (symbol.getName().equals(label)) {
                return symbol;
            }
        }
        if (region != 0){
            return getSymbolFromLabel(label, father);
        } else {
            return null;
        }
    }


    public String typeOfField(Record record, int nodeInt) throws SemanticException {
        Node node = ast.getTree().nodes.get(nodeInt);

        if (node.getChildren().isEmpty()){
            if (record.getFields().containsKey(node.getLabel())){
                return record.getFields().get(node.getLabel());
            } else {
                throw new SemanticException("'"+node.getLabel()+"' is not a field of '"+record.getName()+"'");
            }
        }
        else {
            Node nodeSon = ast.getTree().nodes.get(node.getChildren().get(0));
            System.out.println(nodeSon.getLabel());
            Node nodeSonSon = ast.getTree().nodes.get(nodeSon.getChildren().get(0));
            if (record.getFields().containsKey(node.getLabel())){
                Symbol symbol = getSymbolFromLabel(record.getFields().get(node.getLabel()),stack.lastElement());
                if (symbol == null){
                    throw new SemanticException("'"+node.getLabel()+"' has type '"+record.getFields().get(node.getLabel())+"'. But type '"+record.getFields().get(node.getLabel())+"' is not defined");
                } else if (symbol instanceof Record){
                    return typeOfField((Record) symbol, nodeSonSon.getId());
                } else {
                    throw new SemanticException("'"+record.getFields().get(node.getLabel())+"' must be a record type");
                }
            } else {
                throw new SemanticException("'"+node.getLabel()+"' is not a field of '"+record.getName()+"'");
            }

        }
    }

    public TDS getTds() {
        return tds;
    }

}
