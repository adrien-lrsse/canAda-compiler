package ast;

import java.util.*;

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

    public List<Node> depthFirstTraversal() {
        List<Node> result = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Stack<Integer> stack = new Stack<>();

        if (!nodes.isEmpty()) {
            int startNodeId = nodes.keySet().iterator().next();
            stack.push(startNodeId);

            while (!stack.isEmpty()) {
                int nodeId = stack.pop();
                if (!visited.contains(nodeId)) {
                    Node currentNode = nodes.get(nodeId);
                    result.add(currentNode);
                    visited.add(nodeId);

                    List<Integer> children = currentNode.getChildren();
                    for (int i = children.size() - 1; i >= 0; i--) {
                        int childId = children.get(i);
                        if (!visited.contains(childId)) {
                            stack.push(childId);
                        }
                    }
                }
            }
        }

        return result;
    }
}
