package tds;

import java.util.ArrayList;
import java.util.List;

public class Func extends Symbol {
    private int arguments;
    private String returnType;
    private List<String> types;

    public Func(int nestingLevel, int father) {
        super(nestingLevel, father);
        this.arguments = 0;
        this.returnType = "";
        this.types = new ArrayList<>();
    }

    public void setArguments(int arguments) {
        this.arguments = arguments;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
