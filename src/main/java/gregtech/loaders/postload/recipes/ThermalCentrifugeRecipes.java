package gregtech.loaders.postload.recipes;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.*;
import gregtech.api.util.GT_OreDictUnificator;

public class ThermalCentrifugeRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.stdBuilder().itemInputs(ItemList.SunnariumCell.get(1))
                .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sunnarium, 1),
                        new ItemStack(Items.glowstone_dust, 2),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1))
                .noFluidInputs().noFluidOutputs().duration(25 * SECONDS).eut(TierEU.RECIPE_LV)
                .addTo(sThermalCentrifugeRecipes);
    }
}
