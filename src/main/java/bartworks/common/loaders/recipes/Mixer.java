package bartworks.common.loaders.recipes;

import static gregtech.api.enums.Mods.Gendustry;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import bartworks.common.loaders.FluidLoader;
import bartworks.common.tileentities.multis.MTEHighTempGasCooledReactor;
import bartworks.common.tileentities.multis.MTEThoriumHighTempReactor;
import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class Mixer implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 10),
                Materials.Uranium235.getDust(1),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(new ItemStack(MTEThoriumHighTempReactor.THTRMaterials.aTHTR_Materials))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        int i = 0;
        for (MTEHighTempGasCooledReactor.HTGRMaterials.Fuel_ fuel : MTEHighTempGasCooledReactor.HTGRMaterials.sHTGR_Fuel) {
            GTValues.RA.stdBuilder()
                .itemInputs(fuel.mainItem, fuel.secondaryItem, GTUtility.getIntegratedCircuit(1))
                .itemOutputs(new ItemStack(MTEHighTempGasCooledReactor.HTGRMaterials.aHTGR_Materials, 1, i))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(mixerRecipes);

            i += MTEHighTempGasCooledReactor.HTGRMaterials.MATERIALS_PER_FUEL;
        }

        if (Gendustry.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(17),
                    GTOreDictUnificator.get(OrePrefixes.cell, Materials.Radon, 1L))
                .itemOutputs(Materials.Empty.getCells(1))
                .fluidInputs(FluidRegistry.getFluidStack("liquiddna", 1000))
                .fluidOutputs(new FluidStack(FluidLoader.BioLabFluidMaterials[0], 2000))
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(mixerRecipes);
        }

    }
}
