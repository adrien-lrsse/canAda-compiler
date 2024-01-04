package ast;


import java.io.FileWriter;
import java.io.IOException;

public class GraphViz {
    private FileWriter file;
    public int nodeCount = 0;

    public GraphViz(String filename) {
        try {
            file = new FileWriter(filename + ".dot");
            file.write("""
graph ""
\t{
\t\tfontname="Helvetica,Arial,sans-serif"
\t\tnode [fontname="Helvetica,Arial,sans-serif"]
\t\tedge [fontname="Helvetica,Arial,sans-serif"]
\t\tsubgraph cluster01
\t\t\t{
\t\t\t\tlabel="AST"
                    """);
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
            file.write("""
\t\t\t}
\t}
                    """);
            file.flush();
            file.close();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
