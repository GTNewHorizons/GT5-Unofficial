package com.github.technus.tectech.nei;

import static com.github.technus.tectech.Reference.MODID;

import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Recipe;
import gregtech.nei.GT_NEI_DefaultHandler;

public class TT_NEI_EyeOfHarmonyHandler extends GT_NEI_DefaultHandler {

    public TT_NEI_EyeOfHarmonyHandler(final GT_Recipe.GT_Recipe_Map tMap) {
        super(tMap);
        if (!NEI_TT_Config.sIsAdded) {

            FMLInterModComms.sendRuntimeMessage(
                    GT_Values.GT,
                    "NEIPlugins",
                    "register-crafting-handler",
                    MODID + "@" + this.getRecipeName() + "@" + this.getOverlayIdentifier());
            GuiCraftingRecipe.craftinghandlers.add(this);
            GuiUsageRecipe.usagehandlers.add(this);
        }
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new TT_NEI_EyeOfHarmonyHandler(this.mRecipeMap);
    }
}
