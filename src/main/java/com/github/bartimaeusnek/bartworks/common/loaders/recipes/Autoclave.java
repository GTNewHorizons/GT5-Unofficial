package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.github.bartimaeusnek.bartworks.common.loaders.BioItemList;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;

public class Autoclave implements Runnable {

    @Override
    public void run() {
        Materials[] sterilizers = { Materials.Ammonia, Materials.Chlorine, Materials.Ethanol, Materials.Methanol };
        for (Materials used : sterilizers) {

            GT_Values.RA.stdBuilder().itemInputs(ItemList.Circuit_Parts_PetriDish.get(1L))
                    .itemOutputs(BioItemList.getPetriDish(null))
                    .fluidInputs(used.getGas(10L) != null ? used.getGas(8L) : used.getFluid(16L)).noFluidOutputs()
                    .duration(5 * SECONDS).eut(TierEU.RECIPE_LV).addTo(sAutoclaveRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Items.glass_bottle))
                    .itemOutputs(BioItemList.getDNASampleFlask(null))
                    .fluidInputs(used.getGas(10L) != null ? used.getGas(8L) : used.getFluid(16L)).noFluidOutputs()
                    .duration(5 * SECONDS).eut(TierEU.RECIPE_LV).addTo(sAutoclaveRecipes);

        }
    }
}
