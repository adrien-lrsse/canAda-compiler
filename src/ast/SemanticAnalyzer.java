package ast;

import asm.CodeGenerator;
import tds.*;
import tds.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.io.IOException;
import java.util.*;

public class SemanticAnalyzer {
    private Stack<Integer> stack;
    private GraphViz ast;
    private TDS tds;
    private Stack<Integer> currentDecl;
    private CodeGenerator codeGen;
    private int returnNeeded;
    private int returnNeededTmp;
    private int lastOffset;

    public SemanticAnalyzer(GraphViz ast) {
        this.stack = new Stack<>();
        this.ast = ast;
        this.tds = new TDS();
        this.currentDecl = new Stack<>();
        this.returnNeeded = 0;
        this.returnNeededTmp = 0;
        this.codeGen = new CodeGenerator();
        this.lastOffset = 0;
    }

    public void analyze() throws SemanticException {
        try {
            // init stack with imported functions
            stack.push(tds.newRegion());
            Proc putInt = new Proc(0, -1);
            putInt.setName("put");
            putInt.addType("integer");
            tds.addSymbol(stack.lastElement(), putInt, -1);
            Proc putChar = new Proc(0, -1);
            putChar.setName("put");
            putChar.addType("character");
            tds.addSymbol(stack.lastElement(), putChar, -1);
            Func characterVal = new Func(0, -1);
            characterVal.setName("character'val");
            characterVal.setReturnType("character");
            characterVal.addType("integer");
            tds.addSymbol(stack.lastElement(), characterVal, -1);

            // DFT on AST
            int tmp;
            Stack<Integer> offset = new Stack<>();
            List<String> undefinedTypes = new ArrayList<>();
            int fatherInt;
            String fatherName;
            for (Node node : ast.getDepthFirstTraversal()) {
                switch (node.getLabel()) {
                    case "ROOT":
                        stack.push(tds.newRegion());
                        break;
                    case "PROCEDURE":
                        // reset offset
                        offset.push(0);

                        // check if all the declared types are defined
                        if (!undefinedTypes.isEmpty()) {
                            throw new SemanticException("Following types are declared but not defined (or defined too late): " + undefinedTypes, node.getLine());
                        }

                        // insert procedure in TDS
                        tmp = stack.pop();
                        Proc proc = new Proc(stack.size(), stack.lastElement());
                        stack.push(tmp);
                        proc.setName(ast.getTree().nodes.get(node.getChildren().get(0)).getLabel());
                        currentDecl.push(tds.addSymbol(stack.lastElement(), proc, node.getLine()));


                        // code generation
                        fatherInt = tds.getTds().get(stack.lastElement()).get(currentDecl.lastElement()).getFather();
                        if (fatherInt == 0) {
                            codeGen.procedureGen(proc.getName(), String.valueOf(stack.lastElement()), null); // pass the father name
                        } else {
                            List<Symbol> fatherRegion = tds.getTds().get(fatherInt);
                            fatherName = fatherRegion.get(fatherRegion.size() - 1).getName();
                            codeGen.procedureGen(proc.getName(), String.valueOf(stack.lastElement()), fatherName); // pass the father name
                        }


                        // create new region
                        stack.push(tds.newRegion());
                        break;
                    case "FUNCTION":
                        // reset offset
                        offset.push(0);

                        // check if all the declared types are defined
                        if (!undefinedTypes.isEmpty()) {
                            throw new SemanticException("Undefined types: " + undefinedTypes, node.getLine());
                        }

                        // insert function in TDS
                        tmp = stack.pop();
                        Func func = new Func(stack.size(), stack.lastElement());
                        stack.push(tmp);
                        func.setName(ast.getTree().nodes.get(node.getChildren().get(0)).getLabel());
                        func.setReturnType("");
                        currentDecl.push(tds.addSymbol(stack.lastElement(), func, node.getLine()));

                        // code generation
                        fatherInt = tds.getTds().get(stack.lastElement()).get(currentDecl.lastElement()).getFather();
                        List<Symbol> fatherRegion = tds.getTds().get(fatherInt);
                        fatherName = fatherRegion.get(fatherRegion.size() - 1).getName();
                        codeGen.functionGen(func.getName(), String.valueOf(stack.lastElement()), fatherName);

                        // create new region
                        stack.push(tds.newRegion());
                        returnNeeded = returnNeeded + 1;
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
                            // check if type is defined
                            tmp = stack.pop();
                            if (!(param.getType().equals("boolean") || param.getType().equals("integer") || param.getType().equals("character") || (getSymbolFromLabel(param.getType(), stack.lastElement()) instanceof Record))) {
                                throw new SemanticException("Type '" + param.getType() + "' is not defined", node.getLine());
                            }
                            stack.push(tmp);


                        } else {
                            param.setName(ast.getTree().nodes.get(node.getChildren().get(0)).getLabel());
                            param.setMode(
                                    Objects.equals(ast.getTree().nodes.get(node.getChildren().get(1)).getLabel(), null) ? 0 :
                                            Objects.equals(ast.getTree().nodes.get(node.getChildren().get(1)).getLabel(), "IN") ? 1 : 2
                            );
                            param.setType(ast.getTree().nodes.get(node.getChildren().get(2)).getLabel());
                        }
                        tds.addSymbol(stack.lastElement(), param, node.getLine());

                        // update current declaration
                        tmp = stack.pop();
                        if (tds.getTds().get(stack.lastElement()).get(currentDecl.lastElement()) instanceof Func) {
                            ((Func) tds.getTds().get(stack.lastElement()).get(currentDecl.lastElement())).addType(param.getType());
                            ((Func) tds.getTds().get(stack.lastElement()).get(currentDecl.lastElement())).addMode(param.getMode());
                        } else {
                            ((Proc) tds.getTds().get(stack.lastElement()).get(currentDecl.lastElement())).addType(param.getType());
                            ((Proc) tds.getTds().get(stack.lastElement()).get(currentDecl.lastElement())).addMode(param.getMode());
                        }

                        // code generation gestion des paramètres
                        codeGen.appendToBuffer("\t\t;" + param.getType() + "\t" + param.getName() + "\n");

                        stack.push(tmp);
                        break;
                    case "VARIABLE":
                        tmp = stack.pop();
                        Var var = new Var(stack.size(), stack.lastElement());
                        stack.push(tmp);
                        var.setName(ast.getTree().nodes.get(node.getChildren().get(0)).getLabel());
                        var.setType(ast.getTree().nodes.get(node.getChildren().get(1)).getLabel());
                        if (!(var.getType().equals("boolean") || var.getType().equals("integer") || var.getType().equals("character") || (getSymbolFromLabel(var.getType(), stack.lastElement()) instanceof Record))) {
                            throw new SemanticException("Type '" + var.getType() + "' is not defined", node.getLine());
                        }
                        // update offset
                        if (TDS.offsets.get(var.getType()) == null) {
                            throw new SemanticException("Type '" + var.getType() + "' is not defined", node.getLine());
                        }
                        offset.push(offset.pop() + TDS.offsets.get(var.getType()));
                        var.setOffset(offset.lastElement());
                        tds.addSymbol(stack.lastElement(), var, node.getLine());

                        // assignation in declaration case
                        try {
                            Node value = ast.getTree().nodes.get(node.getChildren().get(2));
                            if (!typeOfOperands(value.getId()).equals(var.getType())) {
                                throw new SemanticException("Expected type " + var.getType() + " for variable '" + var.getName() + "', got " + typeOfOperands(value.getId()), node.getLine());
                            }
                            codeGen.addInitVar(var, value.getId());
                        } catch (IndexOutOfBoundsException e) {
                            // no assignation, init to 0
                            codeGen.addInitVar(var, -1);
                        }
                        break;
                    case "RETURN_TYPE":
                        tmp = stack.pop();
                        Symbol symbol = tds.getTds().get(stack.lastElement()).get(currentDecl.lastElement());
                        if (tds.getTds().get(stack.lastElement()).get(currentDecl.lastElement()) instanceof Func) {
                            String return_type = ast.getTree().nodes.get(node.getChildren().get(0)).getLabel();
                            if (!(return_type.equals("boolean") || return_type.equals("integer") || return_type.equals("character") || (getSymbolFromLabel(return_type, stack.lastElement()) instanceof Record))) {
                                throw new SemanticException("Type '" + return_type + "' is not defined", node.getLine());
                            }
                            ((Func) tds.getTds().get(stack.lastElement()).get(currentDecl.lastElement())).setReturnType(return_type);
                        }

                        // code generation gestion du type de retour
                        codeGen.appendToBuffer("\t;RETURN_TYPE\n\t\t;" + ((Func) tds.getTds().get(stack.lastElement()).get(currentDecl.lastElement())).getReturnType() + "\n");

                        stack.push(tmp);

//                        computeOffsets(symbol, stack.lastElement());
                        break;
                    case "TYPE":
                        String name = ast.getTree().nodes.get(node.getChildren().get(0)).getLabel();
                        // if type is only declared
                        if (node.getChildren().size() < 2) {
                            if (undefinedTypes.contains(name)) {
                                throw new SemanticException("Type '" + name + "' already declared", node.getLine());
                            }
                            undefinedTypes.add(name);
                            break;
                        }

                        // if type is declared and defined
                        tmp = stack.pop();
                        tds.Record record = new Record(stack.size(), stack.lastElement());
                        stack.push(tmp);
                        record.setName(ast.getTree().nodes.get(node.getChildren().get(0)).getLabel());
                        record.setOffset(0);
                        if (ast.getTree().nodes.get(node.getChildren().get(1)).getLabel().equals("RECORD")) {
                            Node child;
                            for (int i = 0; i < ast.getTree().nodes.get(node.getChildren().get(1)).getChildren().size(); i++) {
                                child = ast.getTree().nodes.get(ast.getTree().nodes.get(node.getChildren().get(1)).getChildren().get(i));
                                String type = ast.getTree().nodes.get(child.getChildren().get(1)).getLabel();
                                if (!(type.equals("boolean") || type.equals("integer") || type.equals("character") || (getSymbolFromLabel(type, stack.lastElement()) instanceof Record))) {
                                    throw new SemanticException("Type '" + type + "' is not defined", node.getLine());
                                }
                                record.addField(ast.getTree().nodes.get(child.getChildren().get(0)).getLabel(), type, child.getLine());
                                if (!(ast.getTree().nodes.get(child.getChildren().get(1)).getLabel().equals("boolean") || ast.getTree().nodes.get(child.getChildren().get(1)).getLabel().equals("integer") || ast.getTree().nodes.get(child.getChildren().get(1)).getLabel().equals("character") || (getSymbolFromLabel(ast.getTree().nodes.get(child.getChildren().get(1)).getLabel(), stack.lastElement()) instanceof Record))) {
                                    throw new SemanticException("Type '" + ast.getTree().nodes.get(child.getChildren().get(1)).getLabel() + "' is not defined", node.getLine());
                                }
                            }
                            offset.push(offset.pop() + record.getOffset());
                            TDS.offsets.put(record.getName(), record.getOffset());
                        }
                        tds.addSymbol(stack.lastElement(), record, node.getLine());
                        // remove type from undefined types
                        undefinedTypes.remove(name);
                        break;
                    case "DECLARATIONS":
                        // compute offsets
                        tmp = stack.pop();
                        symbol = tds.getTds().get(stack.lastElement()).get(currentDecl.lastElement());
                        stack.push(tmp);
                        computeOffsets(symbol, stack.lastElement());

                        // reset offset
                        offset.pop();
                        offset.push(0);
                        break;
                    case "INSTRUCTIONS":
                        //  code generation
                        this.codeGen.switchForVar();
                        List<Symbol> delayedVarGen = tds.getTds().get(stack.lastElement());

//                        this.codeGen.varGen(ast, tds.getTds().get(stack.lastElement()));

                        // check if all the declared types are defined
                        if (!undefinedTypes.isEmpty()) {
                            throw new SemanticException("Undefined types: " + undefinedTypes, node.getLine());
                        }

                        // code generation
                        this.codeGen.appendToBuffer("\t;BEGIN of instructions\n");
                        analyzeInstructions(node.getId(), currentDecl.lastElement(), returnNeeded);

                        tmp = stack.pop();
                        if (tds.getTds().get(stack.lastElement()).get(this.currentDecl.lastElement()) instanceof Func) {
                            if (returnNeededTmp == returnNeeded) {
                                throw new SemanticException("Missing return statement ", ast.getTree().nodes.get(node.getId()).getLine());
                            }
                        }
                        stack.push(tmp);

                        stack.pop();
                        int index = currentDecl.pop();

                        // check if a return is no more needed
                        if (tds.getTds().get(stack.lastElement()).get(index) instanceof Func) {
                            returnNeeded = returnNeeded - 1;
                        }

                        this.codeGen.varGen(ast, delayedVarGen, this.lastOffset);
                        // end of block for code generation
                        this.codeGen.appendToBuffer("\t;END of instructions\n");
                        codeGen.endBlock();
                        // pop offset
                        offset.pop();
                        break;
                }
            }
        } catch (SemanticException e) {
            StringBuilder error = new StringBuilder("SEMANTIC ERROR: " + e.getMessage() + "\n");
            if (stack.size() == currentDecl.size() + 2) {
                stack.pop();
            }
            while (!currentDecl.empty()) {
                error.append("  ├in ").append(tds.getTds().get(stack.pop()).get(currentDecl.pop()).getName()).append("\n");
            }
            error.append("  └in ").append(ast.getTree().nodes.get(stack.pop()).getLabel()).append("\n");
            throw new SemanticException(error.toString(), -1);
        }
        codeGen.writeDownBlocks();
    }


    public void analyzeInstructions(int instructionNode, int currentDecl, int returnNeeded) throws SemanticException {
        List<Integer> childrens = ast.getTree().nodes.get(instructionNode).getChildren();
        returnNeededTmp = returnNeeded;
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
                case "WHILE":
                    analyseWhile(children);
                    break;
                case "CALL":
                    analyzeCall(children);
                    break;
                case "END":
                    analyseEnd(children, currentDecl);
                    break;
                case "RETURN_EXPRESSION":
                    analyseReturnExpression(children, currentDecl);
                    break;
            }
        }
    }

    private void analyseWhile(Integer nodeInt) throws SemanticException {
        Node node = ast.getTree().nodes.get(nodeInt);
        List<Integer> childrens = node.getChildren();
        for (Integer children : childrens) {
            Node nodeChild = ast.getTree().nodes.get(children);
            switch (nodeChild.getLabel()) {
                case "CONDITION":
                    analyseCondition(children);

                    // code generation
                    codeGen.appendToBuffer("\twhile" + nodeInt + " ; while\n");
                    int reg = codeGen.stackFrames.peek().getRegisterManager().borrowRegister();
                    codeGen.expressionGen(ast, nodeChild.getChildren().get(0), reg);
                    codeGen.appendToBuffer("\tcmp\tr" + reg + ", #0 ; Condition\n");
                    codeGen.appendToBuffer("\tbeq\twhile" + nodeInt + "_end ; Jump to end\n");
                    break;
                case "DO":
                    analyzeInstructions(children, currentDecl.lastElement(), returnNeededTmp);

                    // code generation
                    codeGen.appendToBuffer("\tb\twhile" + nodeInt + " ; Jump to while\n");
                    codeGen.appendToBuffer("\twhile" + nodeInt + "_end ; end of while\n");
                    break;
            }
        }
    }

    private void analyzeAssignation(Integer nodeInt) throws SemanticException {
        Node node = ast.getTree().nodes.get(nodeInt);
        Symbol symbol = getSymbolFromLabel(ast.getTree().nodes.get(node.getChildren().get(0)).getLabel(), stack.lastElement());
        // Not defined
        if (symbol == null) {
            throw new SemanticException("'" + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + "' is not defined", node.getLine());
        }
        // Procedure and funtions cannot be assigned
        if (symbol instanceof Func) {
            throw new SemanticException("'" + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + "' is a function  and cannot be assigned", node.getLine());
        }
        if (symbol instanceof Proc) {
            throw new SemanticException("'" + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + "' is a procedure  and cannot be assigned", node.getLine());
        }
        // Left side of assignation is variable
        if (symbol instanceof Var) {
            if (((Var) (symbol)).isProtected()) {
                throw new SemanticException("'" + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + "' is a loop index and cannot be modified", node.getLine());
            }
        }
        // In parameter cannot be assigned
        if (symbol instanceof Param) {
            if (((Param) (symbol)).getMode() == 1) {
                throw new SemanticException("'" + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + "' is an 'in' parameter and cannot be modified", node.getLine());
            }
        }
        String rightType = typeOfOperands(node.getChildren().get(1));
        String leftType = typeOfOperands(node.getChildren().get(0));
        if (rightType.equals("undefined")) {
            throw new SemanticException(ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + " operation between different types", node.getLine());
        } else if (!(rightType.equals(leftType))) {
            throw new SemanticException(leftType + " cannot be assigned to type " + rightType, node.getLine());
        }
        this.codeGen.assignationGen(ast, node);
    }

    private void analyzeIf(Integer node) throws SemanticException {
        returnNeededTmp = returnNeededTmp + 1;
        List<Integer> children = ast.getTree().nodes.get(node).getChildren();
        for (Integer child : children) {
            Node nodeChild = ast.getTree().nodes.get(child);
            switch (nodeChild.getLabel()) {
                case "IF":
                    analyzeIf(child);
                case "CONDITION":
                    analyseCondition(child);

                    // code generation
                    int reg = codeGen.stackFrames.peek().getRegisterManager().borrowRegister();
                    codeGen.expressionGen(ast, nodeChild.getChildren().get(0), reg);
                    codeGen.appendToBuffer("\tcmp\tr" + reg + ", #0 ; Condition\n");
                    codeGen.appendToBuffer("\tbeq\tif" + node + " ; Jump to else\n");
                    break;
                case "THEN":
                    analyzeThen(child);

                    // code generation
                    codeGen.appendToBuffer("\tb\tif" + node + "_end ; Jump to end\n");
                    codeGen.appendToBuffer("\tif" + node + " ; else\n");
                    break;
                case "ELSIF":
                    analyzeElsif(child, node);
                    break;
                case "ELSE":
                    analyzeElse(child);
                    break;
            }
        }
        if (returnNeededTmp > returnNeeded) {
            returnNeededTmp = returnNeededTmp - 1;
        }

        // code generation
        codeGen.appendToBuffer("\tif" + node + "_end ; end of if\n");
    }

    private void analyseCondition(Integer nodeInt) throws SemanticException {
        Node node = ast.getTree().nodes.get(nodeInt);
        if (!typeOfOperands(node.getChildren().get(0)).equals("boolean")) {
            throw new SemanticException("Expected boolean in condition, got " + typeOfOperands(node.getChildren().get(0)), node.getLine());
        }
    }

    private void analyzeThen(Integer node) throws SemanticException {
        analyzeInstructions(node, currentDecl.lastElement(), returnNeededTmp);
    }

    private void analyzeElsif(Integer node, Integer ifId) throws SemanticException {
        returnNeededTmp = returnNeededTmp + 1;
        List<Integer> childrens = ast.getTree().nodes.get(node).getChildren();
        for (Integer children : childrens) {
            Node nodeChild = ast.getTree().nodes.get(children);
            switch (nodeChild.getLabel()) {
                case "IF":
                    analyzeIf(children);
                case "CONDITION":
                    analyseCondition(children);

                    // code generation
                    int reg = codeGen.stackFrames.peek().getRegisterManager().borrowRegister();
                    codeGen.expressionGen(ast, nodeChild.getChildren().get(0), reg);
                    codeGen.appendToBuffer("\tcmp\tr" + reg + ", #0 ; Condition\n");
                    codeGen.appendToBuffer("\tbeq\tif" + node + " ; Jump to else\n");
                    break;
                case "THEN":
                    analyzeThen(children);

                    // code generation
                    codeGen.appendToBuffer("\tb\tif" + ifId + "_end ; Jump to end\n");
                    codeGen.appendToBuffer("\tif" + node + " ; else\n");
                    break;
                case "ELSIF":
                    analyzeElsif(children, node);
                    break;
                case "ELSE":
                    analyzeElse(children);
                    break;
            }
        }
        if (returnNeededTmp > returnNeeded) {
            returnNeededTmp = returnNeededTmp - 1;
        }
    }

    private void analyzeElse(Integer node) throws SemanticException {
        analyzeInstructions(node, currentDecl.lastElement(), returnNeededTmp);
    }

    private void analyseReturnExpression(Integer node, int currentDecl) throws SemanticException {
        returnNeededTmp = returnNeededTmp - 1;

        int child = ast.getTree().nodes.get(node).getChildren().get(0);

        String returnType = typeOfOperands(child);
        String wanted;
        String name;

        int tmp = stack.pop();

        if (tds.getTds().get(stack.lastElement()).get(currentDecl) instanceof Func) {
            wanted = ((Func) tds.getTds().get(stack.lastElement()).get(currentDecl)).getReturnType();
            name = ((Func) tds.getTds().get(stack.lastElement()).get(currentDecl)).getName();
        } else {
            throw new SemanticException("Return statement in a procedure", ast.getTree().nodes.get(node).getLine());
        }

        stack.push(tmp);

        if (!(returnType.equals(wanted))) {
            throw new SemanticException("Return type ('" + returnType + "') does not match the declaration ('" + wanted + "')", ast.getTree().nodes.get(node).getLine());
        }
        this.codeGen.returnValue(ast, node, name);
    }

    private void analyzeFor(Integer nodeInt) throws SemanticException {
        List<Integer> children = ast.getTree().nodes.get(nodeInt).getChildren();
        Node node = ast.getTree().nodes.get(children.get(0));
        int temp = stack.pop();
        Var incr = new Var(stack.size(), stack.lastElement());
        stack.push(temp);
        incr.setProtected(true);
        incr.setName(node.getLabel());
        incr.setType("integer");
        lastOffset += 4;
        incr.setOffset(lastOffset);
        tds.addSymbol(stack.lastElement(), incr, node.getLine());

        codeGen.addInitVar(incr, -1);


        String lbl = ast.getTree().nodes.get(children.get(1)).getLabel();
        boolean reverse = false;
        if (lbl.equals("REVERSE")) {
            reverse = true;
            this.codeGen.forAssignationGen(ast, children.get(3), incr.getName());
        } else {
            this.codeGen.forAssignationGen(ast, children.get(1), incr.getName());
        }

        this.codeGen.appendToBuffer("\tfor"+nodeInt+" ; begin of for\n");

        analyzeInstructions(children.get(children.size() - 1), currentDecl.lastElement(), returnNeededTmp);


        this.codeGen.forCheckEnd(ast, incr.getName(), children.get(2), reverse); // check if end


        this.codeGen.appendToBuffer("\tbeq\tendfor"+nodeInt+" ; jump to the end of the loop\n");
        this.codeGen.forIncrement(incr.getName(), reverse); // increment or decrement
        this.codeGen.appendToBuffer("\tb\tfor"+nodeInt+" ; jump to the beginning of the loop\n\tendfor"+nodeInt+" ; end of for\n");

        tds.getTds().get(stack.lastElement()).remove(incr);
    }

    private void analyseEnd(Integer node, int currentDecl) throws SemanticException {

        String endLabel = "";
        if (!ast.getTree().nodes.get(node).getChildren().isEmpty()) {
            endLabel = ast.getTree().nodes.get(ast.getTree().nodes.get(node).getChildren().get(0)).getLabel();
        }

        int tmp;

        if (!(endLabel.isEmpty())) {
            tmp = stack.pop();
            String wanted = tds.getTds().get(stack.lastElement()).get(currentDecl).getName();
            if (!(endLabel.equals(wanted))) {
                throw new SemanticException("End label ('" + endLabel + "') does not match the declaration ('" + tds.getTds().get(stack.lastElement()).get(currentDecl).getName() + "')", ast.getTree().nodes.get(node).getLine());
            }
            stack.push(tmp);
        }
    }

    public void analyzeCall(int nodeInt) throws SemanticException {
        this.codeGen.appendToBuffer("\n\t; Start of calling stack\n");
        Node callNode = ast.getTree().nodes.get(nodeInt);
        Node labelNode = ast.getTree().nodes.get(callNode.getChildren().get(0));
        Symbol symbol;

        // Exception "put" that supports overloading
        if (labelNode.getLabel().equals("put")) {
            if (labelNode.getChildren().size() != 1) {
                throw new SemanticException("Expected 1 parameter, got " + labelNode.getChildren().size() + " for procedure 'put'", callNode.getLine());
            }
            symbol = switch (typeOfOperands(labelNode.getChildren().get(0))) {
                case "integer" -> tds.getTds().get(0).get(0);
                case "character" -> tds.getTds().get(0).get(1);
                default ->
                        throw new SemanticException("Expected type 'integer' or 'character' for parameter 1 of procedure 'put', got " + typeOfOperands(labelNode.getChildren().get(0)), callNode.getLine());
            };
            this.codeGen.stackArg(ast, labelNode.getChildren().get(0), false);
            codeGen.callGen(symbol, getRegionFromLabel(symbol.getName(), stack.peek()));
            return;
        } else {
            symbol = getSymbolFromLabel(labelNode.getLabel(), stack.lastElement());
        }

        if (symbol == null) {
            throw new SemanticException("Function or procedure '" + labelNode.getLabel() + "' is not defined", callNode.getLine());

        } else if (symbol instanceof Func) {

            if (labelNode.getChildren().size() != ((Func) symbol).getTypes().size()) {
                throw new SemanticException("Expected " + ((Func) symbol).getTypes().size() + " parameters, got " + labelNode.getChildren().size() + " for function '" + labelNode.getLabel() + "'", callNode.getLine());
            }
            String type;
            this.codeGen.setNewFunc(true);
            for (int i = 0; i < labelNode.getChildren().size(); i++) {
                type = typeOfOperands(labelNode.getChildren().get(i));
                if (!type.equals(((Func) symbol).getTypes().get(i))) {
                    throw new SemanticException("Expected type " + ((Func) symbol).getTypes().get(i) + " for parameter " + (i + 1) + " of function '" + labelNode.getLabel() + "', got " + typeOfOperands(labelNode.getChildren().get(i)), callNode.getLine());
                }
                if (((Func) symbol).getModes().get(i) == 2) {
                    if ((!getNatureOfLabel(labelNode.getChildren().get(i), stack.lastElement()).equals("variable"))) {
                        throw new SemanticException("Expected a 'variable' or 'x.f' with x type record for parameter 'in out' " + (i + 1) + " of function '" + labelNode.getLabel() + "', got " + getNatureOfLabel(labelNode.getChildren().get(i), stack.lastElement()), callNode.getLine());
                    }
                }
                this.codeGen.stackArg(ast, labelNode.getChildren().get(i), true);
                this.codeGen.setNewFunc(false);
            }
            this.codeGen.setNewFunc(false);
            this.codeGen.stackReturn(symbol, getRegionFromLabel(symbol.getName(), stack.peek()));

        } else if (symbol instanceof Proc) {
            if (labelNode.getChildren().size() != ((Proc) symbol).getTypes().size()) {
                throw new SemanticException("Expected " + ((Proc) symbol).getTypes().size() + " parameters, got " + labelNode.getChildren().size() + " for procedure '" + labelNode.getLabel() + "'", callNode.getLine());
            }
            if (labelNode.getLabel().equals("put")) {
                if (labelNode.getChildren().size() != 1) {
                    throw new SemanticException("Expected 1 parameter, got " + labelNode.getChildren().size() + " for procedure '" + labelNode.getLabel() + "'", callNode.getLine());
                }
                if (!typeOfOperands(labelNode.getChildren().get(0)).equals("integer") && !typeOfOperands(labelNode.getChildren().get(0)).equals("character")) {
                    throw new SemanticException("Expected type 'integer' or 'character' for parameter 1 of procedure '" + labelNode.getLabel() + "', got " + typeOfOperands(labelNode.getChildren().get(0)), callNode.getLine());
                }
            } else {
                for (int i = 0; i < labelNode.getChildren().size(); i++) {
                    if (!typeOfOperands(labelNode.getChildren().get(i)).equals(((Proc) symbol).getTypes().get(i))) {
                        throw new SemanticException("Expected type " + ((Proc) symbol).getTypes().get(i) + " for parameter " + (i + 1) + " of procedure '" + labelNode.getLabel() + "', got " + typeOfOperands(labelNode.getChildren().get(i)), callNode.getLine());
                    }
                    this.codeGen.stackArg(ast, labelNode.getChildren().get(i), false);
                }
            }
        } else {
            throw new SemanticException("Symbol '" + labelNode.getLabel() + "' is not callable", callNode.getLine());
        }
        // code generation
        codeGen.callGen(symbol, getRegionFromLabel(symbol.getName(), stack.peek()));
    }


    public String typeOfOperands(int nodeInt) throws SemanticException {
        Node node = ast.getTree().nodes.get(nodeInt);
        // Cas de base
        if (node.getChildren().isEmpty()) {
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
        if (node.getChildren().size() == 1 && node.getLabel().equals("CALL")) {
            analyzeCall(nodeInt);
            List<Integer> childrens = ast.getTree().nodes.get(nodeInt).getChildren();
            Node nodeCall = ast.getTree().nodes.get(childrens.get(0));
            Symbol symbol = getSymbolFromLabel(nodeCall.getLabel(), stack.lastElement());
            if (symbol instanceof Proc) {
                throw new SemanticException("'" + ast.getTree().nodes.get(childrens.get(0)).getLabel() + "' is a procedure and return nothing", node.getLine());
            }
            return getTypeOfLabel(childrens.get(0), stack.lastElement());
        }
        if (node.getChildren().size() == 1 && node.getLabel().equals("NOT")) {
            if (typeOfOperands(node.getChildren().get(0)).equals("boolean")) {
                return "boolean";
            } else {
                throw new SemanticException("Expected boolean, got " + typeOfOperands(node.getChildren().get(0)), node.getLine());
            }
        }
        Node nodeSon = ast.getTree().nodes.get(node.getChildren().get(0));
        if (node.getLabel().equals("CHARACTER'VAL")) {
            if (typeOfOperands(nodeSon.getId()).equals("integer")) {
                return "character";
            } else {
                throw new SemanticException("Expected integer, got " + typeOfOperands(nodeSon.getId()), node.getLine());
            }
        }
        if (node.getChildren().size() == 1 && nodeSon.getLabel().equals("ACCESS_IDENT")) {
            Symbol symbol = getSymbolFromLabel(node.getLabel(), stack.lastElement());
            if (symbol == null) {
                throw new SemanticException("Symbol '" + node.getLabel() + "' is not defined", node.getLine());
            } else if (symbol instanceof Var) {
                Symbol record = getSymbolFromLabel(((Var) symbol).getType(), stack.lastElement());
                if (record instanceof Record) {
                    return typeOfField((Record) record, nodeSon.getChildren().get(0));
                } else {
                    throw new SemanticException("'" + ((Var) symbol).getType() + "' is not a record", node.getLine());
                }
            } else if (symbol instanceof Param) {
                Symbol record = getSymbolFromLabel(((Param) symbol).getType(), stack.lastElement());
                if (record instanceof Record) {
                    return typeOfField((Record) record, nodeSon.getChildren().get(0));
                } else {
                    throw new SemanticException("'" + ((Param) symbol).getType() + "' is not a record", node.getLine());
                }
            } else {
                throw new SemanticException("Symbol '" + node.getLabel() + "' is not a record", node.getLine());
            }
        }
        // Cas récursif
        else {
            Node nodeLeft = ast.getTree().nodes.get(node.getChildren().get(0));
            Node nodeRight = ast.getTree().nodes.get(node.getChildren().get(1));
            String typeRight = typeOfOperands(nodeRight.getId());
            String typeLeft = typeOfOperands(nodeLeft.getId());
            if (node.getLabel().equals(("AND")) || node.getLabel().equals("OR") || node.getLabel().equals("=") || node.getLabel().equals("<") || node.getLabel().equals(">") || node.getLabel().equals("<=") || node.getLabel().equals(">=") || node.getLabel().equals("/=")) {
                if (typeOfOperands(nodeLeft.getId()).equals(typeOfOperands(nodeRight.getId())) && !typeOfOperands(nodeLeft.getId()).equals("undefined") && !typeOfOperands(nodeRight.getId()).equals("undefined")) {
                    return "boolean";
                }
                return "undefined";
            }

            return Objects.equals(typeLeft, typeRight) ? typeLeft : "undefined";
        }
    }

    public String getTypeOfLabel(int nodeInt, int region) {
        int father = 0;
        for (Symbol symbol : tds.getTds().get(region)) {
            father = symbol.getFather();
            if (symbol.getName().equals(ast.getTree().nodes.get(nodeInt).getLabel())) {
                if (symbol instanceof Record) {
                    return symbol.getName();
                } else if (symbol instanceof Func) {
                    return ((Func) symbol).getReturnType();
                } else if (symbol instanceof Var) {
                    return ((Var) symbol).getType();
                } else if (symbol instanceof Param) {
                    return ((Param) symbol).getType();
                } else {
                    return "undefined";
                }
            }
        }
        if (region != 0) {
            return getTypeOfLabel(nodeInt, father);
        } else {
            return "undefined";
        }
    }

    public String getNatureOfLabel(int nodeInt, int region) {
        int father = 0;
        for (Symbol symbol : tds.getTds().get(region)) {
            father = symbol.getFather();
            if (symbol.getName().equals(ast.getTree().nodes.get(nodeInt).getLabel())) {
                if (symbol instanceof Record) {
                    return "record";
                } else if (symbol instanceof Func) {
                    return "function";
                } else if (symbol instanceof Var) {
                    return "variable";
                } else if (symbol instanceof Param) {
                    return "parameter";
                } else {
                    return "undefined";
                }
            }
        }
        if (region != 0) {
            return getNatureOfLabel(nodeInt, father);
        } else {
            return "undefined";
        }
    }

    public Symbol getSymbolFromLabel(String label, int region) {
        int father = 0;
        for (Symbol symbol : tds.getTds().get(region)) {
            father = symbol.getFather();
            if (symbol.getName().equals(label)) {
                return symbol;
            }
        }
        if (region != 0) {
            return getSymbolFromLabel(label, father);
        } else {
            return null;
        }
    }

    public int getRegionFromLabel(String label, int region) {
        int father = 0;
        for (Symbol symbol : tds.getTds().get(region)) {
            father = symbol.getFather();
            if (symbol.getName().equals(label)) {
                return region;
            }
        }
        if (region != 0) {
            return getRegionFromLabel(label, father);
        } else {
            return -1;
        }
    }

    public void computeOffsets(Symbol callable, int region) throws SemanticException {
        int offset = (callable instanceof Func) ? TDS.offsets.get(((Func) callable).getReturnType()) : 0;
        int firstOffset = offset;
        List<Symbol> symbols = tds.getTds().get(region);
        Symbol symbol;
        for (int i = symbols.size() - 1; i >= 0; i--) {
            symbol = symbols.get(i);
            if (symbol instanceof Param) {
                offset += TDS.offsets.get(((Param) symbol).getType());
                ((Param) symbol).setOffset(offset);
                this.lastOffset = offset;
            }
        }

        // fake param if no one in order to make the chaining work
        if (offset == firstOffset) {
            int tmp = stack.pop();
            Param param = new Param(stack.size() + 1, stack.peek());
            stack.push(tmp);
            param.setName("#fake");
            param.setType("integer");
            param.setOffset(0);
            tds.addSymbol(region, param, -1);
            this.lastOffset = 0;
        }
    }

    public void setCodeGen(CodeGenerator codeGen) {
        this.codeGen = codeGen;
    }

    public String typeOfField(Record record, int nodeInt) throws SemanticException {
        Node node = ast.getTree().nodes.get(nodeInt);

        if (node.getChildren().isEmpty()) {
            if (record.getFields().containsKey(node.getLabel())) {
                return record.getFields().get(node.getLabel());
            } else {
                throw new SemanticException("'" + node.getLabel() + "' is not a field of '" + record.getName() + "'", node.getLine());
            }
        } else {
            Node nodeSon = ast.getTree().nodes.get(node.getChildren().get(0));
            Node nodeSonSon = ast.getTree().nodes.get(nodeSon.getChildren().get(0));
            if (record.getFields().containsKey(node.getLabel())) {
                Symbol symbol = getSymbolFromLabel(record.getFields().get(node.getLabel()), stack.lastElement());
                if (symbol == null) {
                    throw new SemanticException("'" + node.getLabel() + "' has type '" + record.getFields().get(node.getLabel()) + "'. But type '" + record.getFields().get(node.getLabel()) + "' is not defined", node.getLine());
                } else if (symbol instanceof Record) {
                    return typeOfField((Record) symbol, nodeSonSon.getId());
                } else {
                    throw new SemanticException("'" + record.getFields().get(node.getLabel()) + "' must be a record type", node.getLine());
                }
            } else {
                throw new SemanticException("'" + node.getLabel() + "' is not a field of '" + record.getName() + "'", node.getLine());
            }

        }
    }

    public TDS getTds() {
        return tds;
    }

    public Stack<Integer> getStack() {
        return stack;
    }
}
