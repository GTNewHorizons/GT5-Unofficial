package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class RecipeGen_FluidCanning implements Runnable {

    public static void init() {
        FluidCanningRunnableHandler x = new FluidCanningRunnableHandler();
        x.run();
    }

    private static class FluidCanningRunnableHandler implements RunnableWithInfo<String> {

        @Override
        public void run() {
            mHasRun = true;
            for (RecipeGen_FluidCanning aRecipe : mCache) {
                aRecipe.run();
            }
        }

        @Override
        public String getInfoData() {
            return "Fluid Canning Recipes";
        }
    }

    private static boolean mHasRun = false;

    private static HashSet<RecipeGen_FluidCanning> mCache = new HashSet<>();

    private static void addRunnableToRecipeCache(RecipeGen_FluidCanning r) {
        if (mHasRun) {
            CORE.crash();
        }
        mCache.add(r);
    }

    protected boolean disableOptional;

    private final GT_Recipe recipe;
    private final boolean isValid;

    public boolean valid() {
        return isValid;
    }

    public RecipeGen_FluidCanning(boolean aExtracting, ItemStack aEmpty, ItemStack aFull, FluidStack aFluid) {
        this(aExtracting, aEmpty, aFull, aFluid, GT_Values.NF, null, null);
    }

    public RecipeGen_FluidCanning(boolean aExtracting, ItemStack aEmpty, ItemStack aFull, FluidStack aFluidIn,
            FluidStack aFluidOut) {
        this(aExtracting, aEmpty, aFull, aFluidIn, aFluidOut, null, null);
    }

    public RecipeGen_FluidCanning(boolean aExtracting, ItemStack aEmpty, ItemStack aFull, FluidStack aFluid,
            Integer aDuration, Integer aEUt) {
        this(aExtracting, aEmpty, aFull, aFluid, GT_Values.NF, aDuration, aEUt);
    }

    // Alternative Constructor
    public RecipeGen_FluidCanning(boolean aExtracting, ItemStack aEmpty, ItemStack aFull, FluidStack aFluidIn,
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
            aFluidInput = GT_Values.NF;
            aFluidOutput = aFluidIn;
        } else {
            aInput = aEmpty;
            aOutput = aFull;
            aFluidInput = aFluidIn;
            aFluidOutput = aFluidOut != null ? aFluidOut : GT_Values.NF;
        }

        // Check validity

        GT_Recipe aRecipe = new GT_Recipe(
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
            // Logger.INFO("Processing "+(disableOptional ? "Extracting" : "Canning")+" Recipe.");
            if (this.disableOptional) {
                addFluidExtractionRecipe(recipe);
            } else {
                addFluidCannerRecipe(recipe);
            }
        }
    }

    private boolean addFluidExtractionRecipe(GT_Recipe aRecipe) {
        boolean result = false;
        CORE.crash();
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
        result = aCount1 > aCount2;
        if (result) {
            // Logger.INFO("[FIND] Added Extraction recipe for "+ItemUtils.getArrayStackNames(aRecipe.mInputs)+",
            // "+ItemUtils.getArrayStackNames(aRecipe.mOutputs)+",
            // "+ItemUtils.getArrayStackNames(aRecipe.mFluidInputs)+",
            // "+ItemUtils.getArrayStackNames(aRecipe.mFluidOutputs));
        } else {
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
        return result;
    }

    private boolean addFluidCannerRecipe(GT_Recipe aRecipe) {
        boolean result = false;
        int aCount1 = getMapSize(RecipeMaps.fluidCannerRecipes);
        int aCount2 = aCount1;
        RecipeMaps.fluidCannerRecipes.addRecipe(aRecipe);
        aCount1 = getMapSize(RecipeMaps.fluidCannerRecipes);
        result = aCount1 > aCount2;
        if (result) {
            // Logger.INFO("[FIND] Added Canning recipe for "+ItemUtils.getArrayStackNames(aRecipe.mInputs)+",
            // "+ItemUtils.getArrayStackNames(aRecipe.mOutputs)+",
            // "+ItemUtils.getArrayStackNames(aRecipe.mFluidInputs)+",
            // "+ItemUtils.getArrayStackNames(aRecipe.mFluidOutputs));
        } else {
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
        return result;
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
        return aMap.getAllRecipes().size();
    }
}
