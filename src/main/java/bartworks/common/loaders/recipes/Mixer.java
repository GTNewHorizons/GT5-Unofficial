package bartworks.common.loaders.recipes;

import static gregtech.api.enums.Mods.Gendustry;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import bartworks.common.loaders.FluidLoader;
import bartworks.common.tileentities.multis.MTEThoriumHighTempReactor;
import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

public class Mixer implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 10), Materials.Uranium235.getDust(1))
            .circuit(2)
            .itemOutputs(new ItemStack(MTEThoriumHighTempReactor.THTRMaterials.aTHTR_Materials))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        if (Gendustry.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Radon, 1L))
                .circuit(17)
                .itemOutputs(Materials.Empty.getCells(1))
                .fluidInputs(GTModHandler.getLiquidDNA(1_000))
                .fluidOutputs(new FluidStack(FluidLoader.BioLabFluidMaterials[0], 2_000))
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(mixerRecipes);
        }

    }
}
