package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gtnhlanth.common.register.WerkstoffMaterialPool;

public class LatheRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.wooden_slab, 1, WILDCARD))
            .itemOutputs(
                new ItemStack(Items.bowl, 1),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Wood, 1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(8)
            .addTo(latheRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Forestry.ID, "slabs", 1L, WILDCARD))
            .itemOutputs(
                new ItemStack(Items.bowl, 1),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Wood, 1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(8)
            .addTo(latheRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.LanthanumHexaboride.get(OrePrefixes.gemFlawless, 1))
            .itemOutputs(
                WerkstoffMaterialPool.LanthanumHexaboride.get(OrePrefixes.stickLong, 1),
                WerkstoffMaterialPool.LanthanumHexaboride.get(OrePrefixes.dust, 1))
            .duration((100 * SECONDS))
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(latheRecipes);
    }
}
