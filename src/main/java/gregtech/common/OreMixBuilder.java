package gregtech.common;

import java.util.HashMap;
import java.util.Map;

import galacticgreg.api.enums.DimensionDef;
import gregtech.api.enums.Materials;

public class OreMixBuilder {

    public static final String OW = "Overworld";
    public static final String NETHER = "Nether";
    public static final String THE_END = "TheEnd";
    public static final String TWILIGHT_FOREST = "Twilight Forest";
    public String oreMixName;
    public boolean enabledByDefault = true;
    public Map<String, Boolean> dimsEnabled = new HashMap<>();
    public int minY, maxY, weight, density, size;
    public Materials primary, secondary, between, sporadic;

    public OreMixBuilder name(String name) {
        this.oreMixName = name;
        return this;
    }

    public OreMixBuilder disabledByDefault() {
        this.enabledByDefault = false;
        return this;
    }

    public OreMixBuilder enableInDim(DimensionDef... dims) {
        for (DimensionDef dim : dims) {
            this.dimsEnabled.put(dim.modDimensionDef.getDimensionName(), true);
        }
        return this;
    }

    public OreMixBuilder enableInDim(String... dims) {
        for (String dim : dims) {
            this.dimsEnabled.put(dim, true);
        }
        return this;
    }

    public OreMixBuilder heightRange(int minY, int maxY) {
        this.minY = minY;
        this.maxY = maxY;
        return this;
    }

    public OreMixBuilder density(int density) {
        this.density = density;
        return this;
    }

    public OreMixBuilder weight(int weight) {
        this.weight = weight;
        return this;
    }

    public OreMixBuilder size(int size) {
        this.size = size;
        return this;
    }

    public OreMixBuilder primary(Materials primary) {
        this.primary = primary;
        return this;
    }

    public OreMixBuilder secondary(Materials secondary) {
        this.secondary = secondary;
        return this;
    }

    public OreMixBuilder inBetween(Materials between) {
        this.between = between;
        return this;
    }

    public OreMixBuilder sporadic(Materials sporadic) {
        this.sporadic = sporadic;
        return this;
    }

}
