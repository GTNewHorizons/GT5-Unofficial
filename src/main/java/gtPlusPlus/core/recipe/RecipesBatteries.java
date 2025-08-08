package gtPlusPlus.core.recipe;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class RecipesBatteries {

    public static void loadRecipes() {
        addBatteryRecipe(GregtechItemList.Battery_RE_EV_Sodium.get(1), ItemList.Battery_RE_HV_Sodium.get(1));
        addBatteryRecipe(GregtechItemList.Battery_RE_EV_Cadmium.get(1), ItemList.Battery_RE_HV_Cadmium.get(1));
        addBatteryRecipe(GregtechItemList.Battery_RE_EV_Lithium.get(1), ItemList.Battery_RE_HV_Lithium.get(1));
    }

    private static void addBatteryRecipe(ItemStack quad, ItemStack single) {
        GTModHandler.addCraftingRecipe(
            quad,
            new Object[] { "BWB", "CTC", "BWB", 'B', single, 'W',
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Gold, 1), 'C', "circuitAdvanced", 'T',
                ItemList.Transformer_EV_HV.get(1) });
    }
}
