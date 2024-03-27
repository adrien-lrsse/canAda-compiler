package asm;

import ast.GraphViz;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CodeGenerator {
    private final FileWriter fileWriter;
    private List<StringBuilder> buffer;
    private List<StringBuilder> mainProcedureBuffer;

    public CodeGenerator(GraphViz ast) {
        this.buffer = new ArrayList<>();
        this.mainProcedureBuffer = new ArrayList<>();
        try {
            this.fileWriter = new FileWriter(ast.getFilename()+"-output.s");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        mainProcedureBuffer.add(s);
    }

    public void writeMainBuffer(){
        try {
            for (StringBuilder s : mainProcedureBuffer) {
                fileWriter.write(s.toString());
            }
            mainProcedureBuffer.clear();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        try {
            fileWriter.write(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}