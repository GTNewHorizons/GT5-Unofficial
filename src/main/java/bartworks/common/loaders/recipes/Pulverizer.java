package bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import bartworks.common.loaders.ItemRegistry;
import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;

public class Pulverizer implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 1))
            .itemOutputs(Materials.BorosilicateGlass.getDust(9), Materials.Titanium.getDust(8))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 2))
            .itemOutputs(Materials.BorosilicateGlass.getDust(9), Materials.TungstenSteel.getDust(8))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 3))
            .itemOutputs(
                Materials.BorosilicateGlass.getDust(9),
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.dust, 8))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 4))
            .itemOutputs(Materials.BorosilicateGlass.getDust(9), Materials.Iridium.getDust(8))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 5))
            .itemOutputs(Materials.BorosilicateGlass.getDust(9), Materials.Osmium.getDust(8))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 13))
            .itemOutputs(Materials.BorosilicateGlass.getDust(9), Materials.Neutronium.getDust(8))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 14))
            .itemOutputs(Materials.BorosilicateGlass.getDust(9), Materials.CosmicNeutronium.getDust(8))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 15))
            .itemOutputs(Materials.BorosilicateGlass.getDust(9), Materials.Infinity.getDust(8))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[1], 1, 0))
            .itemOutputs(Materials.BorosilicateGlass.getDust(9), Materials.TranscendentMetal.getDust(8))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(maceratorRecipes);

        for (int i = 6; i < 11; i++) {

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, i))
                .itemOutputs(Materials.BorosilicateGlass.getDust(9))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(maceratorRecipes);

        }
    }
}
