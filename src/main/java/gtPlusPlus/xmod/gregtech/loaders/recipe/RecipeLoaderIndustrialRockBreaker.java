package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.multiblockRockBreakerRecipes;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class RecipeLoaderIndustrialRockBreaker {

    public static void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .itemOutputs(new ItemStack(Blocks.cobblestone, 1))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockRockBreakerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2))
            .itemOutputs(new ItemStack(Blocks.stone, 1))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockRockBreakerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L))
            .itemOutputs(new ItemStack(Blocks.obsidian, 1))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockRockBreakerRecipes);
    }
}
