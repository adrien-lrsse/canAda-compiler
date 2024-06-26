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
    private boolean isVarGen;
    private Stack<Boolean> newFunc;
    private Stack<Integer> paramSize;
    private Stack<Integer> returnSize;

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
        this.isVarGen = false;
        this.newFunc = new Stack<>();
        this.paramSize = new Stack<>();
        this.returnSize = new Stack<>();
    }

    public CodeGenerator() {
        // Fake constructor for tests
        this.codeGenOn = false;
        this.fileWriter = null;
        this.stackFrames = new Stack<>();
        this.stackFrames.push(new StackFrame("fake"));
        this.asmStack = null;
        this.tds = null;
        this.initVars = null;
        this.paramSize = new Stack<>();
        this.returnSize = new Stack<>();
    }

    public void write(String s) {
        try {
            fileWriter.write(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addNewFunc(boolean func) {
        if (codeGenOn) {
            this.newFunc.push(func);
        }
    }

    public void setNewFunc(boolean func) {
        if (codeGenOn) {
            this.newFunc.pop();
            this.newFunc.push(func);
        }
    }

    public void removeNewFunc() {
        if (codeGenOn) {
            this.newFunc.pop();
        }
    }

    public void procedureGen(String name, String last, String fatherName) {
        if (codeGenOn) {
            stackFrames.push(new StackFrame(name + last));
            initVars.push(new HashMap<>()); // -1 if default value (i.e. 0) else expression node
            callableElements.add(name);
            String label = name + callableElements.lastIndexOf(name) + "global";
            if (fatherName == null) {
                appendToBuffer("\tldr\tr10, =" + label + "\n\tldr\tr12, [r10]\n\tmov\tr10, r12\n\t;PARAMETERS\n");
                startBufferAppend("\t" + label + "\tDCD\t0xFF000004\n");
                endBufferAppend("end" + name + callableElements.lastIndexOf(name));
                return;
            }
            String labelParent = fatherName + callableElements.lastIndexOf(fatherName) + "global";
            appendToBuffer("\tldr\tr10, =" + label + "\n\tldr\tr10, [r10]\n\tstmfd\tr13!, {r10}\n\tldr\tr10, =" + labelParent + "\n\tldr\tr10, [r10]\n\tstmfd\tr13!, {r10}\n\tmov\tr12, r13\n\tldr\tr10, =" + label + "\n\tstr\tr13, [r10]\n\tstmfd\tr13!, {r11}\n\tmov\tr11, r13\n");
            appendToBuffer("\t;PARAMETERS\n");
            startBufferAppend("\t" + label + "\tDCD\t0xFFFFFFFF\n");
            endBufferAppend("end" + name + callableElements.lastIndexOf(name) + "\tmov\tr13, r11\n\tldmfd\tr13!, {r11}\n\tadd\tr13, r13, #4\n\tldr\tr10, =" + label + "\n\tldmfd\tr13!, {r12}\n\tstr\tr12, [r10]\n");
        }
    }

    public void functionGen(String name, String last, String fatherName) {
        if (codeGenOn) {
            stackFrames.push(new StackFrame(name + last));
            initVars.push(new HashMap<>()); // -1 if default value (i.e. 0) else expression node
            callableElements.add(name);
            String label = name + callableElements.lastIndexOf(name) + "global";
            String labelParent = fatherName + callableElements.lastIndexOf(fatherName) + "global";
            appendToBuffer("\tldr\tr10, =" + label + "\n\tldr\tr10, [r10]\n\tstmfd\tr13!, {r10}\n\tldr\tr10, =" + labelParent + "\n\tldr\tr10, [r10]\n\tstmfd\tr13!, {r10}\n\tmov\tr12, r13\n\tldr\tr10, =" + label + "\n\tstr\tr13, [r10]\n\tstmfd\tr13!, {r11}\n\tmov\tr11, r13\n");
            appendToBuffer("\t;PARAMETERS\n");
            startBufferAppend("\t" + label + "\tDCD\t0xFFFFFFFF\n");
            endBufferAppend("end" + name + callableElements.lastIndexOf(name) + "\tmov\tr13, r11\n\tldmfd\tr13!, {r11}\n\tadd\tr13, r13, #4\n\tldr\tr10, =" + label + "\n\tldmfd\tr13!, {r12}\n\tstr\tr12, [r10]\n");
        }
    }

    public void switchForVar() {
        if (codeGenOn) {
            stackFrames.peek().switchVarBuffer();
        }
    }

    public void varGen(GraphViz ast, List<Symbol> symbolsOfRegion, int lastOffset) {
        if (codeGenOn) {
            isVarGen = true;
            this.stackFrames.peek().setIsVarGen(true);
            this.appendToBuffer("\t;VARIABLES\n");
            int offset;

            for (Symbol symbol : symbolsOfRegion) {
                if (symbol instanceof Var) {
                    this.appendToBuffer("\t\t;" + ((Var) symbol).getType() + "\t" + symbol.getName() + "\n");
                }
            }
            if (lastOffset != -1) {
                this.appendToBuffer("\tsub\tr13, r13, #" + lastOffset + "\n");
            }

            // init vars
            for (Map.Entry<Symbol, Integer> entry : initVars.pop().entrySet()) {
                Symbol symbol = entry.getKey();
                int value = entry.getValue();
                if (symbol instanceof Var) {
                    offset = ((Var) symbol).getOffset();
                    if (value == -1) {
                        this.appendToBuffer("\tmov\tr10, #0\n\tstr\tr10, [r13, #" + (lastOffset - offset) + "]");
                    } else {
                        int exprRegister = 0;
                        boolean isRegisterBorrowed = false;
                        try {
                            exprRegister = this.stackFrames.peek().getRegisterManager().borrowRegister();
                        } catch (RuntimeException e) {
                            exprRegister = 0;
                            this.appendToBuffer("\tstmfd\tr13!, {r" + exprRegister + "} ; No more register available, making space with memory stack\n");
                        }
                        expressionGen(ast, value, exprRegister);
                        this.appendToBuffer("\tstr\tr"+exprRegister+", [r13, #" + (lastOffset - offset) + "]");
                        if (isRegisterBorrowed) {
                            this.appendToBuffer("\tldmfd\tr13!, {r" + exprRegister + "} ; Freeing memory stack\n");
                        } else {
                            this.stackFrames.peek().getRegisterManager().freeRegister(exprRegister);
                        }
                    }
                    this.appendToBuffer("\t; Init " + symbol.getName() + "\n");
                }
            }
            isVarGen = false;
            this.stackFrames.peek().setIsVarGen(false);
        }
    }

    public void appendToBuffer(String s) {
        if (codeGenOn) {
            if (!stackFrames.isEmpty()) {
                if (isVarGen) {
                    stackFrames.peek().getVarBuffer().append(s);
                } else {
                    stackFrames.peek().getBuffer().append(s);
                }
            }
        }
    }

    public void endBufferAppend(String s) {
        if (codeGenOn) {
            if (!stackFrames.isEmpty()) {
                stackFrames.peek().getEndBuffer().append(s);
            }
        }
    }

    public void startBufferAppend(String s) {
        if (codeGenOn) {
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
            this.write("rem\tstmfd\tsp!, {r0-r5, r11, lr} ; This is PreWritten Code for remainder\n\tmov\tr11, r13\n\tldr\tr1, [r11, #4*9]\n\tldr\tr2, [r11, #4*10]\n\tcmp\tr2, #0\n\tbeq div_err\n\tmov\tr0,#0\n\tmov\tr3,#0\n\tcmp\tr1, #0\n\trsblt\tr1, r1,#0\n\teorlt\tr3, r3, #1\n\tcmp\tr2, #0\n\trsblt\tr2, r2,#0\n\teorlt\tr3, r3, #1\n\tmov\tr4, r2\n\tmov\tr5, #1\nrem_max\tlsl\tr4, r4, #1\n\tlsl\tr5,r5,#1\n\tcmp\tr4, r1\n\tble\trem_max\nrem_loop\tlsr\tr4, r4, #1\n\tlsr\tr5, r5, #1\n\tcmp\tr4, r1\n\tbgt\trem_loop\n\tadd\tr0, r0, r5\n\tsub\tr1, r1, r4\n\tcmp\tr1, r2\n\tbge\trem_loop\n\tcmp\tr3, #1\n\tbne\trem_exit\n\tcmp\tr1,#0\n\taddne\tr0, r0,#1\n\trsb\tr0, r0, #0\n\trsb\tr1, r1, #0\n\taddne\tr1,r1,r2\nrem_exit\tcmp\tr0, #0\n\tldreq\tr1, [r11, #4*9]\n\tstr\tr1, [r11, #4*8]\n\tldmfd\tr13!, {r0-r5, r11, pc}\n\n");
            this.write("div\tstmfd\tsp!, {r0-r5, r11, lr} ; This is PreWritten Code for division\n\tmov\tr11, r13\n\tldr\tr1, [r11, #4*9]\n\tldr\tr2, [r11, #4*10]\n\tcmp\tr2, #0\n\tbeq div_err\n\tmov\tr0,#0\n\tmov\tr3,#0\n\tcmp\tr1, #0\n\trsblt\tr1, r1,#0\n\teorlt\tr3, r3, #1\n\tcmp\tr2, #0\n\trsblt\tr2, r2,#0\n\teorlt\tr3, r3, #1\n\tmov\tr4, r2\n\tmov\tr5, #1\ndiv_max\tlsl\tr4, r4, #1\n\tlsl\tr5,r5,#1\n\tcmp\tr4, r1\n\tble\tdiv_max\ndiv_loop\tlsr\tr4, r4, #1\n\tlsr\tr5, r5, #1\n\tcmp\tr4, r1\n\tbgt\tdiv_loop\n\tadd\tr0, r0, r5\n\tsub\tr1, r1, r4\n\tcmp\tr1, r2\n\tbge\tdiv_loop\n\tcmp\tr3, #1\n\tbne\tdiv_exit\n\tcmp\tr1,#0\n\taddne\tr0, r0,#1\n\trsb\tr0, r0, #0\n\trsb\tr1, r1, #0\n\taddne\tr1,r1,r2\ndiv_exit\tstr\tr0, [r11, #4*8]\n\tldmfd\tr13!, {r0-r5, r11, pc}\n\n");
            this.write("reversed_for_err\nstr_reversed_for_err\tdcd\t0x6F727245, 0x49203A72, 0x6E69666E, 0x20657469, 0x65766572, 0x64657372, 0x726F6620, 0x6F6F6C20, 0x70\n\tldr\tr0, =str_reversed_for_err\n\tstmfd\tr13!, {r0}\n\tbl\traise_err\n\tend\ndiv_err\nstr_div_by_zero\tdcd\t0x6F727245, 0x64203A72, 0x73697669, 0x206E6F69, 0x7A207962, 0x6F7265\n\tldr\tr0, =str_div_by_zero\n\tstmfd\tr13!, {r0}\n\tbl\traise_err\n\tend\nfor_err\nstr_for_err\tdcd\t0x6F727245, 0x49203A72, 0x6E69666E, 0x20657469, 0x20726F66, 0x706F6F6C, 0x00\n\tldr\tr0, =str_for_err\n\tstmfd\tr13!, {r0}\n\tbl\traise_err\n\tend\nchar_val_err\nstr_char_val_err\tdcd\t0x6F727245, 0x43203A72, 0x61726168, 0x72657463, 0x6C615627, 0x746F6E20, 0x74656220, 0x6E656577, 0x61203020, 0x3120646E, 0x3732\n\tldr\tr0, =str_char_val_err\n\tstmfd\tr13!, {r0}\n\tbl\traise_err\n\tend\n");

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
            } else {
                addressReg = 1;
            }
            isRegisterAddressBorrowed = true;
            appendToBuffer("\tstmfd\tr13!, {r" + addressReg + "} ; No more register available, making space with memory stack\n");
        }
        // Set the value
        // access record case
        List<String> fields = new ArrayList<>();
        fields.add(type);
        Node nodeAccessIdent, nodeField;
        while (!node1.getChildren().isEmpty()) {
            nodeAccessIdent = ast.getTree().nodes.get(node1.getChildren().get(0));
            nodeField = ast.getTree().nodes.get(nodeAccessIdent.getChildren().get(0));
            fields.add(nodeField.getLabel());
            node1 = nodeField;
        }
        type = String.join(".", fields);

        Record record = getVarAddress(type, addressReg);
        if (record != null) { // several fields need to be accessed
            Symbol symbol = getSymbolFromLabel(ast.getTree().nodes.get(node.getChildren().get(1)).getLabel(), stack.peek());
            if (symbol instanceof Param) {
                copyStructs(record, addressReg, exprRegister, 0, true);
            } else {
                copyStructs(record, addressReg, exprRegister, 0, false);
            }
        } else {
            appendToBuffer("\tstr\tr" + exprRegister + ", [r" + addressReg + "] ; Assigning value to var : " + type + "\n");
        }

        if (isRegisterAddressBorrowed) {
            appendToBuffer("\tldmfd\tr13!, {r" + addressReg + "} ; Freeing memory stack\n"); // Free the register
        } else {
            stackFrames.peek().getRegisterManager().freeRegister(addressReg);
        }
        if (isRegisterBorrowed) {
            appendToBuffer("\tldmfd\tr13!, {r" + exprRegister + "} ; Freeing memory stack\n"); // Free the register
        } else {
            stackFrames.peek().getRegisterManager().freeRegister(exprRegister);
        }
    }

    public void stackArg(GraphViz ast, Integer nodeInt, boolean isFunc) {
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
            int offset;
            if ((offset = expressionGen(ast, nodeInt, register)) != 0) {
                for (int o = 0; o < offset; o += 4) {
                    if (isRegisterBorrowed) {
                        appendToBuffer("\tldr\tr10, [r13, #" + o + "] ; Getting value of var\n");
                        appendToBuffer("\tstmfd\tr13!, {r10} ; Stacking the arg\n");
                    } else {
                        appendToBuffer("\tldr\tr10, [r" + register + ", #" + o + "] ; Getting value of var\n");
                        appendToBuffer("\tstmfd\tr13!, {r10} ; Stacking the arg\n");
                    }
                }

                if (isFunc) {
                    if (newFunc.peek()) {
                        paramSize.push(0);
                    }
                    paramSize.push((paramSize.pop() + offset));
                }

                if (isRegisterBorrowed) {
                    appendToBuffer("\tldmfd\tr13!, {r" + register + "} ; Freeing memory stack\n");
                } else {
                    stackFrames.peek().getRegisterManager().freeRegister(register);
                }
                return;
            }

            if (isRegisterBorrowed) {
                appendToBuffer("\tstr\tr" + register + ", [r13, #4] ; Stacking the arg\n");
                appendToBuffer("\tldmfd\tr13!, {r" + register + "} ; Freeing memory stack\n");
                if (isFunc) {
                    if (newFunc.peek()) {
                        paramSize.push(0);
                    }
                    paramSize.push((paramSize.pop() + 4));
                }
            } else {
                appendToBuffer("\tstmfd\tr13!, {r" + register + "} ; Stacking the arg\n");
                if (isFunc) {
                    if (newFunc.peek()) {
                        paramSize.push(0);
                    }
                    paramSize.push((paramSize.pop() + 4));
                }
                stackFrames.peek().getRegisterManager().freeRegister(register);
            }
        }
    }

    public void forCheckEnd(GraphViz ast, String var, int nodeVal, boolean reversed) {
        if(codeGenOn) {
            int exprRegister = 0;
            boolean isRegisterBorrowed = false;
            try {
                exprRegister = stackFrames.peek().getRegisterManager().borrowRegister();
            } catch (RuntimeException e) {
                exprRegister = 0;
                isRegisterBorrowed = true;
                appendToBuffer("\tstmfd\tr13!, {r" + exprRegister + "} ; No more register available, making space with memory stack\n");
            }

            getVarAddress(var, exprRegister);
            appendToBuffer("\tldr r10, [r" + exprRegister + "] ; Getting value of for var\n");
            appendToBuffer("\tldr r"+exprRegister+", [r" + exprRegister + ", #4] ; Getting end value of for\n");
            appendToBuffer("\tcmp\tr10, r" + exprRegister + " ; Condition\n");

            if (isRegisterBorrowed) {
                appendToBuffer("\tldmfd\tr13!, {r" + exprRegister + "} ; Freeing memory stack\n"); // Free the register
            } else {
                stackFrames.peek().getRegisterManager().freeRegister(exprRegister);
            }
        }
    }

    public void forCheckError(GraphViz ast, String var, int nodeVal, boolean reversed) {
        if(codeGenOn) {
            int exprRegister = 0;
            boolean isRegisterBorrowed = false;
            try {
                exprRegister = stackFrames.peek().getRegisterManager().borrowRegister();
            } catch (RuntimeException e) {
                exprRegister = 0;
                isRegisterBorrowed = true;
                appendToBuffer("\tstmfd\tr13!, {r" + exprRegister + "} ; No more register available, making space with memory stack\n");
            }

            getVarAddress(var, exprRegister);
            appendToBuffer("\tldr r10, [r" + exprRegister + "] ; Getting value of for var\n");
            appendToBuffer("\tldr r"+exprRegister+", [r" + exprRegister + ", #4] ; Getting end value of for\n");
            appendToBuffer("\tcmp\tr10, r" + exprRegister + " ; Condition\n");

//            handling infinite loop
            if (reversed) {
                appendToBuffer("\tblt\treversed_for_err ; Error if the condition is never met\n");
            } else {
                appendToBuffer("\tbgt\tfor_err ; Error if the condition is never met\n");
            }

            if (isRegisterBorrowed) {
                appendToBuffer("\tldmfd\tr13!, {r" + exprRegister + "} ; Freeing memory stack\n"); // Free the register
            } else {
                stackFrames.peek().getRegisterManager().freeRegister(exprRegister);
            }
        }
    }

    public void forInitGen(GraphViz ast, List<Integer> children, String type, int begin, int end) {
        int exprRegister = 0;
        boolean isRegisterBorrowed = false;
        try {
            exprRegister = stackFrames.peek().getRegisterManager().borrowRegister();
        } catch (RuntimeException e) {
            exprRegister = 0;
            isRegisterBorrowed = true;
            appendToBuffer("\tstmfd\tr13!, {r" + exprRegister + "} ; No more register available, making space with memory stack\n");
        }
        expressionGen(ast, children.get(end), exprRegister);


        // Get the var to assign
        int addressReg;
        boolean isRegisterAddressBorrowed = false;
        try {
            addressReg = stackFrames.peek().getRegisterManager().borrowRegister();
        } catch (RuntimeException e) {
            if (exprRegister != 0) {
                addressReg = 0;
            } else {
                addressReg = 1;
            }
            isRegisterAddressBorrowed = true;
            appendToBuffer("\tstmfd\tr13!, {r" + addressReg + "} ; No more register available, making space with memory stack\n");
        }
        // Set the value
        getVarAddress(type, addressReg);

        appendToBuffer("\tstr\tr" + exprRegister + ", [r" + addressReg + ", #4] ; Assigning end value of for loop\n");

        expressionGen(ast, children.get(begin), exprRegister);
        appendToBuffer("\tstr\tr" + exprRegister + ", [r" + addressReg + "] ; Assigning begin value of for loop to var : " + type + "\n");

        if (isRegisterAddressBorrowed) {
            appendToBuffer("\tldmfd\tr13!, {r" + addressReg + "} ; Freeing memory stack\n"); // Free the register
        } else {
            stackFrames.peek().getRegisterManager().freeRegister(addressReg);
        }
        if (isRegisterBorrowed) {
            appendToBuffer("\tldmfd\tr13!, {r" + exprRegister + "} ; Freeing memory stack\n"); // Free the register
        } else {
            stackFrames.peek().getRegisterManager().freeRegister(exprRegister);
        }
    }

    public void forIncrement(String var, boolean reverse) {
        if(codeGenOn){
            int exprRegister = 0;
            boolean isRegisterBorrowed = false;
            try {
                exprRegister = stackFrames.peek().getRegisterManager().borrowRegister();
            } catch (RuntimeException e) {
                exprRegister = 0;
                isRegisterBorrowed = true;
                appendToBuffer("\tstmfd\tr13!, {r" + exprRegister + "} ; No more register available, making space with memory stack\n");
            }

            getVarAddress(var, exprRegister);

            if(reverse) {
                appendToBuffer("\tldr\tr10, [r"+exprRegister+"] ; getting var value\n\tsub\tr10, r10, #1\n\tstr\tr10, [r"+exprRegister+"] ; var = var - 1\n");
            } else {
                appendToBuffer("\tldr\tr10, [r"+exprRegister+"] ; getting var value\n\tadd\tr10, r10, #1\n\tstr\tr10, [r"+exprRegister+"] ; var = var + 1\n");
            }


            if (isRegisterBorrowed) {
                appendToBuffer("\tldmfd\tr13!, {r" + exprRegister + "} ; Freeing memory stack\n"); // Free the register
            } else {
                stackFrames.peek().getRegisterManager().freeRegister(exprRegister);
            }
        }
    }

    public int expressionGen(GraphViz ast, Integer nodeInt, int returnRegister) {
        if (codeGenOn) {
            Node node = ast.getTree().nodes.get(nodeInt);
            String type = node.getLabel();
            try {
                int number = Integer.parseInt(type);
                if (number > 256) {
                    appendToBuffer("\tldr\tr" + returnRegister + ", =" + number + " ; Generating number for expression\n");
                } else {
                    appendToBuffer("\tmov\tr" + returnRegister + ", #" + number + " ; Generating number for expression\n");
                }
                return 0;
            } catch (NumberFormatException e) {
                // Not a number so we continue
            }

            // check if it's a character
            if (type.charAt(0) == '\'') {
                appendToBuffer("\tmov\tr" + returnRegister + ", #" + (int) type.charAt(1) + " ; Generating character for expression\n");
                return 0;
            }

            // check if it's a boolean
            if (type.equals("true")) {
                appendToBuffer("\tmov\tr" + returnRegister + ", #1 ; Generating boolean for expression\n");
                return 0;
            } else if (type.equals("false")) {
                appendToBuffer("\tmov\tr" + returnRegister + ", #0 ; Generating boolean for expression\n");
                return 0;
            }

            int register1; // default value -1
            boolean isR1Borrowed = false;

            switch (type) {
                case "*":
                    expressionGen(ast, node.getChildren().get(1), returnRegister);
                    try { // If no register available, we use memory stack
                        register1 = stackFrames.peek().getRegisterManager().borrowRegister();
                    } catch (RuntimeException e) {
                        if (returnRegister != 0) {
                            register1 = 0;
                        } else {
                            register1 = 1;
                        }
                        appendToBuffer("\tstmfd\tr13!, {r" + register1 + "} ; No more register available, making space with memory stack\n");
                        isR1Borrowed = true;
                    }
                    expressionGen(ast, node.getChildren().get(0), register1);
                    appendToBuffer("\n\tstmfd\tr13!, {r" + returnRegister + ",r" + register1 + "} ; Block for multiplication : " + ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + " * " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + "\n\tsub\tr13, r13, #4\n\tbl\tmul\n\tldr r" + returnRegister + ", [r13]\n\tadd\tr13, r13, #4*3 ; 2 paramètres et 1 valeur de retour\n\n");
                    break;
                case "/":
                    expressionGen(ast, node.getChildren().get(1), returnRegister);
                    try { // If no register available, we use memory stack
                        register1 = stackFrames.peek().getRegisterManager().borrowRegister();
                    } catch (RuntimeException e) {
                        if (returnRegister != 0) {
                            register1 = 0;
                        } else {
                            register1 = 1;
                        }
                        appendToBuffer("\tstmfd\tr13!, {r" + register1 + "} ; No more register available, making space with memory stack\n");
                        isR1Borrowed = true;
                    }
                    expressionGen(ast, node.getChildren().get(0), register1);
                    appendToBuffer("\tstmfd\tr13!, {r" + returnRegister + "} ; Block for division : " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + " / " + ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + "\n\tstmfd\tr13!, {r" + register1 + "}\n\tsub\tr13, r13, #4\n\tbl\tdiv\n\tldr r" + returnRegister + ", [r13]\n\tadd\tr13, r13, #4*3 ; 2 paramètres et 1 valeur de retour\n\n");
                    break;
                case "rem":
                    expressionGen(ast, node.getChildren().get(1), returnRegister);
                    try { // If no register available, we use memory stack
                        register1 = stackFrames.peek().getRegisterManager().borrowRegister();
                    } catch (RuntimeException e) {
                        if (returnRegister != 0) {
                            register1 = 0;
                        } else {
                            register1 = 1;
                        }
                        appendToBuffer("\tstmfd\tr13!, {r" + register1 + "} ; No more register available, making space with memory stack\n");
                        isR1Borrowed = true;
                    }
                    expressionGen(ast, node.getChildren().get(0), register1);
                    appendToBuffer("\tstmfd\tr13!, {r" + returnRegister + "} ; Block for remainder : " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + " rem " + ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + "\n\tstmfd\tr13!, {r" + register1 + "}\n\tsub\tr13, r13, #4\n\tbl\trem\n\tldr r" + returnRegister + ", [r13]\n\tadd\tr13, r13, #4*3 ; 2 paramètres et 1 valeur de retour\n\n");
                    break;
                case "+":
                    expressionGen(ast, node.getChildren().get(0), returnRegister);
                    try { // If no register available, we use memory stack
                        register1 = stackFrames.peek().getRegisterManager().borrowRegister();
                    } catch (RuntimeException e) {
                        if (returnRegister != 0) {
                            register1 = 0;
                        } else {
                            register1 = 1;
                        }
                        appendToBuffer("\tstmfd\tr13!, {r" + register1 + "} ; No more register available, making space with memory stack\n");
                        isR1Borrowed = true;
                    }
                    expressionGen(ast, node.getChildren().get(1), register1);
                    appendToBuffer("\tadd\tr" + returnRegister + ", r" + returnRegister + ", r" + register1 + " ; Block for addition : " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + " + " + ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + "\n\n");
                    break;
                case "-":
                    expressionGen(ast, node.getChildren().get(1), returnRegister);
                    try { // If no register available, we use memory stack
                        register1 = stackFrames.peek().getRegisterManager().borrowRegister();
                    } catch (RuntimeException e) {
                        if (returnRegister != 0) {
                            register1 = 0;
                        } else {
                            register1 = 1;
                        }
                        appendToBuffer("\tstmfd\tr13!, {r" + register1 + "} ; No more register available, making space with memory stack\n");
                        isR1Borrowed = true;
                    }
                    expressionGen(ast, node.getChildren().get(0), register1);
                    if (ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() != "CALL" | ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() != "CALL") { // if there is 2 CALL, the first picked value will be the second returned value, so we switch
                        appendToBuffer("\tsub\tr" + returnRegister + ", r" + returnRegister + ", r" + register1 + " ; Block for substraction : " + ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + " - " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + "\n\n");
                    } else {
                        appendToBuffer("\tsub\tr" + returnRegister + ", r" + register1 + ", r" + returnRegister + " ; Block for substraction : " + ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + " - " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + "\n\n");
                    }
                    break;
                case "=", "/=", ">", ">=", "<", "<=", "OR", "AND":
                    expressionGen(ast, node.getChildren().get(1), returnRegister);
                    try { // If no register available, we use memory stack
                        register1 = stackFrames.peek().getRegisterManager().borrowRegister();
                    } catch (RuntimeException e) {
                        if (returnRegister != 0) {
                            register1 = 0;
                        } else {
                            register1 = 1;
                        }
                        appendToBuffer("\tstmfd\tr13!, {r" + register1 + "} ; No more register available, making space with memory stack\n");
                        isR1Borrowed = true;
                    }
                    expressionGen(ast, node.getChildren().get(0), register1);
                    switch (type) {
                        case "=":
                            appendToBuffer("\tcmp\tr" + register1 + ", r" + returnRegister + " ; Block for equality : " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + " = " + ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + "\n");
                            appendToBuffer("\tmoveq\tr" + returnRegister + ", #1\n\tmovne\tr" + returnRegister + ", #0\n\n");
                            break;
                        case "/=":
                            appendToBuffer("\tcmp\tr" + register1 + ", r" + returnRegister + " ; Block for equality : " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + " /= " + ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + "\n");
                            appendToBuffer("\tmovne\tr" + returnRegister + ", #1\n\tmoveq\tr" + returnRegister + ", #0\n\n");
                            break;
                        case ">":
                            appendToBuffer("\tcmp\tr" + register1 + ", r" + returnRegister + " ; Block for equality : " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + " > " + ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + "\n");
                            appendToBuffer("\tmovgt\tr" + returnRegister + ", #1\n\tmovle\tr" + returnRegister + ", #0\n\n");
                            break;
                        case ">=":
                            appendToBuffer("\tcmp\tr" + register1 + ", r" + returnRegister + " ; Block for equality : " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + " >= " + ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + "\n");
                            appendToBuffer("\tmovge\tr" + returnRegister + ", #1\n\tmovlt\tr" + returnRegister + ", #0\n\n");
                            break;
                        case "<":
                            appendToBuffer("\tcmp\tr" + register1 + ", r" + returnRegister + " ; Block for equality : " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + " < " + ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + "\n");
                            appendToBuffer("\tmovlt\tr" + returnRegister + ", #1\n\tmovge\tr" + returnRegister + ", #0\n\n");
                            break;
                        case "<=":
                            appendToBuffer("\tcmp\tr" + register1 + ", r" + returnRegister + " ; Block for equality : " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + " <= " + ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + "\n");
                            appendToBuffer("\tmovle\tr" + returnRegister + ", #1\n\tmovgt\tr" + returnRegister + ", #0\n\n");
                            break;
                        case "OR":
                            appendToBuffer("\torr\tr" + returnRegister + ", r" + returnRegister + ", r" + register1 + "\n\n");
                            break;
                        case "AND":
                            appendToBuffer("\tand\tr" + returnRegister + ", r" + returnRegister + ", r" + register1 + "\n\n");
                            break;
                    }
                    break;
                case "OR ELSE", "AND THEN":
                    expressionGen(ast, node.getChildren().get(0), returnRegister);
                    try { // If no register available, we use memory stack
                        register1 = stackFrames.peek().getRegisterManager().borrowRegister();
                    } catch (RuntimeException e) {
                        if (returnRegister != 0) {
                            register1 = 0;
                        } else {
                            register1 = 1;
                        }
                        appendToBuffer("\tstmfd\tr13!, {r" + register1 + "} ; No more register available, making space with memory stack\n");
                        isR1Borrowed = true;
                    }
                    switch (type) {
                        case "OR ELSE":
                            appendToBuffer("\tcmp\tr" + returnRegister + ", #1 ; Block for OR ELSE : " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + " OR ELSE " + ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + "\n");
                            appendToBuffer("\tmoveq\tr" + returnRegister + ", #1\n");
                            appendToBuffer("\tbeq\torelse" + nodeInt + "_end\n");
                            expressionGen(ast, node.getChildren().get(1), register1);
                            appendToBuffer("\torr\tr" + returnRegister + ", r" + returnRegister + ", r" + register1 + "\n\n");
                            appendToBuffer("\torelse" + nodeInt + "_end\n");
                            break;
                        case "AND THEN":
                            appendToBuffer("\tcmp\tr" + returnRegister + ", #0 ; Block for AND THEN : " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + " AND THEN " + ast.getTree().nodes.get(node.getChildren().get(1)).getLabel() + "\n");
                            appendToBuffer("\tmoveq\tr" + returnRegister + ", #0\n");
                            appendToBuffer("\tbeq\tandthen" + nodeInt + "_end\n");
                            expressionGen(ast, node.getChildren().get(1), register1);
                            appendToBuffer("\tand\tr" + returnRegister + ", r" + returnRegister + ", r" + register1 + "\n\n");
                            appendToBuffer("\tandthen" + nodeInt + "_end\n");
                            break;
                    }
                    break;
                case "NOT":
                    expressionGen(ast, node.getChildren().get(0), returnRegister);
                    try { // If no register available, we use memory stack
                        register1 = stackFrames.peek().getRegisterManager().borrowRegister();
                    } catch (RuntimeException e) {
                        if (returnRegister != 0) {
                            register1 = 0;
                        } else {
                            register1 = 1;
                        }
                        appendToBuffer("\tstmfd\tr13!, {r" + register1 + "} ; No more register available, making space with memory stack\n");
                        isR1Borrowed = true;
                    }
                    appendToBuffer("\tmov\tr" + register1 + ", #1\n");
                    appendToBuffer("\tsub\tr" + returnRegister + ", r" + register1 + ", r" + returnRegister + " ; Block for NOT : NOT " + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + "\n\n");
                    break;
                case "CALL":
                    int tmp = paramSize.pop(); // get the size of the parameters
                    int tmp1 = returnSize.pop(); // get the size of the return
                    // handle record case
                    if (tmp1 > 4) {
                        appendToBuffer("\tmov\tr" + returnRegister + ", r13 ; getting return value @ for expression (struct)\n");
                        appendToBuffer("\tadd\tr13, r13, #" + (tmp + tmp1) + " ; freeing the space of the parameters\n");
                    } else {
                        appendToBuffer("\tldmfd\tr13!, {r" + returnRegister + "} ; getting return value for expression\n");
                        appendToBuffer("\tadd\tr13, r13, #" + tmp + " ; freeing the space of the parameters\n");
                    }

                    if(paramSize.size() == 0) {
                        paramSize.push(0);
                    }
                    return tmp1 == 4 ? 0 : tmp1;
                case "CHARACTER'VAL":
                    expressionGen(ast, node.getChildren().get(0), returnRegister);
                    appendToBuffer("\tcmp\tr" + returnRegister + ", #127 ; Dynamic semantic control, is character'val between 1 and 127\n");
                    appendToBuffer("\tbgt\tchar_val_err\n");
                    appendToBuffer("\tcmp\tr" + returnRegister + ", #0 ; Dynamic semantic control, is character'val between 1 and 127\n");
                    appendToBuffer("\tblt\tchar_val_err\n");
                    return 0;
                case "UNARY":
                    expressionGen(ast, node.getChildren().get(0), returnRegister);
                    appendToBuffer("\tmov\tr10, #0\n");
                    appendToBuffer("\tsub\tr" + returnRegister + ", r10, r" + returnRegister + " ; Block for unary : -" + ast.getTree().nodes.get(node.getChildren().get(0)).getLabel() + "\n\n");
                    return 0;
                default: // Variable à aller chercher
                    // access record case
                    List<String> fields = new ArrayList<>();
                    fields.add(type);
                    Node nodeAccessIdent, nodeField;
                    while (!node.getChildren().isEmpty()) {
//                        for (int i = 0; i < node.getChildren().size(); i++) {
//                            nodeField = ast.getTree().nodes.get(node.getChildren().get(i));
//                        }
                        nodeAccessIdent = ast.getTree().nodes.get(node.getChildren().get(0));
                        nodeField = ast.getTree().nodes.get(nodeAccessIdent.getChildren().get(0));
                        fields.add(nodeField.getLabel());
                        node = nodeField;
                    }

                    return getVar(String.join(".", fields), returnRegister);
//                    appendToBuffer("\t; Unhandeled expression (for the moment) : " + type + "\n");
//                    throw new RuntimeException("Unhandeled expression : " + type);
//                    return 0;
            }
            if (isR1Borrowed) {
                appendToBuffer("\tldmfd\tr13!, {r" + register1 + "} ; Freeing memory stack\n");
            } else {
                stackFrames.peek().getRegisterManager().freeRegister(register1);
            }
        }
        return 0;
    }

    public void callGen(Symbol symbol, int region) {
        if (codeGenOn) {
            String name = symbol.getName() + region;
            if (Objects.equals(name, "put0")) {
                switch (((Proc) symbol).getTypes().get(0)) {
                    case "integer", "boolean":
                        appendToBuffer("\tbl\tprintln_int ; CALL put(int n)\n");
                        appendToBuffer("\tadd\tr13, r13, #4 ; free the param\n");
                        break;
                    case "character":
                        appendToBuffer("\tbl\tprintln_char ; CALL put(char c)\n");
                        appendToBuffer("\tadd\tr13, r13, #4 ; free the param\n");
                        break;
                }
            } else {
                appendToBuffer("\tbl\t" + name + " ; CALL\n");
            }
            appendToBuffer("\t; End of call\n\n");
        }
    }

    public int getVar(String name, int returnRegister) {
        if (codeGenOn) {
            Record record = getVarAddress(name, returnRegister);
            if (record == null) {
                appendToBuffer("\tldr\tr" + returnRegister + ", [r" + returnRegister + "] ; Getting value of var : " + name + "\n");
                return 0;
            }
            return record.getOffset();
        }
        return 0;
    }

    public Record getVarAddress(String label, int returnRegister) {
        if (codeGenOn) {
            String name = label;
            // handle record access
            List<String> fields = List.of();
            if (name.contains(".")) {
                fields = new ArrayList<>(Arrays.asList(name.split("\\.")));
                name = fields.get(0);
            }

            int destinationRegion = getRegionFromLabel(name, stack.peek());
            if (destinationRegion == -1) {
                throw new RuntimeException("Variable not found : " + name);
            }
            Symbol symbol = getSymbolFromLabel(name, destinationRegion);
            int start = tds.getTds().get(stack.peek()).get(0).getNestingLevel();
            int end = symbol.getNestingLevel();
            int linkingsToGoUp;
            linkingsToGoUp = start - end;
            int offset;
            if (symbol instanceof Var) {
                Var var = (Var) symbol;
                offset = var.getOffset();
                // handle record access
                if (!fields.isEmpty()) {
                    Record record = (Record) getSymbolFromLabel(var.getType(), destinationRegion);
                    fields.remove(0);
                    String field;
                    for (int i = 0; i < fields.size() - 1; i++) {
                        assert record != null;
                        field = fields.get(i);
                        offset -= record.getOffset(field);
                        record = (Record) getSymbolFromLabel(record.getFields().get(field), destinationRegion);
                    }
                    field = fields.get(fields.size() - 1);
                    assert record != null;
                    offset -= record.getOffset(field);

                    // check if several fields need to be accessed
                    if (TDS.offsets.get(record.getFields().get(field)) != 4) {
                        appendToBuffer("\tmov\tr10, r12\n");
                        for (int i = 0; i < linkingsToGoUp; i++) {
                            appendToBuffer("\tldr\tr10, [r10] ; Going up in the static linkings\n");
                        }
                        appendToBuffer("\tadd\tr10, r10, #-4-" + offset + " ; Getting address of var : " + label + "\n\tmov\tr" + returnRegister + ", r10\n");

                        return (Record) getSymbolFromLabel(record.getFields().get(field), destinationRegion);
                    }
                }

                appendToBuffer("\tmov\tr10, r12\n");
                for (int i = 0; i < linkingsToGoUp; i++) {
                    appendToBuffer("\tldr\tr10, [r10] ; Going up in the static linkings\n");
                }
                appendToBuffer("\tadd\tr10, r10, #-4-" + offset + " ; Getting address of var : " + label + "\n\tmov\tr" + returnRegister + ", r10\n");

                // handle record access
                if (fields.isEmpty() && TDS.offsets.get(var.getType()) != 4) {
                    return (Record) getSymbolFromLabel(var.getType(), destinationRegion);
                }
            } else if (symbol instanceof Param) {
                Param param = (Param) symbol;
                offset = param.getOffset();
                // handle record access
                if (!fields.isEmpty()) {
                    Record record = (Record) getSymbolFromLabel(param.getType(), destinationRegion);
                    fields.remove(0);
                    String field;
                    for (int i = 0; i < fields.size() - 1; i++) {
                        assert record != null;
                        field = fields.get(i);
                        offset -= record.getOffset(field);
                        record = (Record) getSymbolFromLabel(record.getFields().get(field), destinationRegion);
                    }
                    field = fields.get(fields.size() - 1);
                    assert record != null;
                    offset -= record.getOffset(field);

                    // check if several fields need to be accessed
                    if (TDS.offsets.get(record.getFields().get(field)) != 4) {
                        appendToBuffer("\tmov\tr10, r12\n");
                        for (int i = 0; i < linkingsToGoUp; i++) {
                            appendToBuffer("\tldr\tr10, [r10]\n");
                        }
                        appendToBuffer("\tadd\tr10, r10, #4*");
                        stackFrames.peek().needRegisterValue();
                        appendToBuffer("+" + offset + "+16 ; Getting param : " + label + "\n\tmov\tr" + returnRegister + ", r10\n");

                        return (Record) getSymbolFromLabel(record.getFields().get(field), destinationRegion);
                    }
                }

                appendToBuffer("\tmov\tr10, r12\n");
                for (int i = 0; i < linkingsToGoUp; i++) {
                    appendToBuffer("\tldr\tr10, [r10]\n");
                }
                appendToBuffer("\tadd\tr10, r10, #4*");
                stackFrames.peek().needRegisterValue();
                appendToBuffer("+" + offset + "+16 ; Getting param : " + label + "\n\tmov\tr" + returnRegister + ", r10\n");

                // handle record access
                if (fields.isEmpty() && TDS.offsets.get(param.getType()) != 4) {
                    return (Record) getSymbolFromLabel(param.getType(), destinationRegion);
                }
            }
            else {
                throw new RuntimeException("Unhandeled getVar : " + symbol + " " + name + " " + symbol.getClass());
            }
        }
        return null;
    }

    private int getRegionFromLabel(String label, int reg) {
        int father = 0;
        for (Symbol symbol : tds.getTds().get(reg)) {
            father = symbol.getFather();
            if (symbol.getName().equals(label)) {
                return reg;
            }
        }
        if (reg != 0) {
            return getRegionFromLabel(label, father);
        } else {
            return -1;
        }
    }

    private Symbol getSymbolFromLabel(String label, int reg) {
        int father = 0;
        for (Symbol symbol : tds.getTds().get(reg)) {
            father = symbol.getFather();
            if (symbol.getName().equals(label)) {
                return symbol;
            }
        }
        if (reg != 0) {
            return getSymbolFromLabel(label, father);
        } else {
            return null;
        }
    }

    public void stackReturn(Symbol symbol, int region) {
        if (codeGenOn) {
            String name = symbol.getName() + region;
            if (symbol instanceof Func) {
                appendToBuffer("\tsub\tr13, r13, #" + TDS.offsets.get(((Func) symbol).getReturnType()) + " ; " + name + " return val init\n");
            }
        }
    }

    public void addInitVar(Symbol symbol, int value) {
        if (codeGenOn) {
            initVars.peek().put(symbol, value);
        }
    }

    public void copyStructs(Record structToCopy, Integer addrToCopyTo, Integer addrToCopyFrom, Integer offset, boolean isParam) {
        if (codeGenOn) {
            int updateOffset = offset;
            for (String field : structToCopy.getFields().keySet()) {
                if (TDS.offsets.get(structToCopy.getFields().get(field)) == 4) {
                    appendToBuffer("\tldr\tr10, [r" + addrToCopyFrom + ", #" + (isParam ? "-" : "") + "4*" + updateOffset + "] ; Copying field " + field + " of struct\n");
                    appendToBuffer("\tstr\tr10, [r" + addrToCopyTo + ", #4*" + updateOffset + "]\n");
                    updateOffset++;
                } else {
                    Record record = (Record) getSymbolFromLabel(structToCopy.getFields().get(field), stack.peek());
                    copyStructs(record, addrToCopyTo, addrToCopyFrom, updateOffset, isParam);
                    assert record != null;
                    updateOffset += record.getOffset() / 4;
                }
            }
        }
    }

    public void returnValue(GraphViz ast, Integer nodeInt, String name) {
        if (codeGenOn) {
            appendToBuffer("\n\t; Return block\n");

            //borrow register
            int register;
            try {
                register = stackFrames.peek().getRegisterManager().borrowRegister();
            } catch (RuntimeException e) {
                register = 0;
            }
            int child = ast.getTree().nodes.get(nodeInt).getChildren().get(0);

            // set the return value
            // handle record case
            int offset;
            if ((offset = expressionGen(ast, child, register)) != 0) {
                appendToBuffer("\tmov\tr13, r11\n");
                for (int o = 0; o < offset; o += 4) {
                    appendToBuffer("\tldr\tr10, [r" + register + ", #" + o + "] ; Getting value of var\n");
                    appendToBuffer("\tstr\tr10, [r13, #4*");
                    stackFrames.peek().needRegisterValue();
                    appendToBuffer("+24+" + o + "] ; Setting the return value\n");
                }
            } else {
                appendToBuffer("\tmov\tr13, r11\n\tstr\tr" + register + ", [r13, #4*");
                stackFrames.peek().needRegisterValue();
                appendToBuffer("+24] ; Setting the return value\n");
            }

            appendToBuffer("\tb\tend" + name + callableElements.lastIndexOf(name) + " ; Jumping to the end of the function\n\t; End of return block\n   \n"); // Jump to the end of the function

        }
    }

    public Stack<Integer> getReturnSize() {
        return returnSize;
    }
}