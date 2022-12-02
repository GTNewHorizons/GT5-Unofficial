package com.github.bartimaeusnek.bartworks.neiHandler;

import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.github.bartimaeusnek.bartworks.MainMod;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.util.GT_Recipe;
import gregtech.nei.GT_NEI_DefaultHandler;
import net.minecraft.util.StatCollector;

public class BW_NEI_RadHatchHandler extends GT_NEI_DefaultHandler {

    public BW_NEI_RadHatchHandler(GT_Recipe.GT_Recipe_Map aRecipeMap) {
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

    @Override
    public TemplateRecipeHandler newInstance() {
        return new BW_NEI_RadHatchHandler(this.mRecipeMap);
    }

    @Override
    public void drawExtras(int aRecipeIndex) {
        GT_Recipe recipe = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe;
        int radioLevel = recipe.mEUt;
        int amount = recipe.mDuration;
        long time = recipe.mSpecialValue;
        int y = getDescriptionYOffset();
        drawText(10, y, StatCollector.translateToLocalFormatted("BW.NEI.display.radhatch.0", radioLevel), -16777216);
        y += 10;
        drawText(10, y, StatCollector.translateToLocalFormatted("BW.NEI.display.radhatch.1", amount), -16777216);
        y += 10;
        drawText(
                10,
                y,
                StatCollector.translateToLocalFormatted("BW.NEI.display.radhatch.2", time * amount / 20.0),
                -16777216);
    }
}
