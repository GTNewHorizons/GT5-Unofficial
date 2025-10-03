package gregtech.common;

import java.util.HashMap;
import java.util.Map;

import galacticgreg.api.enums.DimensionDef;
import gregtech.api.enums.Materials;

public class SmallOreBuilder {

    public static final String OW = "Overworld";
    public static final String NETHER = "Nether";
    public static final String THE_END = "TheEnd";
    public static final String TWILIGHT_FOREST = "Twilight Forest";
    public String smallOreName;
    public boolean enabledByDefault = true;
    public Map<String, Boolean> dimsEnabled = new HashMap<>();
    public int minY, maxY, amount;
    public Materials ore;

    public SmallOreBuilder name(String name) {
        this.smallOreName = name;
        return this;
    }

    public SmallOreBuilder disabledByDefault() {
        this.enabledByDefault = false;
        return this;
    }

    public SmallOreBuilder enableInDim(DimensionDef... dims) {
        for (DimensionDef dim : dims) {
            this.dimsEnabled.put(dim.modDimensionDef.getDimensionName(), true);
        }
        return this;
    }

    public SmallOreBuilder enableInDim(String... dims) {
        for (String dim : dims) {
            this.dimsEnabled.put(dim, true);
        }
        return this;
    }

    public SmallOreBuilder heightRange(int minY, int maxY) {
        this.minY = minY;
        this.maxY = maxY;
        return this;
    }

    public SmallOreBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public SmallOreBuilder ore(Materials ore) {
        this.ore = ore;
        return this;
    }
}
