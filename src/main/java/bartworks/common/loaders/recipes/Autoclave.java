package bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import bartworks.common.loaders.BioItemList;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;

public class Autoclave implements Runnable {

    @Override
    public void run() {
        Materials[] sterilizers = { Materials.Ammonia, Materials.Chlorine, Materials.Ethanol, Materials.Methanol };
        for (Materials used : sterilizers) {

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Circuit_Parts_PetriDish.get(1L))
                .itemOutputs(BioItemList.getPetriDish(null))
                .fluidInputs(used.getGas(10L) != null ? used.getGas(8L) : used.getFluid(16L))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(autoclaveRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Items.glass_bottle))
                .itemOutputs(BioItemList.getDNASampleFlask(null))
                .fluidInputs(used.getGas(10L) != null ? used.getGas(8L) : used.getFluid(16L))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(autoclaveRecipes);

        }
    }
}
