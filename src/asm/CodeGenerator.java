package asm;

import ast.GraphViz;
import tds.Symbol;
import tds.TDS;
import tds.Var;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CodeGenerator {
    private final FileWriter fileWriter;
    private List<StringBuilder> buffer;
    private List<StringBuilder> mainProcedureBuffer;
    private boolean codeGenOn = true;

    public CodeGenerator(GraphViz ast) {
        this.buffer = new ArrayList<>();
        this.mainProcedureBuffer = new ArrayList<>();
        try {
            this.fileWriter = new FileWriter(ast.getFilename()+"-output.s");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setcodeGenOn(boolean codeGenOn) {
        codeGenOn = codeGenOn;
    }

    public void addBuffer(StringBuilder s) {
        buffer.add(s);
    }

    public void writeLastBuffer() {
        try {
            if (!buffer.isEmpty()) {
                //pop the last element
                StringBuilder last = buffer.get(buffer.size() - 1);
                buffer.remove(buffer.size() - 1);
                fileWriter.write(last.toString());
            }
            else {
                throw new RuntimeException("Code Gen : Buffer is empty");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addMainProcedureBuffer(StringBuilder s) {
        if (codeGenOn) {
            mainProcedureBuffer.add(s);
        }
    }

    public void writeMainBuffer(){
        if (codeGenOn) {
            try {
                for (StringBuilder s : mainProcedureBuffer) {
                    fileWriter.write(s.toString());
                }
                mainProcedureBuffer.clear();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void close() {
        try {
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(String s) {
        if (codeGenOn) {
            try {
                fileWriter.write(s);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public void procedureGen(TDS tds, String name, String last) {
        if (codeGenOn) {
            if (tds.getTds().get(2) == null) {
                // procedure main
                this.write("b\t" + name + last + "\n\n");
                this.addMainProcedureBuffer(new StringBuilder(name + last + "\tmov r11, r13\n"));
            } else {
                this.write(name + last + "\tstmfd r13!,{r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, lr} ; Begin of procedure " + name + last + "\n" +
                        "mov r11, r13\n\n");
                this.addBuffer(new StringBuilder("\nmov  r11, r13\n" +
                        "ldmfd  r13!,{r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, pc} ; End of procedure " + name + last + "\n\n\n"));
            }
        }
    }

    public void functionGen(String name, String last) {
        if(codeGenOn){
            this.write(name+last+ "\tstmfd r13!,{r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, lr} ; Begin of function "+name+last+"\n" +
                    "mov r11, r13\n;PARAMS\n");
            this.addBuffer(new StringBuilder("\nmov  r11, r13\n" +
                    "ldmfd  r13!,{r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, pc} ; End of function "+name+last+"\n\n\n"));
        }
    }

    public void varGen(int fatherMain, List<Symbol> symbolsOfRegion) {
        if(codeGenOn){
            //checking if we are in the main procedure
            if (fatherMain == 0) {
                // procedure main
                this.writeMainBuffer();
            }

            this.write(";VARIABLES\n");
            int lastOffset = -1;
            for (Symbol symbol : symbolsOfRegion){
                if (symbol instanceof Var){
                    lastOffset = ((Var) symbol).getOffset();
                    this.write("\t;"+((Var) symbol).getType()+"    "+symbol.getName()+"\n");
                }
            }
            if(lastOffset != -1){
                this.write("sub r13, r13, #"+lastOffset+"\n");
            }
            this.write("\n\n");
        }
    }

    public void endOfBlockGen(int fatherTest) {
        if(codeGenOn){
            if (fatherTest != 0) {
                this.writeLastBuffer();
            }
        }
    }
}