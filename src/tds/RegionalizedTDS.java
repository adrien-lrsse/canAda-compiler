package tds;

import java.util.ArrayList;
import java.util.List;

public class RegionalizedTDS {
    private int imbricationLevel;
    private String regionName;
    private List<SyombolOfTDS> symbols;

    public RegionalizedTDS(int imbricationLevel, String regionName) {
        this.imbricationLevel = imbricationLevel;
        this.regionName = regionName;
        this.symbols = new ArrayList<SyombolOfTDS>();
    }
}
