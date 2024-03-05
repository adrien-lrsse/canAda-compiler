package tds;

import java.util.ArrayList;
import java.util.List;

public class Proc extends Symbol {
    private List<String> types;

    public Proc(int nestingLevel, int father) {
        super(nestingLevel, father);
        this.types = new ArrayList<>();
    }

    public void addType(String type) {
        this.types.add(type);
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "Proc{" +
                "nestingLevel=" + getNestingLevel() +
                ", father=" + getFather() +
                ", name='" + getName() + '\'' +
                ", types=" + types +
                '}';
    }
}
