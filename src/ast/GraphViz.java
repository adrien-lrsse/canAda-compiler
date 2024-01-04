package ast;


import java.io.FileWriter;
import java.io.IOException;

public class GraphViz {
    private FileWriter file;
    public int nodeCount = 0;

    public GraphViz(String filename) {
        try {
            file = new FileWriter(filename + ".dot");
            file.write("graph \"\"\n" +
                    "\t{\n" +
                    "\t\tfontname=\"Helvetica,Arial,sans-serif\"\n" +
                    "\t\tnode [fontname=\"Helvetica,Arial,sans-serif\"]\n" +
                    "\t\tedge [fontname=\"Helvetica,Arial,sans-serif\"]\n" +
                    "\t\tsubgraph cluster01\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\tlabel=\"AST\"\n");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
   }

    public int addNode(String node) {
        try {
            file.write("\t\t\t\tnode" + nodeCount + ";\n");
            file.write("\t\t\t\tnode" + nodeCount + " [label=\"" + node + "\"];\n");
            return nodeCount++;
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        return -1;
    }

    public void addEdge(int node1, int node2) {
        try {
            file.write("\t\t\t\tnode" + node1 + " -- node" + node2 + ";\n");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public void close() {
        try {
            file.write("\t\t\t}\n" +
                    "\t}\n");
            file.flush();
            file.close();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
