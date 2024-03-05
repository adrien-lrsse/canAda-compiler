package tds;

import java.util.HashMap;

public abstract class Symbol {
    private int nestingLevel;
    private int father;
    private String name;
    protected HashMap<String, Integer> offsets;


    public Symbol(int nestingLevel, int father) {
        this.nestingLevel = nestingLevel;
        this.father = father;
        this.name = "";
        this.offsets = new HashMap<>();
        offsets.put("int", 4);
    }

    public HashMap<String, Integer> getOffsets() {
        return offsets;
    }

    public void setName(String name) {
        this.name = name;
    }
}
