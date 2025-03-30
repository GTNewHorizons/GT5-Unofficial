package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.steamExtractinatorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.util.minecraft.FluidUtils;

public class SteamExtractinatorRecipes implements Runnable {

    @Override
    public void run() {

        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.gravel, 16))
            .fluidInputs(FluidUtils.getWater(1000))
            .fluidOutputs(Materials.GravelSluice.getFluid(4000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.sand, 16))
            .fluidInputs(FluidUtils.getWater(1000))
            .fluidOutputs(Materials.SandSluice.getFluid(4000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.obsidian, 16))
            .fluidInputs(FluidUtils.getWater(1000))
            .fluidOutputs(Materials.ObsidianSluice.getFluid(4000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Emerald, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ruby, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Salt, 4))
            .fluidInputs(FluidUtils.getWater(1000))
            .fluidOutputs(Materials.GemSluice.getFluid(4000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        RA.stdBuilder()
            .fluidInputs(Materials.GravelSluice.getFluid(4000))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Iron, 8),
                GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Copper, 8),
                GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Tin, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Clay, 8),
                GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Salt, 8),
                GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Coal, 8))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(steamExtractinatorRecipes);

        RA.stdBuilder()
            .fluidInputs(Materials.SandSluice.getFluid(4000))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Gold, 8),
                GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Redstone, 8),
                GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Zinc, 8),
                GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Ruby, 8),
                GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Sulfur, 8))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(steamExtractinatorRecipes);

        RA.stdBuilder()
            .fluidInputs(Materials.ObsidianSluice.getFluid(4000))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Silver, 8),
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.Diamond, 8),
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.Emerald, 8),
                GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Gypsum, 1),
                GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Calcite, 1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(steamExtractinatorRecipes);

        RA.stdBuilder()
            .fluidInputs(Materials.GemSluice.getFluid(4000))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Diamond, 1),
                GTOreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Emerald, 1),
                GTOreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Ruby, 1),
                GTOreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Salt, 1))
            .outputChances(1000, 1000, 1000, 2000)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(steamExtractinatorRecipes);
    }
}
