package gregtech.common;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.ruling_0.materiallib.api.Material;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffReconstruction;
import galacticgreg.api.enums.DimensionDef;
import gregtech.api.interfaces.IStoneCategory;

public class SmallOreBuilder {

    public String smallOreName;
    public boolean enabledByDefault = true;
    /** {full dimension name} */
    public Set<String> dimsEnabled = new HashSet<>();
    public int minY, maxY, amount;
    public Material ore;
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

    public SmallOreBuilder ore(Material ore) {
        this.ore = ore;
        return this;
    }

    public SmallOreBuilder ore(Werkstoff ore) {
        return ore(requireMaterialLib(ore));
    }

    static Material requireMaterialLib(Werkstoff werkstoff) {
        Material ml = WerkstoffReconstruction.materialLibOf(werkstoff);
        if (ml == null) {
            throw new IllegalStateException("No MaterialLib material for werkstoff " + werkstoff.getVarName());
        }
        return ml;
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
