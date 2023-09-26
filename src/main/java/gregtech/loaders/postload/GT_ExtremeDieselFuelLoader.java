package gregtech.loaders.postload;

import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Recipe;

public class GT_ExtremeDieselFuelLoader implements Runnable {

    @Override
    public void run() {
        GT_Log.out.println("GT_Mod: Adding extreme diesel fuel.");
        int added = 0;
        for (GT_Recipe aRecipe : GT_Recipe.GT_Recipe_Map.sDieselFuels.mRecipeList) {
            if (aRecipe.mSpecialValue < 1500){
                continue;
            }

            added += 1;
            GT_Recipe.GT_Recipe_Map.sExtremeDieselFuels.add(aRecipe);
        }
        GT_Log.out.println("GT_Mod: Added " + added + " kind(s) of extreme diesel fuel.");
    }
}
