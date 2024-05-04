package asm;

import ast.GraphViz;
import ast.Node;
import tds.*;
import tds.Record;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class CodeGenerator {
    private final FileWriter fileWriter;
    public Stack<StackFrame> stackFrames; // TO CHANGE
    private Stack<String> asmStack;
    private Boolean codeGenOn = true;
    private TDS tds;
    private List<String> callableElements = new ArrayList<>();
    private final Stack<HashMap<Symbol, Integer>> initVars;
    private Stack<Integer> stack;
    private boolean isVarGen = false;

    public CodeGenerator(String fileName, boolean codeGenOn, TDS tds, Stack<Integer> stack) {
        this.codeGenOn = codeGenOn;
        if (codeGenOn) {
            try {
                this.fileWriter = new FileWriter(fileName + "-output.s");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.stackFrames = new Stack<>();
            this.asmStack = new Stack<>();
        } else {
            this.fileWriter = null;
        }
        this.tds = tds;
        this.initVars = new Stack<>();
        this.stack = stack;
    }

    public CodeGenerator() {
        // Fake constructor for tests
        this.codeGenOn = false;
        this.fileWriter = null;
        this.stackFrames = null;
        this.asmStack = null;
        this.tds = null;
        this.initVars = null;
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
            initVars.push(new HashMap<>()); // -1 if default value (i.e. 0) else expression node
            callableElements.add(name);
            String label = name + callableElements.lastIndexOf(name) + "global";
            if(fatherName == null){
                appendToBuffer("\tldr\tr10, =" + label + "\n\tldr\tr12, [r10]\n\tmov\tr10, r12\n\t;PARAMETERS\n");
                startBufferAppend("\t" + label + "\tDCD\t0xFF000004\n");
                return;
            }
            String labelParent = fatherName + callableElements.lastIndexOf(fatherName) + "global";
            appendToBuffer("\tldr\tr10, =" + label + "\n\tldr\tr10, [r10]\n\tstmfd\tr13!, {r10}\n\tldr\tr10, ="+ labelParent +"\n\tldr\tr10, [r10]\n\tstmfd\tr13!, {r10}\n\tmov\tr12, r13\n\tldr\tr10, =" + label + "\n\tstr\tr13, [r10]\n\tstmfd\tr13!, {r11}\n\tmov\tr11, r13\n");
            appendToBuffer("\t;PARAMETERS\n");
            startBufferAppend("\t" + label + "\tDCD\t0xFFFFFFFF\n");
            endBufferAppend("\tmov\tr13, r11\n\tldmfd\tr13!, {r11}\n\tadd\tr13, r13, #4\n\tldr\tr10, ="+label+"\n\tldmfd\tr13!, {r12}\n\tstr\tr12, [r10]\n");
        }
    }

    public void functionGen(String name, String last, String fatherName) {
        if (codeGenOn) {
            stackFrames.push(new StackFrame(name + last));
            initVars.push(new HashMap<>()); // -1 if default value (i.e. 0) else expression node
            callableElements.add(name);
            String label = name + callableElements.lastIndexOf(name) + "global";
            String labelParent = fatherName + callableElements.lastIndexOf(fatherName) + "global";
            appendToBuffer("\tldr\tr10, =" + label + "\n\tldr\tr10, [r10]\n\tstmfd\tr13!, {r10}\n\tldr\tr10, ="+ labelParent +"\n\tldr\tr10, [r10]\n\tstmfd\tr13!, {r10}\n\tmov\tr12, r13\n\tldr\tr10, =" + label + "\n\tstr\tr13, [r10]\n\tstmfd\tr13!, {r11}\n\tmov\tr11, r13\n");
            appendToBuffer("\t;PARAMETERS\n");
            startBufferAppend("\t" + label + "\tDCD\t0xFFFFFFFF\n");
            endBufferAppend("\tmov\tr13, r11\n\tldmfd\tr13!, {r11}\n\tadd\tr13, r13, #4\n\tldr\tr10, ="+label+"\n\tldmfd\tr13!, {r12}\n\tstr\tr12, [r10]\n");
        }
    }

    public void varGen(GraphViz ast, List<Symbol> symbolsOfRegion) {
        if (codeGenOn) {
            stackFrames.peek().switchVarBuffer();
            isVarGen = true;
            this.appendToBuffer("\t;VARIABLES\n");
            int offset;
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

            // init vars
            for(Map.Entry<Symbol, Integer> entry : initVars.pop().entrySet()) {
                Symbol symbol = entry.getKey();
                int value = entry.getValue();
                if (symbol instanceof Var) {
                    offset = ((Var) symbol).getOffset();
                    if (value == -1) {
                        this.appendToBuffer("\tmov\tr10, #0\n\tstr\tr10, [r13, #"+(lastOffset - offset) +"]");
                    } else {
                        expressionGen(ast, value, 10);
                        this.appendToBuffer("\tstr\tr10, [r13, #"+(lastOffset - offset) +"]");
                    }
                    this.appendToBuffer("\t; Init " + symbol.getName() + "\n");
                }
            }
            isVarGen = false;
        }
    }

    public void appendToBuffer(String s) {
        if (codeGenOn) {
            if (!stackFrames.isEmpty()) {
                if(isVarGen) {
                    stackFrames.peek().getVarBuffer().append(s);
                } else {
                    stackFrames.peek().getBuffer().append(s);
                }
            }
        }
    }

    public void endBufferAppend(String s){
        if(codeGenOn) {
            if (!stackFrames.isEmpty()) {
                stackFrames.peek().getEndBuffer().append(s);
            }
        }
    }

    public void startBufferAppend(String s){
        if(codeGenOn) {
            if (!stackFrames.isEmpty()) {
                stackFrames.firstElement().getStartBuffer().append(s);
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

            // write the content of the asmStack
            while (!asmStack.isEmpty()) {
                this.write(asmStack.pop());
            }

            // copy the content of the its.s file at the end of the output file
            try {
                List<String> lines = Files.readAllLines(Paths.get("src/asm/visual/its.s"));
                for (String line : lines) {
                    this.write(line + "\n");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            this.write("mul\tstmfd\tr13!, {r0-r2, r11, lr} ; This is PreWritten Code for multiplication\n\tmov\tr11, r13\n\tldr\tr1, [r11, #4*6]\n\tldr\tr2, [r11, #4*7]\n\tmov\tr0, #0\nmul_loop\tlsrs\tr2,r2,#1\n\taddcs\tr0,r0,r1\n\tlsl\tr1,r1,#1\n\ttst\tr2,r2\n\tbne\tmul_loop\n\tstr\tr0, [r11, #4*5]\n\tmov\tr13, r11\n\tldmfd\tr13!,{r0-r2, r11, pc}\n\n");
            this.write("div\tstmfd\tsp!, {r0-r5, r11, lr} ; This is PreWritten Code for division\n\tmov\tr11, r13\n\tldr\tr1, [r11, #4*9]\n\tldr\tr2, [r11, #4*10]\n\tmov\tr0,#0\n\tmov\tr3,#0\n\tcmp\tr1, #0\n\trsblt\tr1, r1,#0\n\teorlt\tr3, r3, #1\n\tcmp\tr2, #0\n\trsblt\tr2, r2,#0\n\teorlt\tr3, r3, #1\n\tmov\tr4, r2\n\tmov\tr5, #1\ndiv_max\tlsl\tr4, r4, #1\n\tlsl\tr5,r5,#1\n\tcmp\tr4, r1\n\tble\tdiv_max\ndiv_loop\tlsr\tr4, r4, #1\n\tlsr\tr5, r5, #1\n\tcmp\tr4, r1\n\tbgt\tdiv_loop\n\tadd\tr0, r0, r5\n\tsub\tr1, r1, r4\n\tcmp\tr1, r2\n\tbge\tdiv_loop\n\tcmp\tr3, #1\n\tbne\tdiv_exit\n\tcmp\tr1,#0\n\taddne\tr0, r0,#1\n\trsb\tr0, r0, #0\n\trsb\tr1, r1, #0\n\taddne\tr1,r1,r2\ndiv_exit\tstr\tr0, [r11, #4*8]\n\tldmfd\tr13!, {r0-r5, r11, pc}\n\n");

            try {
                assert fileWriter != null;
                fileWriter.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void assignationGen(GraphViz ast, Node node) {
        int exprRegister = 0;
        boolean isRegisterBorrowed = false;
        try {
            exprRegister = stackFrames.peek().getRegisterManager().borrowRegister();
        } catch (RuntimeException e) {
            exprRegister = 0;
            isRegisterBorrowed = true;
            appendToBuffer("\tstmfd\tr13!, {r" + exprRegister + "} ; No more register available, making space with memory stack\n");
        }
        expressionGen(ast, node.getChildren().get(1), exprRegister);
        // Get the var to assign
        Node node1 = ast.getTree().nodes.get(node.getChildren().get(0));
        String type = node1.getLabel();
        int addressReg;
        boolean isRegisterAddressBorrowed = false;
        try {
            addressReg = stackFrames.peek().getRegisterManager().borrowRegister();
        } catch (RuntimeException e) {
            if (exprRegister != 0) {
                addressReg = 0;
            }
            else {
                addressReg = 1;
            }
            isRegisterAddressBorrowed = true;
            appendToBuffer("\tstmfd\tr13!, {r" + addressReg + "} ; No more register available, making space with memory stack\n");
        }
        // Set the value
        getVarAddress(type, addressReg);
        appendToBuffer("\tstr\tr" + exprRegister + ", [r" + addressReg + "] ; Assigning value to var : " + type + "\n");

        if (isRegisterAddressBorrowed) {
            appendToBuffer("\tldmfd\tr13!, {r" + addressReg + "} ; Freeing memory stack\n"); // Free the register
        } else {
            stackFrames.peek().getRegisterManager().freeRegister(addressReg);
        }
        if(isRegisterBorrowed) {
            appendToBuffer("\tldmfd\tr13!, {r" + exprRegister + "} ; Freeing memory stack\n"); // Free the register
        } else {
            stackFrames.peek().getRegisterManager().freeRegister(exprRegister);
        }
    }

    public void stackArg(GraphViz ast, Integer nodeInt) {
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
            expressionGen(ast, nodeInt, register);
            if (isRegisterBorrowed) {
                appendToBuffer("\tstr\tr" + register + ", [r13, #4] ; Stacking the arg\n");
                appendToBuffer("\tldmfd\tr13!, {r" + register + "} ; Freeing memory stack\n");
            } else {
                appendToBuffer("\tstmfd\tr13!, {r" + register + "} ; Stacking the arg\n");
                stackFrames.peek().getRegisterManager().freeRegister(register);
            }
        }
    }

    public void expressionGen(GraphViz ast, Integer nodeInt, int returnRegister) {
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

            // check if it's a character
            if (type.charAt(0) == '\'') {
                appendToBuffer("\tmov\tr" + returnRegister + ", #" + (int) type.charAt(1) + " ; Generating character for expression\n");
                return;
            }

            int register1;
            boolean isR1Borrowed = false;

            switch (type) {
                case "*" :
                    expressionGen(ast, node.getChildren().get(1), returnRegister);
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
                    expressionGen(ast, node.getChildren().get(0), register1);
                    appendToBuffer("\n\tstmfd\tr13!, {r" + returnRegister + ",r" + register1 + "} ; Block for multiplication : " + ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + " * " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + "\n\tsub\tr13, r13, #4\n\tbl\tmul\n\tldr r" + returnRegister + ", [r13]\n\tadd\tr13, r13, #4*3 ; 2 paramètres et 1 valeur de retour\n\n");
                    break;
                case "/" :
                    expressionGen(ast, node.getChildren().get(1), returnRegister);
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
                    expressionGen(ast, node.getChildren().get(0), register1);
                    appendToBuffer("\tstmfd\tr13!, {r" + returnRegister + "} ; Block for division : " + ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + " / " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + "\n\tstmfd\tr13!, {r" + register1 + "}\n\tsub\tr13, r13, #4\n\tbl\tdiv\n\tldr r" + returnRegister + ", [r13]\n\tadd\tr13, r13, #4*3 ; 2 paramètres et 1 valeur de retour\n\n");
                    break;
                case "+" :
                    expressionGen(ast, node.getChildren().get(0), returnRegister);
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
                    expressionGen(ast, node.getChildren().get(1), register1);
                    appendToBuffer("\tadd\tr" + returnRegister + ", r" + returnRegister + ", r" + register1 + " ; Block for addition : " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + " + " + ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + "\n\n");
                    break;
                case "-" :
                    expressionGen(ast, node.getChildren().get(1), returnRegister);
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
                    expressionGen(ast, node.getChildren().get(0), register1);
                    appendToBuffer("\tsub\tr" + returnRegister + ", r" + returnRegister + ", r" + register1 + " ; Block for substraction : " + ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + " - " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + "\n\n");
                    break;
                default: // Variable à aller chercher
                    if (type != "CALL") {
                        getVar(type, returnRegister);
                    }
//                    appendToBuffer("\t; Unhandeled expression (for the moment) : " + type + "\n");
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

    public void callGen(Symbol symbol, int region) {
        if (codeGenOn) {
            String name = symbol.getName() + region;
            if (Objects.equals(name, "put0")) {
                switch (((Proc) symbol).getTypes().get(0)) {
                    case "integer", "boolean":
                        appendToBuffer("\tbl\tprintln_int ; CALL put(int n)\n");
                        break;
                    case "character":
                        appendToBuffer("\tbl\tprintln_char ; CALL put(char c)\n");
                        break;
                }
            } else {
                appendToBuffer("\tbl\t" + name + " ; CALL\n");
            }
            appendToBuffer("\t; End of call\n\n");
        }
    }

    public void getVar(String name, int returnRegister) {
        if (codeGenOn) {
            getVarAddress(name, returnRegister);
            appendToBuffer("\tldr\tr" + returnRegister + ", [r" + returnRegister + "] ; Getting value of var : " + name + "\n");
        }
    }

    public void getVarAddress(String name, int returnRegister) {
        if (codeGenOn) {
            int destinationRegion = getRegionFromLabel(name, stack.peek());
            if (destinationRegion == -1) {
                throw new RuntimeException("Variable not found : " + name);
            }
            int start = tds.getTds().get(stack.peek()).get(0).getNestingLevel();
            int end = tds.getTds().get(destinationRegion).get(0).getNestingLevel();
            int linkingsToGoUp;
            linkingsToGoUp = start - end;
            Symbol symbol = getSymbolFromLabel(name, destinationRegion);
            int offset;
            if (symbol instanceof Var) {
                Var var = (Var) symbol;
                offset =  var.getOffset();
                appendToBuffer("\tmov\tr10, r12\n");
                for (int i = 0; i < linkingsToGoUp; i++) {
                    appendToBuffer("\tldr\tr10, [r10] ; Going up in the static linkings\n");
                }
                appendToBuffer("\tadd\tr10, r10, #-4-" + offset + " ; Getting address of var : " + name + "\n\tmov\tr" + returnRegister + ", r10\n");
            } else if (symbol instanceof Param) {
                Param param = (Param) symbol;
                offset = param.getOffset();
                appendToBuffer("\tmov\tr10, r12\n");
                for (int i = 0; i < linkingsToGoUp; i++) {
                    appendToBuffer("\tldr\tr10, [r10]\n");
                }
                appendToBuffer("\tadd\tr10, r10, #4*");
                stackFrames.peek().needRegisterValue();
                appendToBuffer("+" + offset + "+16 ; Getting param : " + name + "\n\tmov\tr" + returnRegister + ", r10\n");
            }
//            else if (symbol instanceof Record) {
//                Record record = (Record) symbol;
//                offset = record.getOffset();
//                appendToBuffer("\tmov\tr10, r12\n");
//                for (int i = 0; i < linkingsToGoUp; i++) {
//                    appendToBuffer("\tldr\tr10, [r10]\n");
//                }
//                appendToBuffer("\tldr\tr" + returnRegister + ", [r10, #-4-" + offset + "] ; Getting Record : " + name + "\n");
//            }
            else {
                throw new RuntimeException("Unhandeled getVar : " + symbol + " " + name + " " + symbol.getClass());
            }
        }
    }

    private int getRegionFromLabel(String label, int reg) {
        int father = 0;
        for (Symbol symbol : tds.getTds().get(reg)){
            father = symbol.getFather();
            if (symbol.getName().equals(label)) {
                return reg;
            }
        }
        if (reg != 0){
            return getRegionFromLabel(label, father);
        } else {
            return -1;
        }
    }

    private Symbol getSymbolFromLabel(String label, int reg) {
        int father = 0;
        for (Symbol symbol : tds.getTds().get(reg)){
            father = symbol.getFather();
            if (symbol.getName().equals(label)) {
                return symbol;
            }
        }
        if (reg != 0){
            return getSymbolFromLabel(label, father);
        } else {
            return null;
        }
    }

    public void stackReturn(Symbol symbol, int region) {
        if (codeGenOn) {
            String name = symbol.getName() + region;
            if (symbol instanceof Func) {
                appendToBuffer("\tsub\tr13, r13, #" + TDS.offsets.get(((Func )symbol).getReturnType()) + " ; " + name + " return val init\n");
            }
        }
    }

    public void addInitVar(Symbol symbol, int value) {
        if (codeGenOn) {
            initVars.peek().put(symbol, value);
        }
    }
}