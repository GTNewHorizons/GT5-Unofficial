package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import static gregtech.api.enums.Mods.CropLoadCore;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sExtractorRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.github.bartimaeusnek.bartworks.common.loaders.BioItemList;
import com.github.bartimaeusnek.bartworks.util.BW_Util;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;

public class Extractor implements Runnable {

    @Override
    public void run() {
        List<ItemStack> oreCropVine = OreDictionary.getOres("cropVine", false);
        if (CropLoadCore.isModLoaded() && !oreCropVine.isEmpty()) {
            for (ItemStack stack : oreCropVine) {

                GT_Values.RA.stdBuilder().itemInputs(BW_Util.setStackSize(stack, 12))
                        .itemOutputs(BioItemList.getOther(1)).duration(25 * SECONDS).eut((int) TierEU.RECIPE_HV)
                        .addTo(sExtractorRecipes);

            }
        }

        GT_Values.RA.stdBuilder().itemInputs(ItemList.Circuit_Chip_Stemcell.get(1L))
                .itemOutputs(BioItemList.getOther(4)).duration(25 * SECONDS).eut((int) TierEU.RECIPE_LuV)
                .addTo(sExtractorRecipes);

    }
}
