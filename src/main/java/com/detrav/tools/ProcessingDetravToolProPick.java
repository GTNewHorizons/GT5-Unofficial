package com.detrav.tools;

import com.detrav.enums.DetravSimpleItems;
import com.detrav.items.DetravMetaGeneratedTool01;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import net.minecraft.item.ItemStack;

/**
 * Created by wital_000 on 18.03.2016.
 */
public class ProcessingDetravToolProPick implements gregtech.api.interfaces.IOreRecipeRegistrator {
    public ProcessingDetravToolProPick() {
        OrePrefixes.toolHeadPickaxe.add(this);
    }

    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        GT_ModHandler.
                addShapelessCraftingRecipe(
                        DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(0, 1, aMaterial, aMaterial.mHandleMaterial, null),
                        new Object[]{DetravSimpleItems.toolProPickHead.get(aMaterial), OrePrefixes.stick.get(aMaterial.mHandleMaterial)});
    }
}