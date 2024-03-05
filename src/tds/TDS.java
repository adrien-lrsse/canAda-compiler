package tds;

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
}
