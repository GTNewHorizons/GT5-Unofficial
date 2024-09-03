package gtnhlanth.common.tileentity.recipe.beamline;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gtnhlanth.common.register.LanthItemList;

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

    @Override
    public ItemStack getRepresentativeOutput(int aIndex) {

        ArrayList<ItemStack> mOutputsWithParticle = new ArrayList<>();

        ItemStack particleStack = new ItemStack(LanthItemList.PARTICLE_ITEM);

        Items.ender_pearl.setDamage(particleStack, this.particleId);

        mOutputsWithParticle.addAll(Arrays.asList(mOutputs));
        mOutputsWithParticle.add(particleStack);

        ItemStack[] mOutputsWithParticleArray = mOutputsWithParticle.toArray(new ItemStack[0]);

        if (aIndex < 0 || aIndex >= mOutputsWithParticleArray.length) return null;
        return GTUtility.copyOrNull(mOutputsWithParticleArray[aIndex]);
    }
}
