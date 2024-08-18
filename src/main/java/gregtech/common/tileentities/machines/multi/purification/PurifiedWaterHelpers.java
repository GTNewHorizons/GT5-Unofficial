package gregtech.common.tileentities.machines.multi.purification;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Recipe;

public class PurifiedWaterHelpers {

    public static Materials getPurifiedWaterTier(int tier) {
        return switch (tier) {
            case 1 -> Materials.Grade1PurifiedWater;
            case 2 -> Materials.Grade2PurifiedWater;
            case 3 -> Materials.Grade3PurifiedWater;
            case 4 -> Materials.Grade4PurifiedWater;
            case 5 -> Materials.Grade5PurifiedWater;
            case 6 -> Materials.Grade6PurifiedWater;
            case 7 -> Materials.Grade7PurifiedWater;
            case 8 -> Materials.Grade8PurifiedWater;
            default -> throw new IllegalStateException("Unexpected value: " + tier);
        };
    }

    public static int getWaterTier(FluidStack fluid) {
        if (fluid == null) return 0;
        else if (fluid.isFluidEqual(Materials.Grade1PurifiedWater.getFluid(1000L))) return 1;
        else if (fluid.isFluidEqual(Materials.Grade2PurifiedWater.getFluid(1000L))) return 2;
        else if (fluid.isFluidEqual(Materials.Grade3PurifiedWater.getFluid(1000L))) return 3;
        else if (fluid.isFluidEqual(Materials.Grade4PurifiedWater.getFluid(1000L))) return 4;
        else if (fluid.isFluidEqual(Materials.Grade5PurifiedWater.getFluid(1000L))) return 5;
        else if (fluid.isFluidEqual(Materials.Grade6PurifiedWater.getFluid(1000L))) return 6;
        else if (fluid.isFluidEqual(Materials.Grade7PurifiedWater.getFluid(1000L))) return 7;
        else if (fluid.isFluidEqual(Materials.Grade8PurifiedWater.getFluid(1000L))) return 8;
        else return 0;
    }

    // Used to construct NEI comparator for water tier. Returns 0 if no water is used in this recipe
    public static int getWaterTierFromRecipe(GT_Recipe recipe) {
        if (recipe.mFluidInputs.length == 0) return 0;
        else return getWaterTier(recipe.mFluidInputs[0]);
    }
}
