package tds;

public class Param extends Symbol {
    private String type;
    private int offset;
    private int mode; // 0 = in, 1 = in out

    public Param(int nestingLevel, int father) {
        super(nestingLevel, father);
        this.type = "";
        this.offset = 0;
        this.mode = 0;
    }

    public String getType() {
        return type;
    }

    public int getOffset() {
        return offset;
    }

    public int getMode() {
        return mode;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "Param{" +
                "nestingLevel=" + getNestingLevel() +
                ", father=" + getFather() +
                ", name='" + getName() + '\'' +
                ", type='" + type + '\'' +
                ", offset=" + offset +
                ", mode=" + mode +
                '}';
    }
}
