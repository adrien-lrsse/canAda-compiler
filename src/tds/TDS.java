package tds;

import ast.SemanticException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TDS {
    private HashMap<Integer, List<Symbol>> tds;

    public TDS() {
        this.tds = new HashMap<>();
    }

    public HashMap<Integer, List<Symbol>> getTds() {
        return tds;
    }

    public int newRegion() {
        int region = tds.size();
        tds.put(region, new ArrayList<>());
        return region;
    }

    public int addSymbol(int region, Symbol symbol) throws SemanticException {
        if (region != 0 && symbol.getName().equals("put")) {
            throw new SemanticException("'put' is a reserved word, it cannot be redefined");
        }
        for (Symbol s : tds.get(region)) {
            if (s.getName().equals(symbol.getName()) && !s.getName().equals("put")) {
                throw new SemanticException("Label '" + symbol.getName() + "' already used in this scope");
            }
        }
        tds.get(region).add(symbol);
        return tds.get(region).size() - 1;
    }

    public void display() {
        for (int region : tds.keySet()) {
            System.out.println("Region : " + region);
            for (Symbol symbol : tds.get(region)) {
                System.out.println("\t" + symbol);
            }
        }
    }
}
