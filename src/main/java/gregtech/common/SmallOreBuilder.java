package gregtech.common;

import java.util.HashSet;
import java.util.Set;

import galacticgreg.api.enums.DimensionDef;
import gregtech.api.interfaces.IMaterial;

public class SmallOreBuilder {

    public static final String OW = "Overworld";
    public static final String NETHER = "Nether";
    public static final String THE_END = "TheEnd";
    public static final String TWILIGHT_FOREST = "Twilight Forest";
    public String smallOreName;
    public boolean enabledByDefault = true;
    /** {full dimension name: enabled} */
    public Set<String> dimsEnabled = new HashSet<>();
    public int minY, maxY, amount;
    public IMaterial ore;

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
            this.dimsEnabled.add(dim.modDimensionDef.getDimensionName());
        }
        return this;
    }

    public SmallOreBuilder enableInDim(String... dims) {
        for (String dim : dims) {
            this.dimsEnabled.add(dim);
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

    public SmallOreBuilder ore(IMaterial ore) {
        this.ore = ore;
        return this;
    }
}
