package gtPlusPlus.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.*;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.*;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.nei.GT_NEI_DefaultHandler;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import java.util.*;
import net.minecraft.item.ItemStack;

public class GTPP_NEI_DefaultHandler extends GT_NEI_DefaultHandler {

    public GTPP_NEI_DefaultHandler(final GT_Recipe_Map tMap) {
        super(tMap);
        if (!NEI_GT_Config.sIsAdded) {
            FMLInterModComms.sendRuntimeMessage(
                    GT_Values.GT,
                    "NEIPlugins",
                    "register-crafting-handler",
                    "gregtechplusplus@" + this.getRecipeName() + "@" + this.getOverlayIdentifier());
            GuiCraftingRecipe.craftinghandlers.add(this);
            GuiUsageRecipe.usagehandlers.add(this);
        }
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new GTPP_NEI_DefaultHandler(this.mRecipeMap);
    }

    @Override
    public List<String> handleItemTooltip(
            GuiRecipe<?> gui, ItemStack aStack, List<String> currenttip, int aRecipeIndex) {
        super.handleItemTooltip(gui, aStack, currenttip, aRecipeIndex);
        CachedRecipe tObject = this.arecipes.get(aRecipeIndex);
        if (tObject instanceof CachedDefaultRecipe) {
            CachedDefaultRecipe tRecipe = (CachedDefaultRecipe) tObject;
            for (PositionedStack tStack : tRecipe.mOutputs) {
                // no-op
            }
            for (PositionedStack tStack : tRecipe.mInputs) {
                if (aStack == tStack.item) {
                    if (ItemUtils.isMillingBall(aStack)) {
                        currenttip.remove(GT_Utility.trans("151", "Does not get consumed in the process"));
                        currenttip.add("Does not always get consumed in the process");
                    }
                    if (ItemUtils.isCatalyst(aStack)) {
                        currenttip.remove(GT_Utility.trans("151", "Does not get consumed in the process"));
                        currenttip.add("Does not always get consumed in the process");
                        currenttip.add("Higher tier pipe casings allow this item to last longer");
                    }
                }
            }
        }

        return currenttip;
    }
}
