package gtPlusPlus.xmod.gregtech.loaders.recipe;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.util.GTRecipe;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.WeightedCollection;
import gtPlusPlus.core.item.chemistry.AgriculturalChem;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeLoaderAlgaeFarm {

    private static final HashMap<Integer, AutoMap<GTRecipe>> mRecipeCache = new HashMap<>();
    private static final HashMap<Integer, AutoMap<GTRecipe>> mRecipeCompostCache = new HashMap<>();

    public static void generateRecipes() {
        for (int i = 0; i < 15; i++) {
            getTieredRecipeFromCache(i, false);
        }
        for (int i = 0; i < 15; i++) {
            getTieredRecipeFromCache(i, true);
        }
    }

    public static GTRecipe getTieredRecipeFromCache(int aTier, boolean aCompost) {
        HashMap<Integer, AutoMap<GTRecipe>> aMap = aCompost ? mRecipeCompostCache : mRecipeCache;
        String aComp = aCompost ? "(Compost)" : "";

        AutoMap<GTRecipe> aTemp = aMap.get(aTier);
        if (aTemp == null || aTemp.isEmpty()) {
            aTemp = new AutoMap<>();
            aMap.put(aTier, aTemp);
            Logger.INFO("Tier " + aTier + aComp + " had no recipes, initialising new map.");
        }
        if (aTemp.size() < 500) {
            Logger
                .INFO("Tier " + aTier + aComp + " has less than 500 recipes, generating " + (500 - aTemp.size()) + ".");
            for (int i = aTemp.size(); i < 500; i++) {
                aTemp.put(generateBaseRecipe(aCompost, aTier));
            }
        }
        int aIndex = MathUtils.randInt(0, aTemp.isEmpty() ? 1 : aTemp.size());
        Logger.INFO("Using recipe with index of " + aIndex + ". " + aComp);
        return aTemp.get(aIndex);
    }

    public static int compostForTier(int aTier) {
        return aTier > 1 ? (int) Math.min(64, Math.pow(2, aTier - 1)) : 1;
    }

    private static GTRecipe generateBaseRecipe(boolean aUsingCompost, int aTier) {

        // Type Safety
        if (aTier < 0) {
            return null;
        }

        WeightedCollection<Float> aOutputTimeMulti = new WeightedCollection<>();
        for (int i = 100; i > 0; i--) {
            float aValue = 0;
            if (i < 10) {
                aValue = 3f;
            } else if (i < 20) {
                aValue = 2f;
            } else {
                aValue = 1f;
            }
            aOutputTimeMulti.put(i, aValue);
        }

        final int[] aDurations = new int[] { 2000, 1800, 1600, 1400, 1200, 1000, 512, 256, 128, 64, 32, 16, 8, 4, 2,
            1 };

        ItemStack[] aInputs = new ItemStack[] {};

        if (aUsingCompost) {
            // Make it use 4 compost per tier if we have some available
            // Compost consumption maxes out at 1 stack per cycle
            ItemStack aCompost = ItemUtils.getSimpleStack(AgriculturalChem.mCompost, compostForTier(aTier));
            aInputs = new ItemStack[] { aCompost };
            // Boost Tier by one if using compost so it gets a speed boost
            aTier++;
        }

        // We set these elsewhere
        ItemStack[] aOutputs = getOutputsForTier(aTier);

        GTRecipe tRecipe = new GTRecipe(
            false,
            aInputs,
            aOutputs,
            (Object) null,
            new int[] {},
            new FluidStack[] { GTValues.NF },
            new FluidStack[] { GTValues.NF },
            (int) (aDurations[aTier] * aOutputTimeMulti.get() / 2), // Time
            0,
            0);

        tRecipe.mSpecialValue = tRecipe.hashCode();

        return tRecipe;
    }

    private static ItemStack[] getOutputsForTier(int aTier) {

        // Create an Automap to dump contents into
        AutoMap<ItemStack> aOutputMap = new AutoMap<>();

        // Add loot relevant to tier and also add any from lower tiers.

        if (aTier >= 0) {
            aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mAlgaeBiosmass, 2));
            aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mAlgaeBiosmass, 4));
            if (MathUtils.randInt(0, 10) > 9) {
                aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 2));
            }
        }

        if (aTier >= 1) {
            aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mAlgaeBiosmass, 4));
            aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 2));
            if (MathUtils.randInt(0, 10) > 9) {
                aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 4));
            }
        }
        if (aTier >= 2) {
            aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 2));
            aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 3));
            if (MathUtils.randInt(0, 10) > 9) {
                aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 8));
            }
        }
        if (aTier >= 3) {
            aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 4));
            aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, 1));
            if (MathUtils.randInt(0, 10) > 9) {
                aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, 4));
            }
        }
        if (aTier >= 4) {
            aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, 2));
            aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, 3));
            if (MathUtils.randInt(0, 10) > 9) {
                aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownAlgaeBiosmass, 4));
            }
        }
        if (aTier >= 5) {
            aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, 4));
            aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownAlgaeBiosmass, 2));
            if (MathUtils.randInt(0, 10) > 9) {
                aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mRedAlgaeBiosmass, 4));
            }
        }
        // Tier 6 is Highest for outputs
        if (aTier >= 6) {
            aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownAlgaeBiosmass, 4));
            aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mRedAlgaeBiosmass, 2));
            if (MathUtils.randInt(0, 10) > 9) {
                aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mRedAlgaeBiosmass, 8));
            }
        }

        // Iterate a special loop at higher tiers to provide more Red/Gold Algae.
        for (int i2 = 0; i2 < 20; i2++) {
            if (aTier >= (6 + i2)) {
                int aMulti = i2 + 1;
                aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, aMulti * 4));
                aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, aMulti * 3));
                aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownAlgaeBiosmass, aMulti * 2));
                aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mRedAlgaeBiosmass, aMulti));
            } else {
                i2 = 20;
            }
        }

        // Map the AutoMap contents to an Itemstack Array.
        ItemStack[] aOutputs = new ItemStack[aOutputMap.size()];
        for (int i = 0; i < aOutputMap.size(); i++) {
            aOutputs[i] = aOutputMap.get(i);
        }

        // Return filled ItemStack Array.
        return aOutputs;
    }
}
