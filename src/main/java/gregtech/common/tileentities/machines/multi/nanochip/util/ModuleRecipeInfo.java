package gregtech.common.tileentities.machines.multi.nanochip.util;

import static gregtech.api.util.GTRecipeBuilder.TICKS;

public class ModuleRecipeInfo {

    public static final int MODULE_RECIPE_TIME = 20 * TICKS;

    public final int recipeProcessingTime;

    public ModuleRecipeInfo(int time) {
        this.recipeProcessingTime = time;
    }

    public final int getBaseParallel() {
        return MODULE_RECIPE_TIME / recipeProcessingTime;
    }

    // Some base speeds to keep things simple
    public static final ModuleRecipeInfo Fast = new ModuleRecipeInfo(5 * TICKS);
    public static final ModuleRecipeInfo Medium = new ModuleRecipeInfo(10 * TICKS);
    public static final ModuleRecipeInfo Slow = new ModuleRecipeInfo(20 * TICKS);
}
