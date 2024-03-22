package asm;

import ast.GraphViz;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;


public class CodeGenerator {
    private GraphViz ast;
    private final FileWriter fileWriter;
    private List<StringBuilder> buffer;

    public CodeGenerator(GraphViz ast) {
        this.ast = ast;
        this.buffer = new ArrayList<>();
        try {
            this.fileWriter = new FileWriter(ast.getFilename()+"-output.s");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public FileWriter getfileWriter() {
        return fileWriter;
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
                System.out.println(last);
                fileWriter.write(last.toString());
            }
            else {
                throw new RuntimeException("Code Gen : Buffer is empty");
            }
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
}