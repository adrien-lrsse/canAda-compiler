package ast;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Tree {
    public Map<Integer, Node> nodes;

    public Tree(){
        this.nodes = new HashMap<>();
    }

    public void addEdge(int parent, int child) {
        nodes.get(parent).addChild(child);
    }

    public void addNode(int id, String label) {
        nodes.put(id, new Node(id, label));
    }

    public void display() {
        for (Node node : nodes.values()) {
            System.out.println("Node : " + node.getId() + " - " + node.getLabel());
            System.out.println("Children : " + node.getChildren());
        }
    }
}
