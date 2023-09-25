package gregtech.loaders.postload;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Recipe;

public class GT_ExtremeDieselFuelLoader implements Runnable {

    @Override
    public void run() {
        GT_Log.out.println("GT_Mod: Adding extreme diesel fuel.");
        int added = 0;
        for (GT_Recipe aRecipe : RecipeMap.sDieselFuels.mRecipeList) {
            if (aRecipe.mSpecialValue >= 1500) {
                added += 1;
                RecipeMap.sExtremeDieselFuels.add(aRecipe);
            }
        }
        GT_Log.out.println("GT_Mod: Added " + added + " kind(s) of extreme diesel fuel.");
    }
}
