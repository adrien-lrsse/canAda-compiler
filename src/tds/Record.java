package tds;

import ast.SemanticException;

import java.util.HashMap;

public class Record extends Symbol {
    private HashMap<String, String> fields;

    public Record(int nestingLevel, int father) {
        super(nestingLevel, father);
        this.fields = new HashMap<>();
    }

    public void addField(String name, String type) throws SemanticException {
        if (this.fields.containsKey(name)) {
            throw new SemanticException("Field '" + name + "' already defined in record + '" + getName() + "'");
        }
        this.fields.put(name, type);
    }

    public HashMap<String, String> getFields() {
        return fields;
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
