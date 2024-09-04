package gtPlusPlus.xmod.ic2;

import gtPlusPlus.xmod.ic2.item.IC2Items;
import gtPlusPlus.xmod.ic2.recipe.RecipeIC2;

public class HandlerIC2 {

    public static void preInit() {
        IC2Items.register();
    }

    public static void postInit() {
        RecipeIC2.initRecipes();
    }
}
