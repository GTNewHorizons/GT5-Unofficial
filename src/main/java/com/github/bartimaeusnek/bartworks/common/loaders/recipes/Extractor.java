package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import static gregtech.api.enums.Mods.CropLoadCore;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.github.bartimaeusnek.bartworks.common.loaders.BioItemList;
import com.github.bartimaeusnek.bartworks.util.BW_Util;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;

public class Extractor implements Runnable {

    @Override
    public void run() {
        List<ItemStack> oreCropVine = OreDictionary.getOres("cropVine", false);
        if (CropLoadCore.isModLoaded() && !oreCropVine.isEmpty()) {
            for (ItemStack stack : oreCropVine) {
                GT_Values.RA.addExtractorRecipe(
                        BW_Util.setStackSize(stack, 12),
                        BioItemList.getOther(1),
                        500,
                        BW_Util.getMachineVoltageFromTier(3));
            }
        }

        GT_Values.RA.addExtractorRecipe(
                ItemList.Circuit_Chip_Stemcell.get(1L),
                BioItemList.getOther(4),
                500,
                BW_Util.getMachineVoltageFromTier(6));
    }
}
