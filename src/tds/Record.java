package tds;

import java.util.HashMap;

public class Record extends Symbol {
    private HashMap<String, String> fields;

    public Record(int nestingLevel, int father) {
        super(nestingLevel, father);
        this.fields = new HashMap<>();
    }

    public void addField(String name, String type) {
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
