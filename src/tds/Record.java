package tds;

import ast.SemanticException;

import java.util.HashMap;

public class Record extends Symbol {
    private HashMap<String, String> fields;

    private HashMap<String, Integer> offsets;

    private int offset;

    public Record(int nestingLevel, int father) {
        super(nestingLevel, father);
        this.fields = new HashMap<>();
        this.offsets = new HashMap<>();
    }

    public void addField(String name, String type, int line) throws SemanticException {
        if (this.fields.containsKey(name)) {
            throw new SemanticException("Field '" + name + "' already defined in record + '" + getName() + "'", line);
        } else if (TDS.offsets.get(type) == null) {
            throw new SemanticException("Type '" + type + "' not defined", line);
        }
        this.fields.put(name, type);
        this.offset += TDS.offsets.get(type);
        this.offsets.put(name, TDS.offsets.get(type));
    }

    public HashMap<String, String> getFields() {
        return fields;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset(String field) {
        return offsets.get(field);
    }

    @Override
    public String toString() {
        return "Record{" +
                "nestingLevel=" + getNestingLevel() +
                ", father=" + getFather() +
                ", name='" + getName() + '\'' +
                ", fields=" + fields +
                ", offsets=" + offsets +
                ", offset=" + getOffset() +
                '}';
    }
}
