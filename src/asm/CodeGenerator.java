package asm;

import ast.GraphViz;
import ast.Node;
import tds.Func;
import tds.Symbol;
import tds.TDS;
import tds.Var;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;


public class CodeGenerator {
    private final FileWriter fileWriter;
    public Stack<StackFrame> stackFrames; // TO CHANGE
    private Stack<String> asmStack;
    private Boolean codeGenOn = true;
    private Boolean thereIsMult = false;
    private Boolean thereIsDiv = false;
    private List<String> callableElements = new ArrayList<>();

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
    
    public void procedureGen(String name, String last, String fatherName) {
        if (codeGenOn) {
            stackFrames.push(new StackFrame(name + last));
            callableElements.add(name);
            String label = name + callableElements.lastIndexOf(name) + "global";
            if(fatherName == null){
                appendToBuffer("\tadr\tr10, " + label + "\n\tstr\tr13, [r10]\n\tstmfd\tr13!, {r10}\n\t;PARAMETERS\n");
                return;
            }
            String labelParent = fatherName + callableElements.lastIndexOf(fatherName) + "global";
            appendToBuffer("\tadr\tr10, " + label + "\n\tldr\tr10, [r10]\n\tstmfd\tr13!, {r10}\n\tadr\tr10, "+ labelParent +"\n\tldr\tr12, [r10]\n\tstmfd\tr13!, {r12}\n\tadr\tr10, " + label + "\nstr\tr13, [r10]");
            appendToBuffer("\t;PARAMETERS\n");
        }
    }

    public void functionGen(String name, String last, String fatherName) {
        if (codeGenOn) {
            stackFrames.push(new StackFrame(name + last));
            callableElements.add(name);
            String label = name + callableElements.lastIndexOf(name) + "global";
            String labelParent = fatherName + callableElements.lastIndexOf(fatherName) + "global";
            appendToBuffer("\tadr\tr10, " + label + "\n\tldr\tr10, [r10]\n\tstmfd\tr13!, {r10}\n\tadr\tr10, "+ labelParent +"\n\tldr\tr12, [r10]\n\tstmfd\tr13!, {r12}\n\tadr\tr10, " + label + "\nstr\tr13, [r10]");
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
                this.appendToBuffer("\tsub\tr13, r13, #" + lastOffset + "\n");
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
            // copy the content of the print.s file at the beginning of the output file
            try {
                List<String> lines = Files.readAllLines(Paths.get("src/asm/visual/print.s"));
                for (String line : lines) {
                    this.write(line + "\n");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            while (!asmStack.isEmpty()) {
                this.write(asmStack.pop());
            }

            if(thereIsMult){
                this.write("mul\tstmfd\tr13!, {r0-r2, r11, lr} ; This is PreWritten Code for multiplication\n\tmov\tr11, r13\n\tldr\tr1, [r11, #4*6]\n\tldr\tr2, [r11, #4*7]\n\tmov\tr0, #0\nmul_loop\tlsrs\tr2,r2,#1\n\taddcs\tr0,r0,r1\n\tlsl\tr1,r1,#1\n\ttst\tr2,r2\n\tbne\tmul_loop\n\tstr\tr0, [r11, #4*5]\n\tmov\tr13, r11\n\tldmfd\tr13!,{r0-r2, r11, pc}\n\n");
            }
            if(thereIsDiv){
                this.write("div\tstmfd\tsp!, {r0-r5, r11, lr} ; This is PreWritten Code for division\n\tmov\tr11, r13\n\tldr\tr1, [r11, #4*9]\n\tldr\tr2, [r11, #4*10]\n\tmov\tr0,#0\n\tmov\tr3,#0\n\tcmp\tr1, #0\n\trsblt\tr1, r1,#0\n\teorlt\tr3, r3, #1\n\tcmp\tr2, #0\n\trsblt\tr2, r2,#0\n\teorlt\tr3, r3, #1\n\tmov\tr4, r2\n\tmov\tr5, #1\ndiv_max\tlsl\tr4, r4, #1\n\tlsl\tr5,r5,#1\n\tcmp\tr4, r1\n\tble\tdiv_max\ndiv_loop\tlsr\tr4, r4, #1\n\tlsr\tr5, r5, #1\n\tcmp\tr4, r1\n\tbgt\tdiv_loop\n\tadd\tr0, r0, r5\n\tsub\tr1, r1, r4\n\tcmp\tr1, r2\n\tbge\tdiv_loop\n\tcmp\tr3, #1\n\tbne\tdiv_exit\n\tcmp\tr1,#0\n\taddne\tr0, r0,#1\n\trsb\tr0, r0, #0\n\trsb\tr1, r1, #0\n\taddne\tr1,r1,r2\ndiv_exit\tstr\tr0, [r11, #4*8]\n\tldmfd\tr13!, {r0-r5, r11, pc}\n\n");
            }

            try {
                fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void assignationGen(GraphViz ast, Node node, TDS tds) {
//        System.out.println(ast.getTree().nodes.get(node.getChildren().get(0)).getLabel());
//        System.out.println(tds.getTds());
//        System.out.println("--------------------");
        // TODO assign the value
        int exprRegister = 0;
        boolean isRegisterBorrowed = false;
        try {
            exprRegister = stackFrames.peek().getRegisterManager().borrowRegister();
        } catch (RuntimeException e) {
            exprRegister = 0;
            isRegisterBorrowed = true;
            appendToBuffer("\tstmfd\tr13!, {r" + exprRegister + "} ; No more register available, making space with memory stack\n");
        }
        expressionGen(ast, node.getChildren().get(1), tds, exprRegister);
        // Get the var to assign
        // Set the value
        if(isRegisterBorrowed) {
            appendToBuffer("\tldmfd\tr13!, {r" + exprRegister + "} ; Freeing memory stack\n"); // Free the register
        } else {
            stackFrames.peek().getRegisterManager().freeRegister(exprRegister);
        }
    }

    public void stackArg(GraphViz ast, Integer nodeInt, TDS tds) {
        if (codeGenOn) {
            int register;
            boolean isRegisterBorrowed = false;
            try {
                register = stackFrames.peek().getRegisterManager().borrowRegister();
            } catch (RuntimeException e) {
                register = 0;
                isRegisterBorrowed = true;
                appendToBuffer("\tsub\tr13, r13, #4 ; keeping some space to stack the arg\n\tstmfd\tr13!, {r" + register + "} ; No more register available, making space with memory stack\n");
            }
            expressionGen(ast, nodeInt, tds, register);
            if (isRegisterBorrowed) {
                appendToBuffer("\tstr\tr" + register + ", [r13, #4] ; Stacking the arg\n");
                appendToBuffer("\tldmfd\tr13!, {r" + register + "} ; Freeing memory stack\n");
            } else {
                appendToBuffer("\tstmfd\tr13!, {r" + register + "} ; Stacking the arg\n");
                stackFrames.peek().getRegisterManager().freeRegister(register);
            }
        }
    }

    public void expressionGen(GraphViz ast, Integer nodeInt, TDS tds, int returnRegister) {
        if(codeGenOn){
            Node node = ast.getTree().nodes.get(nodeInt);
            String type = node.getLabel();
            try {
                int number = Integer.parseInt(type);
                if(number > 256){
                    appendToBuffer("\tldr\tr" + returnRegister + ", =" + number + " ; Generating number for expression\n");
                } else {
                    appendToBuffer("\tmov\tr" + returnRegister + ", #" + number + " ; Generating number for expression\n");
                }
                return;
            } catch (NumberFormatException e) {
                // Not a number so we continue
            }

            int register1;
            boolean isR1Borrowed = false;

            switch (type) {
                case "*" :
                    thereIsMult = true;
                    expressionGen(ast, node.getChildren().get(1), tds, returnRegister);
                    try { // If no register available, we use memory stack
                        register1 = stackFrames.peek().getRegisterManager().borrowRegister();
                    } catch (RuntimeException e) {
                        if (returnRegister != 0) {
                            register1 = 0;
                        }
                        else {
                            register1 = 1;
                        }
                        appendToBuffer("\tstmfd\tr13!, {r" + register1 + "} ; No more register available, making space with memory stack\n");
                        isR1Borrowed = true;
                    }
                    expressionGen(ast, node.getChildren().get(0), tds, register1);
                    appendToBuffer("\n\tstmfd\tr13!, {r" + returnRegister + ",r" + register1 + "} ; Block for multiplication : " + ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + " * " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + "\n\tsub\tr13, r13, #4\n\tbl\tmul\n\tldr r" + returnRegister + ", [r13]\n\tadd\tr13, r13, #4*3 ; 2 paramètres et 1 valeur de retour\n\n");
                    break;
                case "/" :
                    thereIsDiv = true;
                    expressionGen(ast, node.getChildren().get(1), tds, returnRegister);
                    try { // If no register available, we use memory stack
                        register1 = stackFrames.peek().getRegisterManager().borrowRegister();
                    } catch (RuntimeException e) {
                        if (returnRegister != 0) {
                            register1 = 0;
                        }
                        else {
                            register1 = 1;
                        }
                        appendToBuffer("\tstmfd\tr13!, {r" + register1 + "} ; No more register available, making space with memory stack\n");
                        isR1Borrowed = true;
                    }
                    expressionGen(ast, node.getChildren().get(0), tds, register1);
                    appendToBuffer("\tstmfd\tr13!, {r" + returnRegister + "} ; Block for division : " + ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + " / " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + "\n\tstmfd\tr13!, {r" + register1 + "}\n\tsub\tr13, r13, #4\n\tbl\tdiv\n\tldr r" + returnRegister + ", [r13]\n\tadd\tr13, r13, #4*3 ; 2 paramètres et 1 valeur de retour\n\n");
                    break;
                case "+" :
                    expressionGen(ast, node.getChildren().get(0), tds, returnRegister);
                    try { // If no register available, we use memory stack
                        register1 = stackFrames.peek().getRegisterManager().borrowRegister();
                    } catch (RuntimeException e) {
                        if (returnRegister != 0) {
                            register1 = 0;
                        }
                        else {
                            register1 = 1;
                        }
                        appendToBuffer("\tstmfd\tr13!, {r" + register1 + "} ; No more register available, making space with memory stack\n");
                        isR1Borrowed = true;
                    }
                    expressionGen(ast, node.getChildren().get(1), tds, register1);
                    appendToBuffer("\tadd\tr" + returnRegister + ", r" + returnRegister + ", r" + register1 + " ; Block for addition : " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + " + " + ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + "\n\n");
                    break;
                case "-" :
                    expressionGen(ast, node.getChildren().get(1), tds, returnRegister);
                    try { // If no register available, we use memory stack
                        register1 = stackFrames.peek().getRegisterManager().borrowRegister();
                    } catch (RuntimeException e) {
                        if (returnRegister != 0) {
                            register1 = 0;
                        }
                        else {
                            register1 = 1;
                        }
                        appendToBuffer("\tstmfd\tr13!, {r" + register1 + "} ; No more register available, making space with memory stack\n");
                        isR1Borrowed = true;
                    }
                    expressionGen(ast, node.getChildren().get(0), tds, register1);
                    appendToBuffer("\tsub\tr" + returnRegister + ", r" + returnRegister + ", r" + register1 + " ; Block for substraction : " + ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + " - " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + "\n\n");
                    break;
                default: // Variable à aller chercher
                    appendToBuffer("\t; Unhandeled expression (for the moment) : " + type + "\n");
//                    throw new RuntimeException("Unhandeled expression : " + type);
                    return;
            }
            if (isR1Borrowed){
                appendToBuffer("\tldmfd\tr13!, {r" + register1 + "} ; Freeing memory stack\n");
            } else {
                stackFrames.peek().getRegisterManager().freeRegister(register1);
            }
        }
    }

    public void callGen(Symbol symbol, int region, List<String> argsT) {
        if (codeGenOn) {
            String name = symbol.getName() + region;
            if (Objects.equals(name, "put0")) {
               appendToBuffer("\t; CALL put (not yet implemented)\n");
            } else {
                for (int i = 0; i < argsT.size(); i++) {
                    appendToBuffer("\tsub\tr13, r13, #"+ TDS.offsets.get(argsT.get(i)) +" ; " + name + " param " + (i + 1) + " init\n");
                    appendToBuffer("\tstr\tr" + argsT.get(i) + ", [r13]\n");
                }
                if (symbol instanceof Func) {
                    appendToBuffer("\tsub\tr13, r13, #" + TDS.offsets.get(((Func )symbol).getReturnType()) + " ; " + name + " return val init\n");
                }
                appendToBuffer("\tbl\t" + name + " ; CALL\n");
            }
            appendToBuffer("\t; End of call\n\n");
        }
    }
}