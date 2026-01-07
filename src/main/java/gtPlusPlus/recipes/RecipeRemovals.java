package gtPlusPlus.recipes;

import net.minecraft.item.ItemStack;

import advsolar.common.AdvancedSolarPanel;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;

public class RecipeRemovals {

    public static void postInit() {
        if (Mods.AdvancedSolarPanel.isModLoaded()) {
            GTModHandler.removeRecipeByOutput(new ItemStack(AdvancedSolarPanel.blockMolecularTransformer));
        }
    }
}
