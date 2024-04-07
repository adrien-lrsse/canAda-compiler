package asm;

import ast.GraphViz;
import ast.Node;
import tds.Symbol;
import tds.TDS;
import tds.Var;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class CodeGenerator {
    private final FileWriter fileWriter;
    private Stack<StackFrame> stackFrames;
    private Stack<String> asmStack;
    private Boolean codeGenOn = true;

    public CodeGenerator(String fileName) {
        try {
            this.fileWriter = new FileWriter(fileName+"-output.s");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.stackFrames = new Stack<>();
        this.asmStack = new Stack<>();
    }

    public void setCodeGenOn(Boolean codeGenOn) {
        this.codeGenOn = codeGenOn;
    }

    public void write(String s) {
        try {
            fileWriter.write(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void procedureGen(String name, String last) {
        if (codeGenOn) {
            stackFrames.push(new StackFrame(name + last));
            appendToBuffer("\t;PARAMETERS\n");
        }
    }

    public void functionGen(String name, String last) {
        if (codeGenOn) {
            stackFrames.push(new StackFrame(name + last));
            appendToBuffer("\t;PARAMETERS\n");
        }
    }

    public void varGen(List<Symbol> symbolsOfRegion) {
        if (codeGenOn) {
            this.appendToBuffer("\t;VARIABLES\n");
            int lastOffset = -1;
            for (Symbol symbol : symbolsOfRegion) {
                if (symbol instanceof Var) {
                    lastOffset = ((Var) symbol).getOffset();
                    this.appendToBuffer("\t\t;" + ((Var) symbol).getType() + "\t" + symbol.getName() + "\n");
                }
            }
            if (lastOffset != -1) {
                this.appendToBuffer("\tsub\tr11, r11, #" + lastOffset + "\n\tmov\tr13, r11\n");
            }
        }
    }

    public void appendToBuffer(String s) {
        if (codeGenOn) {
            if (!stackFrames.isEmpty()) {
                stackFrames.peek().getBuffer().append(s);
            }
        }
    }

    public void endBlock() {
        if (codeGenOn) {
            if (!stackFrames.isEmpty()) {
                StackFrame stackFrame = stackFrames.pop();
                asmStack.push(stackFrame.toString(stackFrames.isEmpty()));
            }
        }
    }

    public void writeDownBlocks() {
        if (codeGenOn) {
            while (!asmStack.isEmpty()) {
                this.write(asmStack.pop());
            }

            try {
                fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int expressionGen(GraphViz ast, Integer nodeInt, List<Symbol> symbols) {
        if(codeGenOn){
            Node node = ast.getTree().nodes.get(nodeInt);
            String type = node.getLabel();
            try {
                int number = Integer.parseInt(type);
                int register = stackFrames.peek().getRegisterManager().borrowRegister();
                if(number > 1020){
                    appendToBuffer("\tldr\tr" + register + ", =" + number + "\n");
                } else {
                    appendToBuffer("\tmov\tr" + register + ", #" + number + "\n");
                }
                return register;
            } catch (Exception e) {
                // Not a number
            }
            switch (type) {
                case "*" :
                    Integer register1 = expressionGen(ast, node.getChildren().get(0), symbols);
                    Integer register2 = expressionGen(ast, node.getChildren().get(1), symbols);
                    stackFrames.peek().getRegisterManager().borrowRegister();

                    stackFrames.peek().getRegisterManager().freeRegister(register1);
                    stackFrames.peek().getRegisterManager().freeRegister(register2);
                    break;
            }

        }
        return 0;
    }

}