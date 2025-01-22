package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.steamGateAssemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;

public class SteamGateAssemblerRecipes implements Runnable {

    @Override
    public void run() {
        ItemStack steamPiston = ItemList.Electric_Piston_LV.get(1);
        ItemStack steelBlock = GTOreDictUnificator.get(OrePrefixes.block, Materials.Steel, 1);
        ItemStack bronzeBlock = GTOreDictUnificator.get(OrePrefixes.block, Materials.Bronze, 1);
        ItemStack superdenseBronze = GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Bronze, 1);
        ItemStack superdenseSteel = GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Steel, 1);

        // Blank recipe for copy-paste convenience
        /*
         * GTValues.RA.stdBuilder()
         * .itemInputs(
         * null, null, null, null, null, null, null, null, null,
         * null, null, null, null, null, null, null, null, null,
         * null, null, null, null, null, null, null, null, null,
         * null, null, null, null, null, null, null, null, null,
         * null, null, null, null, null, null, null, null, null,
         * null, null, null, null, null, null, null, null, null,
         * null, null, null, null, null, null, null, null, null,
         * null, null, null, null, null, null, null, null, null,
         * null, null, null, null, null, null, null, null, null)
         * .itemOutputs(null)
         * .duration(20 * SECONDS)
         * .eut((int) TierEU.RECIPE_LV)
         * .addTo(steamGateAssemblerRecipes);
         */

        //spotless:off

        // Superdense Bronze Plate
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

        // Steamgate Iris Blade
        GTValues.RA.stdBuilder()
            .itemInputs(
                null, null, null, null, null, superdenseBronze, superdenseBronze, superdenseBronze, superdenseBronze,
                null, null, null, null, superdenseBronze, superdenseSteel, superdenseSteel, superdenseBronze, null,
                null, null, null, superdenseBronze, superdenseSteel, superdenseSteel, superdenseBronze, null, null,
                null, null, superdenseBronze, superdenseSteel, superdenseSteel, superdenseBronze, null, null, null,
                null, superdenseBronze, superdenseSteel, superdenseSteel, superdenseSteel, superdenseBronze, null, null, null,
                superdenseBronze, steamPiston, superdenseSteel, superdenseSteel, superdenseSteel, superdenseBronze, null, null, null,
                superdenseBronze, steamPiston, superdenseSteel, superdenseSteel, superdenseSteel, superdenseSteel, superdenseBronze, null, null,
                superdenseBronze, steelBlock, steamPiston, steamPiston, superdenseSteel, superdenseSteel, superdenseSteel, superdenseBronze, null,
                bronzeBlock, superdenseBronze, superdenseBronze, superdenseBronze, superdenseBronze, superdenseBronze, superdenseBronze, superdenseBronze, superdenseBronze)
            .itemOutputs(ItemList.Steamgate_Iris_Blade.get(1))
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(steamGateAssemblerRecipes);

        //spotless:on
    }
}
