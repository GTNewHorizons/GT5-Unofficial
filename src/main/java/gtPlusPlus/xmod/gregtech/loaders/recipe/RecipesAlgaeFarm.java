package gtPlusPlus.xmod.gregtech.loaders.recipe;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.item.chemistry.AgriculturalChem;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipesAlgaeFarm {

    public static GTRecipe getTieredRecipe(int aTier, ItemStack[] aItemInputs) {
        return generateBaseRecipe(aTier, isUsingCompost(aItemInputs, aTier));
    }

    private static boolean isUsingCompost(ItemStack[] aItemInputs, int aTier) {
        ItemStack aCompost = ItemUtils.getSimpleStack(AgriculturalChem.mCompost, 1);
        for (ItemStack i : aItemInputs) {
            if (GTUtility.areStacksEqual(aCompost, i)) {
                if (i.stackSize >= compostForTier(aTier)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int compostForTier(int aTier) {
        return aTier > 1 ? (int) Math.min(64, Math.pow(2, aTier - 1)) : 1;
    }

    private static GTRecipe generateBaseRecipe(int aTier, boolean isUsingCompost) {

        if (aTier < 0) return null; // Type Safety

        final ItemStack[] aInputs;
        if (isUsingCompost) {
            // Make it use 4 compost per tier if we have some available
            // Compost consumption maxes out at 1 stack per cycle
            ItemStack aCompost = ItemUtils.getSimpleStack(AgriculturalChem.mCompost, compostForTier(aTier));
            aInputs = new ItemStack[] { aCompost };
            // Boost Tier by one if using compost, so it gets a speed boost
            aTier++;
        } else {
            aInputs = new ItemStack[] {};
        }

        ItemStack[] aOutputs = getOutputsForTier(aTier);
        GTRecipe tRecipe = new GTRecipe(
            false,
            aInputs,
            aOutputs,
            null,
            new int[] {},
            new FluidStack[] { GTValues.NF },
            new FluidStack[] { GTValues.NF },
            getRecipeDuration(aTier),
            0,
            0);
        tRecipe.mSpecialValue = tRecipe.hashCode();
        return tRecipe;
    }

    private static final int[] aDurations = new int[] { 2000, 1800, 1600, 1400, 1200, 1000, 512, 256, 128, 64, 32, 16,
        8, 4, 2, 1 };
    private static final Random random = new XSTR();

    private static int getRecipeDuration(int aTier) {
        final float randFloat = random.nextFloat();
        float randMult;
        if (randFloat < 0.96237624) randMult = 1f;
        else if (randFloat < 0.9912871) randMult = 2f;
        else randMult = 3f;
        return (int) (aDurations[aTier] * randMult / 2);
    }

    private static ItemStack[] getOutputsForTier(int aTier) {
        ArrayList<ItemStack> outputList = new ArrayList<>();

        if (aTier >= 0) {
            outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mAlgaeBiosmass, 2));
            outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mAlgaeBiosmass, 4));
            if (MathUtils.randInt(0, 10) > 9) {
                outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 2));
            }
        }
        if (aTier >= 1) {
            outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mAlgaeBiosmass, 4));
            outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 2));
            if (MathUtils.randInt(0, 10) > 9) {
                outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 4));
            }
        }
        if (aTier >= 2) {
            outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 2));
            outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 3));
            if (MathUtils.randInt(0, 10) > 9) {
                outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 8));
            }
        }
        if (aTier >= 3) {
            outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 4));
            outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, 1));
            if (MathUtils.randInt(0, 10) > 9) {
                outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, 4));
            }
        }
        if (aTier >= 4) {
            outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, 2));
            outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, 3));
            if (MathUtils.randInt(0, 10) > 9) {
                outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownAlgaeBiosmass, 4));
            }
        }
        if (aTier >= 5) {
            outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, 4));
            outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownAlgaeBiosmass, 2));
            if (MathUtils.randInt(0, 10) > 9) {
                outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mRedAlgaeBiosmass, 4));
            }
        }
        if (aTier >= 6) {
            outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownAlgaeBiosmass, 4));
            outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mRedAlgaeBiosmass, 2));
            if (MathUtils.randInt(0, 10) > 9) {
                outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mRedAlgaeBiosmass, 8));
            }
            // Iterate a special loop at higher tiers to provide more Red/Gold Algae.
            for (int i = 0; i < 20; i++) {
                if (aTier >= (6 + i)) {
                    int aMulti = i + 1;
                    outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, aMulti * 4));
                    outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, aMulti * 3));
                    outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownAlgaeBiosmass, aMulti * 2));
                    outputList.add(ItemUtils.getSimpleStack(AgriculturalChem.mRedAlgaeBiosmass, aMulti));
                } else {
                    i = 20;
                }
            }
        }

        final ItemStack[] aOutputs = new ItemStack[outputList.size()];
        for (int i = 0; i < outputList.size(); i++) {
            aOutputs[i] = outputList.get(i);
        }
        return aOutputs;
    }
}
