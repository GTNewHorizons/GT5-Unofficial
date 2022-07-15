package goodgenerator.crossmod.nei;

import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.nei.GT_NEI_DefaultHandler;
import java.awt.*;
import net.minecraft.util.StatCollector;

public class NeutronActivatorHandler extends GT_NEI_DefaultHandler {

    public NeutronActivatorHandler(GT_Recipe.GT_Recipe_Map aRecipeMap) {
        super(aRecipeMap);
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(
                new Rectangle(65, 13, 36, 18), this.getOverlayIdentifier()));
        if (!NEI_Config.isAdded) {
            FMLInterModComms.sendRuntimeMessage(
                    GT_Values.GT,
                    "NEIPlugins",
                    "register-crafting-handler",
                    "gregtech@" + this.getRecipeName() + "@" + this.getOverlayIdentifier());
            GuiCraftingRecipe.craftinghandlers.add(this);
            GuiUsageRecipe.usagehandlers.add(this);
        }
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new NeutronActivatorHandler(this.mRecipeMap);
    }

    @Override
    public void drawExtras(int aRecipeIndex) {
        int tDuration = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mDuration;
        int minNKE = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mSpecialValue % 10000;
        int maxNKE = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mSpecialValue / 10000;
        drawText(
                10,
                73,
                this.trans("158", "Time: ")
                        + GT_Utility.formatNumbers((float) tDuration / 20.0)
                        + this.trans("161", " secs"),
                -16777216);
        drawText(10, 83, StatCollector.translateToLocal("value.neutron_activator.0"), -16777216);
        drawText(
                10,
                93,
                GT_Utility.formatNumbers(minNKE) + StatCollector.translateToLocal("value.neutron_activator.2"),
                -16777216);
        drawText(10, 103, StatCollector.translateToLocal("value.neutron_activator.1"), -16777216);
        drawText(
                10,
                113,
                GT_Utility.formatNumbers(maxNKE) + StatCollector.translateToLocal("value.neutron_activator.2"),
                -16777216);
    }
}
