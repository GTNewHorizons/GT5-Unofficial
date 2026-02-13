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
import gtPlusPlus.core.util.minecraft.ItemUtils;

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

    private static final HashSet<RecipeGenFluidCanning> mCache = new HashSet<>();

    private static void addRunnableToRecipeCache(RecipeGenFluidCanning r) {
        if (mHasRun) {
            throw new IllegalArgumentException();
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
            new ItemStack[] { aInput },
            new ItemStack[] { aOutput },
            null,
            null,
            new int[] { 10000 },
            null,
            null,
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
                throw new IllegalArgumentException();
            } else {
                addFluidCannerRecipe(recipe);
            }
        }
    }

    private void addFluidCannerRecipe(GTRecipe aRecipe) {
        boolean result;
        int aCount1 = getMapSize(RecipeMaps.cannerRecipes);
        int aCount2 = aCount1;
        RecipeMaps.cannerRecipes.addRecipe(aRecipe);
        aCount1 = getMapSize(RecipeMaps.cannerRecipes);
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
        Logger.modLogger.info((disableOptional ? "EXTRACTING" : "CANNING") + " DEBUG ", new Exception());
    }

    private int getMapSize(RecipeMap<?> aMap) {
        return aMap.getAllRecipes()
            .size();
    }
}
