package gregtech.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.util.StatCollector;

import com.ruling_0.materiallib.api.Material;

import bartworks.system.material.Werkstoff;
import galacticgreg.api.enums.DimensionDef;
import gregtech.api.enums.StoneCategory;
import gregtech.api.interfaces.IStoneCategory;
import gregtech.api.material.MU;
import gregtech.api.util.StringUtils;
import it.unimi.dsi.fastutil.shorts.ShortShortPair;

public class OreMixBuilder {

    public String oreMixName;
    public boolean enabledByDefault = true;
    /** {full dim name} */
    public Set<String> dimsEnabled = new HashSet<>();
    public Map<String, ShortShortPair> dimVeinHeights = new HashMap<>();
    public int minY, maxY, weight, density, size;
    public Material primary, secondary, between, sporadic;
    public Object representative;
    public Set<IStoneCategory> stoneCategories = new HashSet<>(Arrays.asList(StoneCategory.Stone));
    public boolean defaultStoneCategories = true;
    public List<String> materialKeys = new ArrayList<>();

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
            this.dimsEnabled.add(dim.modDimensionDef.getDimensionName());
        }
        return this;
    }

    public OreMixBuilder enableInDim(String... dims) {
        for (String dim : dims) {
            this.dimsEnabled.add(dim);
        }
        return this;
    }

    public OreMixBuilder heightRange(int minY, int maxY) {
        this.minY = minY;
        this.maxY = maxY;
        return this;
    }

    public OreMixBuilder heightRangeOverride(DimensionDef dim, int minY, int maxY) {
        ShortShortPair pair = ShortShortPair.of((short) minY, (short) maxY);
        dimVeinHeights.put(dim.modDimensionDef.getDimensionName(), pair);
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

    public OreMixBuilder primary(Material primary) {
        this.primary = primary;
        if (representative == null || materialKeys.isEmpty()) {
            representative = primary;
            materialKeys.add(MU.localizedNameKeyOf(primary));
        }
        return this;
    }

    public OreMixBuilder secondary(Material secondary) {
        this.secondary = secondary;
        return this;
    }

    public OreMixBuilder inBetween(Material between) {
        this.between = between;
        return this;
    }

    public OreMixBuilder sporadic(Material sporadic) {
        this.sporadic = sporadic;
        return this;
    }

    public OreMixBuilder primary(Werkstoff primary) {
        return primary(SmallOreBuilder.requireMaterialLib(primary));
    }

    public OreMixBuilder secondary(Werkstoff secondary) {
        return secondary(SmallOreBuilder.requireMaterialLib(secondary));
    }

    public OreMixBuilder inBetween(Werkstoff between) {
        return inBetween(SmallOreBuilder.requireMaterialLib(between));
    }

    public OreMixBuilder sporadic(Werkstoff sporadic) {
        return sporadic(SmallOreBuilder.requireMaterialLib(sporadic));
    }

    public OreMixBuilder stoneCategory(IStoneCategory... stoneCategories) {
        if (defaultStoneCategories) {
            this.stoneCategories = new HashSet<>();
            defaultStoneCategories = false;
        }

        this.stoneCategories.addAll(Arrays.asList(stoneCategories));

        return this;
    }

    /**
     * Sets the localized name for the ore mix based on the provided materials. If more than one material is provided,
     * their localized names are concatenated with commas, last comma is replaced by "&".
     *
     * @param materials The materials to be used for localization. The first material in the array will be used to
     *                  represent to ore mix in GUI's. If none are provided the {@link #primary} will be used.
     */
    public OreMixBuilder setLocalizedName(Object... materials) {
        if (materials.length == 1) this.representative = materials[0];
        for (Object m : materials) {
            materialKeys.add(MU.localizedNameKeyOf(m));
        }
        return this;
    }

    public String getLocalizedName() {
        return StringUtils.formatList(
            materialKeys.stream()
                .map(StatCollector::translateToLocal)
                .toArray(String[]::new));
    }
}
