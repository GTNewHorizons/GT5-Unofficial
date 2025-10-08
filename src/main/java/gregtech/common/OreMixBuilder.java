package gregtech.common;

import java.util.Arrays;
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
    public Materials primary, secondary, between, sporadic, representative;
    public String localizedName;

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
        if (representative == null || localizedName == null) {
            representative = primary;
            localizedName = primary.mLocalizedName;
        }
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

    /**
     * Sets the localized name for the ore mix based on the provided materials. If more than one material is provided,
     * their localized names are concatenated with commas, last comma is replaced by "&".
     *
     * @param materials The materials to be used for localization. The first material in the array will be used to
     *                  represent to ore mix in GUI's. If none are provided the {@link #primary} will be used.
     */
    public OreMixBuilder localize(Materials... materials) {
        if (materials.length > 1) {
            String localizedName = String.join(
                ", ",
                Arrays.stream(materials)
                    .map(material -> material.mLocalizedName)
                    .toArray(String[]::new));
            int index = localizedName.lastIndexOf(", ");
            if (index != -1) {
                localizedName = localizedName.substring(0, index) + " & " + localizedName.substring(index + 2);
            }
            this.localizedName = localizedName;
        } else {
            this.localizedName = materials[0].mLocalizedName;
        }
        this.representative = materials[0];
        return this;
    }
}
