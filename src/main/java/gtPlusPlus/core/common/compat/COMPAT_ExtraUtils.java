package gtPlusPlus.core.common.compat;

import net.minecraft.item.ItemStack;

import gtPlusPlus.core.recipe.RECIPES_Tools;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class COMPAT_ExtraUtils {

    public static void OreDict() {
        RECIPES_Tools.RECIPE_DivisionSigil = new ItemStack(ItemUtils.getItemFromFQRN("ExtraUtilities:divisionSigil"));
        run();
    }

    private static void run() {
        ItemUtils.getItemForOreDict("ExtraUtilities:bedrockiumIngot", "ingotBedrockium", "Bedrockium Ingot", 0);
    }
}
