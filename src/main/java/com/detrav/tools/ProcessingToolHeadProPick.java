package com.detrav.tools;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import net.minecraft.item.ItemStack;

/**
 * Created by wital_000 on 18.03.2016.
 */
public class ProcessingToolHeadProPick implements gregtech.api.interfaces.IOreRecipeRegistrator {
    public ProcessingToolHeadProPick() {
        OrePrefixes.toolHeadPickaxe.add(this);
    }

    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        /*GT_ModHandler.
                addShapelessCraftingRecipe(
                        Detrav_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(1, 1, aMaterial, aMaterial.mHandleMaterial, null),
                        new Object[]{aOreDictName, OrePrefixes.stick.get(aMaterial.mHandleMaterial),}});*/
    }
}
