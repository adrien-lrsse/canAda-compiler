package tds;

public class Var extends Symbol {
    private String type;
    private int offset;
    private boolean isProtected;

    public Var(int nestingLevel, int father) {
        super(nestingLevel, father);
        this.type = "";
        this.offset = 0;
        this.isProtected = false;
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

    public boolean isProtected() {
        return isProtected;
    }

    public void setProtected(boolean isProtected) {
        this.isProtected = isProtected;
    }

    @Override
    public String toString() {
        return "Var{" +
                "nestingLevel=" + getNestingLevel() +
                ", father=" + getFather() +
                ", name='" + getName() + '\'' +
                ", type='" + type + '\'' +
                ", offset=" + offset +
                '}';
    }
}
