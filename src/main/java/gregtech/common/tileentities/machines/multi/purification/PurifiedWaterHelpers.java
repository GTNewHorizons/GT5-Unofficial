package gregtech.common.tileentities.machines.multi.purification;

import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.util.GTRecipe;

public class PurifiedWaterHelpers {

    public static Material getPurifiedWaterTier(int tier) {
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
        else if (fluid.isFluidEqual(
            MaterialLibAPI.getFluidStack(Materials.Grade1PurifiedWater, FluidShapes.fluidLiquid, 1_000))) return 1;
        else if (fluid.isFluidEqual(
            MaterialLibAPI.getFluidStack(Materials.Grade2PurifiedWater, FluidShapes.fluidLiquid, 1_000))) return 2;
        else if (fluid.isFluidEqual(
            MaterialLibAPI.getFluidStack(Materials.Grade3PurifiedWater, FluidShapes.fluidLiquid, 1_000))) return 3;
        else if (fluid.isFluidEqual(
            MaterialLibAPI.getFluidStack(Materials.Grade4PurifiedWater, FluidShapes.fluidLiquid, 1_000))) return 4;
        else if (fluid.isFluidEqual(
            MaterialLibAPI.getFluidStack(Materials.Grade5PurifiedWater, FluidShapes.fluidLiquid, 1_000))) return 5;
        else if (fluid.isFluidEqual(
            MaterialLibAPI.getFluidStack(Materials.Grade6PurifiedWater, FluidShapes.fluidLiquid, 1_000))) return 6;
        else if (fluid.isFluidEqual(
            MaterialLibAPI.getFluidStack(Materials.Grade7PurifiedWater, FluidShapes.fluidLiquid, 1_000))) return 7;
        else if (fluid.isFluidEqual(
            MaterialLibAPI.getFluidStack(Materials.Grade8PurifiedWater, FluidShapes.fluidLiquid, 1_000))) return 8;
        else return 0;
    }

    // Used to construct NEI comparator for water tier. Returns 0 if no water is used in this recipe
    public static int getWaterTierFromRecipe(GTRecipe recipe) {
        if (recipe.mFluidInputs.length == 0) return 0;
        else return getWaterTier(recipe.mFluidInputs[0]);
    }
}
