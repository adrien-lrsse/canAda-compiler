package tds;

import ast.SemanticException;

import java.util.HashMap;

public class Record extends Symbol {
    private HashMap<String, String> fields;

    public Record(int nestingLevel, int father) {
        super(nestingLevel, father);
        this.fields = new HashMap<>();
    }

    public void addField(String name, String type, int line) throws SemanticException {
        if (this.fields.containsKey(name)) {
            throw new SemanticException("Field '" + name + "' already defined in record + '" + getName() + "'", line);
        }
        this.fields.put(name, type);
    }

    @Override
    public String toString() {
        return "Record{" +
                "nestingLevel=" + getNestingLevel() +
                ", father=" + getFather() +
                ", name='" + getName() + '\'' +
                ", fields=" + fields +
                '}';
    }
}
