package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;

public class LatheRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.wooden_slab, 1, GTValues.W))
            .itemOutputs(
                new ItemStack(Items.bowl, 1),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Wood, 1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(8)
            .addTo(latheRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Forestry.ID, "slabs", 1L, GTValues.W))
            .itemOutputs(
                new ItemStack(Items.bowl, 1),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Wood, 1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(8)
            .addTo(latheRecipes);
    }
}
