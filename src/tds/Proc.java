package tds;

import java.util.ArrayList;
import java.util.List;

public class Proc extends Symbol {
    private int arguments;
    private List<String> types;

    public Proc(int nestingLevel, int father) {
        super(nestingLevel, father);
        this.arguments = 0;
        this.types = new ArrayList<>();
    }

    public void setArguments(int arguments) {
        this.arguments = arguments;
    }

    public void addType(String type) {
        this.types.add(type);
    }

    public int getArguments() {
        return arguments;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
