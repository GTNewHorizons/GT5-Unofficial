package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.bioSynthesizerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.AO_DATA;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.util.recipe.AORecipeData;

public class BioSynthesizerRecipes implements Runnable {

    /**
     * This is an AO Unit - all recipes should use .metadata(AO_DATA, new AORecipeData(x, y, z)) or the multiblock
     * will not use AOs in its logic.
     * AORecipeData constructors, in order:
     * - requiredIntelligence: AO population intelligence required to run this recipe.
     * - requiredCount: Number of AOs that will be drained at the recipe start.
     * - dangerLevel: This is the percentage of AOs that will die while running the recipe. Use values from 0-100 only.
     */

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.beef, 1))
            .itemOutputs(ItemList.Circuit_Chip_Stemcell.get(1))
            .duration(15 * SECONDS)
            .eut(2)
            .metadata(AO_DATA, new AORecipeData(5, 500, 20))
            .addTo(bioSynthesizerRecipes);
    }
}
