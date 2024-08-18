package gtPlusPlus.xmod.forestry;

import static gregtech.api.enums.Mods.Forestry;

import gtPlusPlus.xmod.forestry.bees.items.FR_ItemRegistry;
import gtPlusPlus.xmod.forestry.bees.recipe.FR_Gregtech_Recipes;
import gtPlusPlus.xmod.forestry.bees.registry.GTPP_Bees;

public class HANDLER_FR {

    public static void preInit() {
        if (Forestry.isModLoaded()) {
            FR_ItemRegistry.Register();
        }
    }

    public static void postInit() {
        if (Forestry.isModLoaded()) {
            FR_Gregtech_Recipes.registerItems();
            new GTPP_Bees();
        }
    }

}
