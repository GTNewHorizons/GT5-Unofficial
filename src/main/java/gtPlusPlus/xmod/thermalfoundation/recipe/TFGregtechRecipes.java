package gtPlusPlus.xmod.thermalfoundation.recipe;

import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;

public class TFGregtechRecipes {

    public static void run() {
        start();
    }

    private static void start() {

        // Gelid Cryotheum
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.ore, Materials.Cinnabar, 1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cinnabar, 3L))
            .fluidInputs(new FluidStack(TFFluids.fluidCryotheum, 1 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalBathRecipes);

        // Blizz Powder
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.blaze_powder, 4))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blizz, 4L))
            .fluidInputs(Materials.LiquidNitrogen.getGas(100L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(chemicalBathRecipes);

        // Blizz Rod
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.blaze_rod))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stick, Materials.Blizz, 1L))
            .duration(((int) Math.max((Materials.Blaze.getMass()) * 3L, 1L)) * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(vacuumFreezerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Pyrotheum, 1L))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Snow, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blizz, 1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cryotheum, 1L))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Niter, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Snow, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blizz, 1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cryotheum, 1L))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);
    }
}
