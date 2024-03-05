package tds;

public abstract class Symbol {
    private int nestingLevel;
    private int father;
    private String name;

    public Symbol(int nestingLevel, int father) {
        this.nestingLevel = nestingLevel;
        this.father = father;
        this.name = "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNestingLevel() {
        return nestingLevel;
    }

    public int getFather() {
        return father;
    }

    public String getName() {
        return name;
    }
}
