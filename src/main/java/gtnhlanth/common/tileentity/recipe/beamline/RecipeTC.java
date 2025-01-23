package gtnhlanth.common.tileentity.recipe.beamline;

import net.minecraft.item.ItemStack;

import gregtech.api.util.GTRecipe;

public class RecipeTC extends GTRecipe {

    public int particleId;
    public int amount;

    public float minEnergy;
    public float maxEnergy;

    public float minFocus;
    public float energyRatio;

    public ItemStack focusItem;

    public RecipeTC(boolean aOptimize, ItemStack aInput, ItemStack aOutput, ItemStack aFocusItem, int particleId,
        int amount, float minEnergy, float maxEnergy, float minFocus, float energyRatio, int aEUt) {

        super(
            aOptimize,
            new ItemStack[] { aFocusItem, aInput },
            new ItemStack[] { aOutput },
            null,
            null,
            null,
            null,
            1,
            aEUt,
            0);

        this.particleId = particleId;
        this.amount = amount;

        this.minEnergy = minEnergy;
        this.maxEnergy = maxEnergy;

        this.minFocus = minFocus;

        this.energyRatio = energyRatio;

        this.focusItem = aFocusItem;
    }
}
