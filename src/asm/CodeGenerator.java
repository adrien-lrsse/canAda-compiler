package asm;

import ast.GraphViz;
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

    public CodeGenerator(String fileName) {
        try {
            this.fileWriter = new FileWriter(fileName+"-output.s");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.stackFrames = new Stack<>();
        this.asmStack = new Stack<>();
    }

    public void write(String s) {
        try {
            fileWriter.write(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void procedureGen(String name, String last) {
        stackFrames.push(new StackFrame(name + last));
        appendToBuffer("\t;PARAMETERS\n");
    }

    public void functionGen(String name, String last) {
        stackFrames.push(new StackFrame(name + last));
        appendToBuffer("\t;PARAMETERS\n");
    }

    public void varGen(List<Symbol> symbolsOfRegion) {
        this.appendToBuffer("\t;VARIABLES\n");
        int lastOffset = -1;
        for (Symbol symbol : symbolsOfRegion){
            if (symbol instanceof Var){
                lastOffset = ((Var) symbol).getOffset();
                this.appendToBuffer("\t\t;"+((Var) symbol).getType()+"\t"+symbol.getName()+"\n");
            }
        }
        if(lastOffset != -1){
            this.appendToBuffer("\tsub\tr13, r13, #"+lastOffset+"\n");
        }
    }

    public void appendToBuffer(String s) {
        if (!stackFrames.isEmpty()) {
            stackFrames.peek().getBuffer().append(s);
        }
    }

    public void endBlock() {
        if (!stackFrames.isEmpty()) {
            StackFrame stackFrame = stackFrames.pop();
            asmStack.push(stackFrame.toString(stackFrames.isEmpty()));
        }
    }

    public void writeDownBlocks() {
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