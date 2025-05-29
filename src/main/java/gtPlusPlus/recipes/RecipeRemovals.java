package gtPlusPlus.recipes;

import advsolar.common.AdvancedSolarPanel;
import gregtech.api.enums.Mods;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import net.minecraft.item.ItemStack;

public class RecipeRemovals {

    public static void postInit() {
        if (Mods.AdvancedSolarPanel.isModLoaded()) {
            RecipeUtils.removeRecipeByOutput(new ItemStack(AdvancedSolarPanel.blockMolecularTransformer));
        }
    }
}
