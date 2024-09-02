package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class RecipeGenFluidCanning implements Runnable {

    public static void init() {
        FluidCanningRunnableHandler x = new FluidCanningRunnableHandler();
        x.run();
    }

    private static class FluidCanningRunnableHandler implements RunnableWithInfo<String> {

        @Override
        public void run() {
            mHasRun = true;
            for (RecipeGenFluidCanning aRecipe : mCache) {
                aRecipe.run();
            }
        }

        @Override
        public String getInfoData() {
            return "Fluid Canning Recipes";
        }
    }

    private static boolean mHasRun = false;

    private static HashSet<RecipeGenFluidCanning> mCache = new HashSet<>();

    private static void addRunnableToRecipeCache(RecipeGenFluidCanning r) {
        if (mHasRun) {
            GTPPCore.crash();
        }
        mCache.add(r);
    }

    protected boolean disableOptional;

    private final GTRecipe recipe;
    private final boolean isValid;

    public boolean valid() {
        return isValid;
    }

    // Alternative Constructor
    public RecipeGenFluidCanning(boolean aExtracting, ItemStack aEmpty, ItemStack aFull, FluidStack aFluidIn,
        FluidStack aFluidOut, Integer aDuration, Integer aEUt) {
        ItemStack aInput;
        ItemStack aOutput;
        FluidStack aFluidInput;
        FluidStack aFluidOutput;

        // Safety check on the duration
        if (aDuration == null || aDuration <= 0) {
            aDuration = (aFluidIn != null) ? (aFluidIn.amount / 62)
                : ((aFluidOut != null) ? (aFluidOut.amount / 62) : 10);
        }

        // Safety check on the EU
        if (aEUt == null || aEUt <= 0) {
            if (aExtracting) {
                aEUt = 2;
            } else {
                aEUt = 1;
            }
        }

        // Set Item stacks correctly, invert if extraction recipe.
        if (aExtracting) {
            aInput = aFull;
            aOutput = aEmpty;
            aFluidInput = GTValues.NF;
            aFluidOutput = aFluidIn;
        } else {
            aInput = aEmpty;
            aOutput = aFull;
            aFluidInput = aFluidIn;
            aFluidOutput = aFluidOut != null ? aFluidOut : GTValues.NF;
        }

        // Check validity

        GTRecipe aRecipe = new GTRecipe(
            true,
            new ItemStack[] { aInput },
            new ItemStack[] { aOutput },
            null,
            new int[] { 10000 },
            new FluidStack[] { aFluidInput },
            new FluidStack[] { aFluidOutput },
            aDuration,
            aEUt,
            0);

        // Check Valid
        boolean aTempValidityCheck = false;
        // Logger.INFO("Validity Check.");
        if (aExtracting) {
            Logger.INFO("Extracting.");
            if (aInput != null && aFluidOutput != null) {
                // Logger.INFO("Pass.");
                aTempValidityCheck = true;
            }
        } else {
            // Logger.INFO("Canning.");
            if (aInput != null && aOutput != null && (aFluidInput != null || aFluidOutput != null)) {
                // Logger.INFO("Pass.");
                aTempValidityCheck = true;
            }
        }

        if (aTempValidityCheck) {
            // Valid Recipe
            recipe = aRecipe;
            disableOptional = aExtracting;
            isValid = true;
            addRunnableToRecipeCache(this);
        } else {
            // Logger.INFO("Failed Validity Check.");
            isValid = false;
            disableOptional = aExtracting;
            aRecipe.mEnabled = false;
            aRecipe.mHidden = true;
            recipe = null;
        }
    }

    @Override
    public void run() {
        Logger.INFO("Processing Recipe with Hash: " + recipe.hashCode());
        generateRecipes();
    }

    private void generateRecipes() {
        if (isValid && recipe != null) {
            if (this.disableOptional) {
                addFluidExtractionRecipe(recipe);
            } else {
                addFluidCannerRecipe(recipe);
            }
        }
    }

    private void addFluidExtractionRecipe(GTRecipe aRecipe) {
        GTPPCore.crash();
        Logger.INFO(
            "[FE-Debug] " + aRecipe.mFluidOutputs[0].amount
                + "L of "
                + aRecipe.mFluidOutputs[0].getLocalizedName()
                + " fluid extractor from 1 "
                + aRecipe.mInputs[0].getDisplayName()
                + " - Success. Time: "
                + aRecipe.mDuration
                + ", Voltage: "
                + aRecipe.mEUt);
        int aCount1 = getMapSize(RecipeMaps.fluidExtractionRecipes);
        int aCount2 = aCount1;
        RecipeMaps.fluidExtractionRecipes.addRecipe(aRecipe);
        aCount1 = getMapSize(RecipeMaps.fluidExtractionRecipes);
        if (aCount1 <= aCount2) {
            Logger.INFO(
                "[ERROR] Failed adding Extraction recipe for " + ItemUtils.getArrayStackNames(aRecipe.mInputs)
                    + ", "
                    + ItemUtils.getArrayStackNames(aRecipe.mOutputs)
                    + ", "
                    + ItemUtils.getArrayStackNames(aRecipe.mFluidInputs)
                    + ", "
                    + ItemUtils.getArrayStackNames(aRecipe.mFluidOutputs));
            dumpStack();
        }
    }

    private void addFluidCannerRecipe(GTRecipe aRecipe) {
        boolean result;
        int aCount1 = getMapSize(RecipeMaps.fluidCannerRecipes);
        int aCount2 = aCount1;
        RecipeMaps.fluidCannerRecipes.addRecipe(aRecipe);
        aCount1 = getMapSize(RecipeMaps.fluidCannerRecipes);
        if (aCount1 <= aCount2) {
            Logger.INFO(
                "[ERROR] Failed adding Canning recipe for " + ItemUtils.getArrayStackNames(aRecipe.mInputs)
                    + ", "
                    + ItemUtils.getArrayStackNames(aRecipe.mOutputs)
                    + ", "
                    + ItemUtils.getArrayStackNames(aRecipe.mFluidInputs)
                    + ", "
                    + ItemUtils.getArrayStackNames(aRecipe.mFluidOutputs));
            dumpStack();
        }
    }

    private void dumpStack() {
        int parents = 2;
        for (int i = 0; i < 6; i++) {
            Logger.INFO(
                (disableOptional ? "EXTRACTING" : "CANNING") + " DEBUG | "
                    + (i == 0 ? "Called from: " : "Parent: ")
                    + ReflectionUtils.getMethodName(i + parents));
        }
    }

    private int getMapSize(RecipeMap<?> aMap) {
        return aMap.getAllRecipes()
            .size();
    }
}
