package gtPlusPlus.recipes;

import static gtPlusPlus.core.util.minecraft.MaterialUtils.getMaterialName;

import java.util.Collection;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.ArrayUtils;

import advsolar.common.AdvancedSolarPanel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;
import gregtech.common.items.MetaGeneratedTool01;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.config.Configuration;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;

public class RecipeRemovals {

    public static void postInit() {
        if (Mods.AdvancedSolarPanel.isModLoaded()) {
            RecipeUtils.removeRecipeByOutput(ItemUtils.getSimpleStack(AdvancedSolarPanel.blockMolecularTransformer));
        }
    }
}
