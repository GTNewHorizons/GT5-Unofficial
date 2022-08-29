package com.github.bartimaeusnek.bartworks.neiHandler;

import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import com.github.bartimaeusnek.bartworks.MainMod;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.util.GT_Recipe;
import gregtech.nei.GT_NEI_DefaultHandler;

public class BW_NEI_HTGRHandler extends GT_NEI_DefaultHandler {
    public BW_NEI_HTGRHandler(GT_Recipe.GT_Recipe_Map aRecipeMap) {
        super(aRecipeMap);
        if (!NEI_BW_Config.sIsAdded) {
            FMLInterModComms.sendRuntimeMessage(
                    MainMod.instance,
                    "NEIPlugins",
                    "register-crafting-handler",
                    "bartworks@" + this.getRecipeName() + "@" + this.getOverlayIdentifier());
            GuiCraftingRecipe.craftinghandlers.add(this);
            GuiUsageRecipe.usagehandlers.add(this);
        }
    }
}
