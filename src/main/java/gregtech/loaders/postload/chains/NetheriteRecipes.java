package gregtech.loaders.postload.chains;

import static gregtech.api.recipe.RecipeMaps.crackingRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.util.minecraft.FluidUtils;

public class NetheriteRecipes {

    public static void run() {

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.NetherAir.getFluid(1000))
            .fluidOutputs(Materials.NetherSemiFluid.getFluid(100))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .noOptimize()
            .addTo(vacuumFreezerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.NetherSemiFluid.getFluid(100000))
            .itemOutputs(ItemList.Heavy_Hellish_Mud.get(2))
            .fluidOutputs(
                Materials.NefariousGas.getFluid(4000),
                FluidUtils.getFluidStack("fluid.coalgas", 16000),
                FluidUtils.getFluidStack("fluid.anthracene", 7000),
                Materials.SulfurTrioxide.getGas(21000),
                Materials.SulfurDioxide.getGas(38000),
                Materials.NitrogenDioxide.getGas(14000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .noOptimize()
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Grade2PurifiedWater.getFluid(4000), Materials.NefariousGas.getFluid(16000))
            .fluidOutputs(Materials.NefariousOil.getFluid(12000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .noOptimize()
            .addTo(crackingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Heavy_Hellish_Mud.get(32))
            .fluidInputs(Materials.PoorNetherWaste.getFluid(16000))
            .fluidOutputs(Materials.RichNetherWaste.getFluid(16000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .noOptimize()
            .addTo(mixerRecipes);

    }
}
