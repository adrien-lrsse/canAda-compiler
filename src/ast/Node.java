package ast;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private int id;
    private String label;
    private List<Integer> children;
    private int line;

    public Node(int id, String label, int line) {
        this.id = id;
        this.label = label;
        this.children = new ArrayList<>();
        this.line = line;
    }

    public void addChild(int child) {
        children.add(child);
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public List<Integer> getChildren() {
        return children;
    }

    public int getLine() {
        return line;
    }

    public boolean isLeaf(){
        return this.children.size() == 0;
    }
}
