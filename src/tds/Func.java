package tds;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Func extends Symbol {
    private String returnType;
    private List<String> types;
    private List<Integer> modes;

    public Func(int nestingLevel, int father) {
        super(nestingLevel, father);
        this.returnType = "";
        this.types = new ArrayList<>();
        this.modes = new ArrayList<>();
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getReturnType() {
        return returnType;
    }

    public void addType(String type) {
        this.types.add(type);
    }

    public List<String> getTypes() {
        return types;
    }

    public void addMode(int mode){
        this.modes.add(mode);
    }

    public List<Integer> getModes(){
        return this.modes;
    }

    public void setModes(List<Integer> modes){
        this.modes = modes;
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
