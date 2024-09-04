package bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import bartworks.common.loaders.BioItemList;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTUtility;

public class LaserEngraver implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.emerald), GTUtility.getIntegratedCircuit(17))
            .itemOutputs(BioItemList.getPlasmidCell(null))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(laserEngraverRecipes);

    }
}
