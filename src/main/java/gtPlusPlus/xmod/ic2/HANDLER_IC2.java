package gtPlusPlus.xmod.ic2;

import gtPlusPlus.xmod.ic2.item.IC2_Items;
import gtPlusPlus.xmod.ic2.recipe.RECIPE_IC2;

public class HANDLER_IC2 {

    public static void preInit() {
        IC2_Items.register();
    }

    public static void postInit() {
        RECIPE_IC2.initRecipes();
    }
}
