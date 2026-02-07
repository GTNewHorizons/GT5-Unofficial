package gregtech.common.tileentities.machines.multi.nanochip.util;

import gregtech.api.enums.TierEU;

public class ModuleRecipeInfo {

    public final long recipeEUt;

    public ModuleRecipeInfo(long recipeEUt) {
        this.recipeEUt = recipeEUt;
    }

    // Some base speeds to keep things simple
    public static final ModuleRecipeInfo ExtremeTier = new ModuleRecipeInfo(TierEU.RECIPE_UIV);
    public static final ModuleRecipeInfo HighTier = new ModuleRecipeInfo(TierEU.RECIPE_UEV);
    public static final ModuleRecipeInfo MediumTier = new ModuleRecipeInfo(TierEU.RECIPE_UHV);
    public static final ModuleRecipeInfo LowTier = new ModuleRecipeInfo(TierEU.RECIPE_UV);
}
