package com.detrav.items.processing;

import com.detrav.items.DetravMetaGeneratedTool01;
import gregtech.api.enums.*;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import ic2.core.Ic2Items;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

/**
 * Created by Detrav on 11.12.2016.
 */
public class ProcessingDetravSmartPlunger implements IOreRecipeRegistrator {
        public ProcessingDetravSmartPlunger() {
        OrePrefixes.toolHeadHammer.add(this);
    }

    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        if ((aMaterial != Materials.Stone) && (aMaterial != Materials.Flint)) {
            if (aMaterial != Materials.Rubber)
                GT_ModHandler.addCraftingRecipe(
                        DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(4,1,aMaterial,aMaterial,null),
                        GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED,
                        new Object[]{"xRR", " CR", "S f",
                                Character.valueOf('S'), OrePrefixes.stick.get(aMaterial),
                                Character.valueOf('R'), OrePrefixes.plate.get(Materials.Rubber),
                                Character.valueOf('C'), Ic2Items.cell});
        }
    }
}
