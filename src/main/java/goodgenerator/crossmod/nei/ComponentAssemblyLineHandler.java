package goodgenerator.crossmod.nei;

import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Recipe;
import gregtech.nei.GT_NEI_DefaultHandler;
import java.awt.*;

public class ComponentAssemblyLineHandler extends GT_NEI_DefaultHandler {
    public ComponentAssemblyLineHandler(GT_Recipe.GT_Recipe_Map aRecipeMap) {

        super(aRecipeMap);
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
        return new ComponentAssemblyLineHandler(this.mRecipeMap);
    }

    @Override
    protected String getSpecialInfo(int specialValue) {
        return this.mRecipeMap.mNEISpecialValuePre + GT_Values.VN[specialValue];
    }
}
