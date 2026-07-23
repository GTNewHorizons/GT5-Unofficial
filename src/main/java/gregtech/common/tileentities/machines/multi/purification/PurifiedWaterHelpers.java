package gregtech.common.tileentities.machines.multi.purification;

import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.MU;
import gregtech.api.util.GTRecipe;

public class PurifiedWaterHelpers {

    public static Material getPurifiedWaterTier(int tier) {
        return switch (tier) {
            case 1 -> Materials2Materials.Grade1PurifiedWater;
            case 2 -> Materials2Materials.Grade2PurifiedWater;
            case 3 -> Materials2Materials.Grade3PurifiedWater;
            case 4 -> Materials2Materials.Grade4PurifiedWater;
            case 5 -> Materials2Materials.Grade5PurifiedWater;
            case 6 -> Materials2Materials.Grade6PurifiedWater;
            case 7 -> Materials2Materials.Grade7PurifiedWater;
            case 8 -> Materials2Materials.Grade8PurifiedWater;
            default -> throw new IllegalStateException("Unexpected value: " + tier);
        };
    }

    public static int getWaterTier(FluidStack fluid) {
        if (fluid == null) return 0;
        else if (fluid.isFluidEqual(MU.fluid(Materials2Materials.Grade1PurifiedWater, 1_000))) return 1;
        else if (fluid.isFluidEqual(MU.fluid(Materials2Materials.Grade2PurifiedWater, 1_000))) return 2;
        else if (fluid.isFluidEqual(MU.fluid(Materials2Materials.Grade3PurifiedWater, 1_000))) return 3;
        else if (fluid.isFluidEqual(MU.fluid(Materials2Materials.Grade4PurifiedWater, 1_000))) return 4;
        else if (fluid.isFluidEqual(MU.fluid(Materials2Materials.Grade5PurifiedWater, 1_000))) return 5;
        else if (fluid.isFluidEqual(MU.fluid(Materials2Materials.Grade6PurifiedWater, 1_000))) return 6;
        else if (fluid.isFluidEqual(MU.fluid(Materials2Materials.Grade7PurifiedWater, 1_000))) return 7;
        else if (fluid.isFluidEqual(MU.fluid(Materials2Materials.Grade8PurifiedWater, 1_000))) return 8;
        else return 0;
    }

    // Used to construct NEI comparator for water tier. Returns 0 if no water is used in this recipe
    public static int getWaterTierFromRecipe(GTRecipe recipe) {
        if (recipe.mFluidInputs.length == 0) return 0;
        else return getWaterTier(recipe.mFluidInputs[0]);
    }
}
