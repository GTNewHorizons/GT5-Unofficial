package gtPlusPlus.xmod.forestry;

import static gregtech.api.enums.Mods.Forestry;

import gtPlusPlus.xmod.forestry.bees.items.FRItemRegistry;
import gtPlusPlus.xmod.forestry.bees.recipe.FRGregTechRecipes;
import gtPlusPlus.xmod.forestry.bees.registry.GTPP_Bees;

public class ForestryHandler {

    public static void preInit() {
        if (Forestry.isModLoaded()) {
            FRItemRegistry.Register();
        }
    }

    public static void postInit() {
        if (Forestry.isModLoaded()) {
            FRGregTechRecipes.registerItems();
            new GTPP_Bees();
        }
    }

}
