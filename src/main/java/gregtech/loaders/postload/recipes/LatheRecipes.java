package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.util.GTOreDictUnificator;
import gtnhlanth.common.register.WerkstoffMaterialPool;

public class LatheRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("slabWood", 1))
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

        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("plankWood", 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L))
            .duration(10 * TICKS)
            .eut(8)
            .addTo(latheRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("logWood", 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Wood, 4L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L))
            .duration(8 * SECONDS)
            .eut(7)
            .addTo(latheRecipes);

        // From ProcessingSaplings
        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("treeSapling", 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Wood, 1L))
            .duration(16 * TICKS)
            .eut(8)
            .addTo(latheRecipes);
    }
}
