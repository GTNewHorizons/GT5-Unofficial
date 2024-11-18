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
