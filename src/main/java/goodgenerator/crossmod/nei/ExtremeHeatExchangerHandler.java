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
import net.minecraftforge.fluids.FluidStack;

public class ExtremeHeatExchangerHandler extends GT_NEI_DefaultHandler {

    public ExtremeHeatExchangerHandler(GT_Recipe.GT_Recipe_Map aRecipeMap) {
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
        return new ExtremeHeatExchangerHandler(this.mRecipeMap);
    }

    @Override
    public void drawExtras(int aRecipeIndex) {
        FluidStack[] Inputs = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mFluidInputs;
        FluidStack[] Outputs = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mFluidOutputs;
        int Threshold = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mSpecialValue;
        drawText(
                10,
                73,
                StatCollector.translateToLocal("value.extreme_heat_exchanger.0") + " "
                        + GT_Utility.formatNumbers(Inputs[0].amount) + " L/s",
                -16777216);
        drawText(10, 83, StatCollector.translateToLocal("value.extreme_heat_exchanger.1"), -16777216);
        drawText(10, 93, GT_Utility.formatNumbers(Outputs[0].amount / 160) + " L/s", -16777216);
        drawText(10, 103, StatCollector.translateToLocal("value.extreme_heat_exchanger.2"), -16777216);
        drawText(10, 113, GT_Utility.formatNumbers(Outputs[1].amount / 160) + " L/s", -16777216);
        drawText(
                10,
                123,
                StatCollector.translateToLocal("value.extreme_heat_exchanger.4") + " " + Threshold + " L/s",
                -16777216);
    }
}
