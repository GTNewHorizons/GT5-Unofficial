package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.steamGateAssemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;

public class SteamGateAssemblerRecipes implements Runnable {

    @Override
    public void run() {
        ItemStack bronzeBlock = GTOreDictUnificator.get(OrePrefixes.block, Materials.Bronze, 1);

        GTValues.RA.stdBuilder()
            .itemInputs(
                null, null, null, null, bronzeBlock, null, null, null, null,
                null, null, null, bronzeBlock, bronzeBlock, bronzeBlock, null, null, null,
                null, null, bronzeBlock, bronzeBlock, bronzeBlock, bronzeBlock, bronzeBlock, null, null,
                null, bronzeBlock, bronzeBlock, bronzeBlock, bronzeBlock, bronzeBlock, bronzeBlock, bronzeBlock, null,
                bronzeBlock, bronzeBlock, bronzeBlock, bronzeBlock, bronzeBlock, bronzeBlock, bronzeBlock, bronzeBlock, bronzeBlock,
                null, bronzeBlock, bronzeBlock, bronzeBlock, bronzeBlock, bronzeBlock, bronzeBlock, bronzeBlock, null,
                null, null, bronzeBlock, bronzeBlock, bronzeBlock, bronzeBlock, bronzeBlock, null, null,
                null, null, null, bronzeBlock, bronzeBlock, bronzeBlock, null, null, null,
                null, null, null, null, bronzeBlock, null, null, null, null)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Bronze, 1))
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(steamGateAssemblerRecipes);
    }
}
