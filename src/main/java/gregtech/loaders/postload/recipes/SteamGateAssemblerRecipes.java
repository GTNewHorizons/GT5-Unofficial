package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.steamGateAssemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;

public class SteamGateAssemblerRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.bed, 1))
            .itemOutputs(new ItemStack(Items.leather_boots, 1))
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(steamGateAssemblerRecipes);
    }
}
