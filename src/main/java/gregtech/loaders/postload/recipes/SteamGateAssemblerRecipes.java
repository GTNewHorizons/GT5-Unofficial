package gregtech.loaders.postload.recipes;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.steamGateAssemblerRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

public class SteamGateAssemblerRecipes implements Runnable {
    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.bed,1))
            .itemOutputs(new ItemStack(Items.leather_boots,1))
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(steamGateAssemblerRecipes);
    }
}
