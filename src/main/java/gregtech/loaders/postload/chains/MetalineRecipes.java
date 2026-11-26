package gregtech.loaders.postload.chains;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gtPlusPlus.core.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.recipe.RecipeMaps.metalineRecipes;

public class MetalineRecipes {
    public static void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Iron.getDust(1),
                Materials.Gold.getDust(1))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .itemOutputs(Materials.Steel.getIngots(2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(metalineRecipes);
    }
}
