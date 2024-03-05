package tds;

public class Var extends Symbol {
    private String type;
    private int offset;

    public Var(int nestingLevel, int father) {
        super(nestingLevel, father);
        this.type = "";
        this.offset = 0;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
