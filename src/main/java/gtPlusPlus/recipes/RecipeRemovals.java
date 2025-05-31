package gtPlusPlus.recipes;

import net.minecraft.item.ItemStack;

import advsolar.common.AdvancedSolarPanel;
import gregtech.api.enums.Mods;
import gtPlusPlus.core.util.minecraft.RecipeUtils;

public class RecipeRemovals {

    public static void postInit() {
        if (Mods.AdvancedSolarPanel.isModLoaded()) {
            RecipeUtils.removeRecipeByOutput(new ItemStack(AdvancedSolarPanel.blockMolecularTransformer));
        }
    }
}
