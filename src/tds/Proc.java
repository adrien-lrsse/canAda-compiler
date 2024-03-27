package tds;

import java.util.ArrayList;
import java.util.List;

public class Proc extends Symbol {
    private List<String> types;
    private List<Integer> modes;

    public Proc(int nestingLevel, int father) {
        super(nestingLevel, father);
        this.types = new ArrayList<>();
        this.modes = new ArrayList<>();
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
        return "Proc{" +
                "nestingLevel=" + getNestingLevel() +
                ", father=" + getFather() +
                ", name='" + getName() + '\'' +
                ", types=" + types +
                '}';
    }
}
