package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.github.bartimaeusnek.bartworks.common.loaders.BioItemList;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_Utility;

public class LaserEngraver implements Runnable {

    @Override
    public void run() {

        GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Items.emerald), GT_Utility.getIntegratedCircuit(17))
                .itemOutputs(BioItemList.getPlasmidCell(null)).duration(5 * SECONDS).eut(TierEU.RECIPE_LV)
                .addTo(sLaserEngraverRecipes);

    }
}
