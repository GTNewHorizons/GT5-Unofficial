package bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import bartworks.API.enums.BioDataEnum;
import gregtech.api.enums.ItemList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import bartworks.common.loaders.BioItemList;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;

public class LaserEngraver implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.emerald))
            .circuit(17)
            .itemOutputs(ItemList.EmptyPlasmid.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(laserEngraverRecipes);

    }
}
