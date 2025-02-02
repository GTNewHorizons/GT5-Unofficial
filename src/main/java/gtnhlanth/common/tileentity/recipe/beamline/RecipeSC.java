package gtnhlanth.common.tileentity.recipe.beamline;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GTRecipe;

public class RecipeSC extends GTRecipe {

    public int particleId;
    public int rate;
    public float maxEnergy;
    public float focus;
    public float energyRatio;

    public RecipeSC(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems, int[] aChances,
        FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int particleId, int rate,
        float maxEnergy, float focus, float energyRatio) {

        super(aOptimize, aInputs, aOutputs, null, aChances, null, null, aDuration, aEUt, 0);

        this.particleId = particleId;
        this.rate = rate;
        this.maxEnergy = maxEnergy;
        this.focus = focus;
        this.energyRatio = energyRatio;
    }
}
