package gtnhlanth.common.tileentity.recipe.beamline;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gtnhlanth.common.register.LanthItemList;

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

    @Override
    public ItemStack getRepresentativeInput(int aIndex) {

        ArrayList<ItemStack> mInputsWithParticle = new ArrayList<>();

        ItemStack particleStack = new ItemStack(LanthItemList.PARTICLE_ITEM);
        Items.ender_pearl.setDamage(particleStack, this.particleId);

        mInputsWithParticle.add(particleStack);
        mInputsWithParticle.addAll(Arrays.asList(mInputs));

        ItemStack[] mInputsWithParticleArray = mInputsWithParticle.toArray(new ItemStack[0]);

        if (aIndex < 0 || aIndex >= mInputsWithParticleArray.length) return null;
        return GTUtility.copyOrNull(mInputsWithParticleArray[aIndex]);
    }

}
