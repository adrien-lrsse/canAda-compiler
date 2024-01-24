package ast;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private int id;
    private String label;
    private List<Integer> children;

    public Node(int id, String label) {
        this.id = id;
        this.label = label;
        this.children = new ArrayList<>();
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

    public boolean isLeaf(){
        return this.children.size() == 0;
    }
}
