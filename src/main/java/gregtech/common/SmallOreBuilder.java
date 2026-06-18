package gregtech.common;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import galacticgreg.api.enums.DimensionDef;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.IStoneCategory;

public class SmallOreBuilder {

    public String smallOreName;
    public boolean enabledByDefault = true;
    /** {full dimension name} */
    public Set<String> dimsEnabled = new HashSet<>();
    public int minY, maxY, amount;
    public IOreMaterial ore;
    public Set<IStoneCategory> stoneCategories;
    public boolean defaultStoneCategories = true;

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

    public SmallOreBuilder ore(IOreMaterial ore) {
        this.ore = ore;
        return this;
    }

    public SmallOreBuilder stoneType(IStoneCategory... stoneCategories) {
        if (defaultStoneCategories) {
            this.stoneCategories = new HashSet<>();
            defaultStoneCategories = false;
        }

        this.stoneCategories.addAll(Arrays.asList(stoneCategories));

        return this;
    }
}
