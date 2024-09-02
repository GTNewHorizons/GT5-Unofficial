package gtPlusPlus.core.common.compat;

import net.minecraft.item.ItemStack;

import gtPlusPlus.core.recipe.RecipesTools;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class CompatExtraUtils {

    public static void OreDict() {
        RecipesTools.RECIPE_DivisionSigil = new ItemStack(ItemUtils.getItemFromFQRN("ExtraUtilities:divisionSigil"));
        run();
    }

    private static void run() {
        ItemUtils.getItemForOreDict("ExtraUtilities:bedrockiumIngot", "ingotBedrockium", "Bedrockium Ingot", 0);
    }
}
