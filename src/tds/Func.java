package tds;

import java.util.ArrayList;
import java.util.List;

public class Func extends Symbol {
    private String returnType;
    private List<String> types;

    public Func(int nestingLevel, int father) {
        super(nestingLevel, father);
        this.returnType = "";
        this.types = new ArrayList<>();
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public void addType(String type) {
        this.types.add(type);
    }

    public List<String> getTypes() {
        return types;
    }

    public String getReturnType() {
        return returnType;
    }

    @Override
    public String toString() {
        return "Func{" +
                "nestingLevel=" + getNestingLevel() +
                ", father=" + getFather() +
                ", name='" + getName() + '\'' +
                ", returnType='" + returnType + '\'' +
                ", types=" + types +
                '}';
    }
}
