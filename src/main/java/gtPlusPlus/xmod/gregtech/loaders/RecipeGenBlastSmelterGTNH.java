package gtPlusPlus.xmod.gregtech.loaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.enums.GTValues;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gregtech.common.items.ItemIntegratedCircuit;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.ItemStackData;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.util.math.MathUtils;

public class RecipeGenBlastSmelterGTNH {

    private static final Map<String, FluidStack> mCachedIngotToFluidRegistry = new HashMap<>();
    private static final Map<String, String> mCachedHotToColdRegistry = new HashMap<>();

    private static synchronized void setIngotToFluid(final ItemStackData stack, final FluidStack fluid) {
        if (stack != null && fluid != null) {
            mCachedIngotToFluidRegistry.put(stack.getUniqueDataIdentifier(), fluid);
        }
    }

    private static synchronized void setHotToCold(final ItemStackData hot, final ItemStackData cold) {
        if (hot != null && cold != null) {
            mCachedHotToColdRegistry.put(hot.getUniqueDataIdentifier(), cold.getUniqueDataIdentifier());
        }
    }

    private static synchronized FluidStack getFluidFromIngot(final ItemStackData ingot) {
        if (mCachedIngotToFluidRegistry.containsKey(ingot.getUniqueDataIdentifier())) {
            Logger.MACHINE_INFO("[ABS] mCachedIngotToFluidRegistry contains Output Ingot.");
            return mCachedIngotToFluidRegistry.get(ingot.getUniqueDataIdentifier());
        }
        if (mCachedHotToColdRegistry.containsKey(ingot.getUniqueDataIdentifier())) {
            Logger.MACHINE_INFO("[ABS] mCachedHotToColdRegistry contains Output Ingot.");
            return mCachedIngotToFluidRegistry.get(mCachedHotToColdRegistry.get(ingot.getUniqueDataIdentifier()));
        }
        Logger.MACHINE_INFO("[ABS] Neither Cache contains Output Ingot.");
        return null;
    }

    private static boolean isValid(final ItemStack[] inputs, final ItemStack[] outputs, final FluidStack[] fluidIn,
        final FluidStack fluidOut) {
        return inputs != null && outputs != null
            && fluidIn != null
            && fluidOut != null
            && inputs.length > 0
            && outputs.length > 0;
    }

    public static synchronized boolean generateGTNHBlastSmelterRecipesFromEBFList() {

        // Make a counting object
        int mSuccess = 0;

        Logger.INFO("[ABS] Starting recipe generation based on EBF recipe map.");
        Logger.INFO("[ABS] Caching Ingots and their Molten fluid..");
        // Ingots/Dusts -> Fluids
        for (GTRecipe x : RecipeMaps.fluidExtractionRecipes.getAllRecipes()) {
            ItemStack validInput = null;
            FluidStack validOutput = null;
            // If there aren't both non empty inputs and outputs, we skip
            if (ArrayUtils.isEmpty(x.mInputs) || ArrayUtils.isEmpty(x.mFluidOutputs)) {
                continue;
            }

            for (int tag : OreDictionary.getOreIDs(x.mInputs[0])) {
                String oreName = OreDictionary.getOreName(tag)
                    .toLowerCase();
                String mType = "ingot";
                if (oreName.startsWith(mType) && !oreName.contains("double")
                    && !oreName.contains("triple")
                    && !oreName.contains("quad")
                    && !oreName.contains("quintuple")) {
                    validInput = x.mInputs[0];
                }
            }

            validOutput = x.mFluidOutputs[0];

            if (validInput != null) {
                ItemStackData R = new ItemStackData(validInput);
                setIngotToFluid(R, validOutput);
                Logger.MACHINE_INFO(
                    "[ABS][I2F] Cached " + validInput.getDisplayName()
                        + " to "
                        + validOutput.getLocalizedName()
                        + ". Stored Under ID of "
                        + R.getUniqueDataIdentifier());
            }
        }

        Logger.INFO("[ABS] Caching Ingots and their Hot form...");
        // Hot Ingots -> Cold Ingots
        for (GTRecipe x : RecipeMaps.vacuumFreezerRecipes.getAllRecipes()) {
            ItemStack validInput = null;
            ItemStack validOutput = null;
            // If we the input is an ingot and it and the output are valid, map it to cache.
            if (ArrayUtils.isNotEmpty(x.mInputs) && x.mInputs[0] != null) {
                validInput = x.mInputs[0];
            }
            if (ArrayUtils.isNotEmpty(x.mOutputs) && x.mOutputs[0] != null) {
                validOutput = x.mOutputs[0];
            }
            if (validInput != null && validOutput != null) {
                ItemStackData R1 = new ItemStackData(validInput);
                ItemStackData R2 = new ItemStackData(validOutput);
                setHotToCold(R1, R2);
                Logger.MACHINE_INFO(
                    "[ABS][H2C] Cached " + validInput.getDisplayName()
                        + " to "
                        + validOutput.getDisplayName()
                        + ". Stored Under ID of "
                        + R1.getUniqueDataIdentifier()
                        + ", links to ID "
                        + R2.getUniqueDataIdentifier());
            }
        }

        Logger.INFO("[ABS] Caching existing ABS recipes...");
        GTRecipe[] recipeCandidates = GTPPRecipeMaps.alloyBlastSmelterRecipes.getAllRecipes()
            .stream()
            .filter(r -> r.mOutputs.length == 0 && r.mFluidOutputs.length == 1)
            .toArray(GTRecipe[]::new);
        Logger.MACHINE_INFO("[ABS] Cached " + recipeCandidates.length + " existing ABS recipes...");

        Logger.INFO("[ABS] Generating recipes based on existing EBF recipes.");
        // Okay, so now lets Iterate existing EBF recipes.
        for (GTRecipe x : RecipeMaps.blastFurnaceRecipes.getAllRecipes()) {
            ItemStack[] inputs, outputs;
            FluidStack[] inputsF;
            int voltage, time, special;
            boolean enabled;
            inputs = x.mInputs.clone();
            outputs = x.mOutputs.clone();
            inputsF = x.mFluidInputs.clone();
            voltage = x.mEUt;
            time = x.mDuration;
            enabled = x.mEnabled;
            special = x.mSpecialValue;

            // continue to next recipe if the Temp is too high.
            if (special > 3600) {
                Logger.MACHINE_INFO("[ABS] Skipping ABS addition for GTNH due to temp.");
                continue;
            }

            FluidStack mMoltenStack = null;
            int mMoltenCount = 0;
            // If We have a valid Output, let's try use our cached data to get it's molten form.
            if (x.mOutputs != null && x.mOutputs[0] != null) {
                mMoltenCount = x.mOutputs[0].stackSize;
                ItemStackData R = new ItemStackData(x.mOutputs[0]);
                Logger.MACHINE_INFO(
                    "[ABS] Found " + x.mOutputs[0].getDisplayName()
                        + " as valid EBF output, finding it's fluid from the cache. We will require "
                        + (144 * mMoltenCount)
                        + "L. Looking for ID "
                        + R.getUniqueDataIdentifier());
                FluidStack tempFluid = getFluidFromIngot(R);
                if (tempFluid != null) {
                    // Logger.MACHINE_INFO("[ABS] Got Fluid from Cache.");
                    mMoltenStack = new FluidStack(tempFluid, mMoltenCount * 144);
                } else {
                    Logger.MACHINE_INFO("[ABS] Failed to get Fluid from Cache.");
                }
            }

            // This recipe isn't enabled
            if (!enabled) {
                Logger.MACHINE_INFO("[ABS] Failure. EBF recipe was not enabled.");
                continue;
            }
            // We don't have a valid molten fluidstack
            if (!isValid(inputs, outputs, inputsF, mMoltenStack)) {
                Logger.MACHINE_INFO("[ABS] Failure. Invalid Inputs or Outputs.");
                if (inputs == null) {
                    Logger.MACHINE_INFO("[ABS] Inputs were not Valid.");
                } else {
                    Logger.MACHINE_INFO("[ABS] inputs size: " + inputs.length);
                }
                if (outputs == null) {
                    Logger.MACHINE_INFO("[ABS] Outputs were not Valid.");
                } else {
                    Logger.MACHINE_INFO("[ABS] outputs size: " + outputs.length);
                }
                if (inputsF == null) {
                    Logger.MACHINE_INFO("[ABS] Input Fluids were not Valid.");
                } else {
                    Logger.MACHINE_INFO("[ABS] inputsF size: " + inputsF.length);
                }
                if (mMoltenStack == null) {
                    Logger.MACHINE_INFO("[ABS] Output Fluid were not Valid.");
                }
                continue;
            }

            // Boolean to decide whether or not to create a new circuit later
            boolean circuitFound = false;

            // Build correct input stack
            ArrayList<ItemStack> aTempList = new ArrayList<>();
            for (ItemStack recipeItem : inputs) {
                if (recipeItem != null && recipeItem.getItem() instanceof ItemIntegratedCircuit) {
                    circuitFound = true;
                }
                aTempList.add(recipeItem);
            }

            inputs = aTempList.toArray(new ItemStack[0]);
            int baseItemCount = circuitFound ? inputs.length - 1 : inputs.length;

            boolean recipeFound = false;
            for (GTRecipe recipe : recipeCandidates) {
                if (itemStacksMatch(recipe.mInputs, inputs) && fluidStacksMatch(recipe.mFluidInputs, inputsF)
                    && GTUtility.areFluidsEqual(recipe.mFluidOutputs[0], mMoltenStack)) {
                    recipeFound = true;
                    break;
                }
            }
            if (recipeFound) {
                Logger.MACHINE_INFO("[ABS] Skipping ABS addition for GTNH due to existing recipe.");
                continue;
            }

            GTRecipeBuilder builder = GTValues.RA.stdBuilder()
                .itemInputs(inputs)
                .fluidInputs(inputsF)
                .fluidOutputs(mMoltenStack)
                .duration(MathUtils.roundToClosestInt(time * 0.8))
                .eut(voltage)
                .recipeCategory(
                    baseItemCount == 1 ? RecipeCategories.absNonAlloyRecipes
                        : GTPPRecipeMaps.alloyBlastSmelterRecipes.getDefaultRecipeCategory());

            if (!circuitFound) {
                builder.circuit(inputs.length);
            }

            builder.addTo(GTPPRecipeMaps.alloyBlastSmelterRecipes);
            mSuccess++;

        }

        Logger.INFO("[ABS] Processed " + mSuccess + " recipes.");
        return mSuccess > 0;
    }

    private static boolean itemStacksMatch(final ItemStack[] mStack1, final ItemStack[] mStack2) {
        if (mStack1 == null || mStack2 == null) {
            return false;
        }

        // Build lists of non-circuit, non-null items for more robust check
        List<ItemStack> list1 = new ArrayList<>();
        for (ItemStack part : mStack1) {
            if (part != null && !(part.getItem() instanceof ItemIntegratedCircuit)) {
                list1.add(part);
            }
        }

        List<ItemStack> list2 = new ArrayList<>();
        for (ItemStack part : mStack2) {
            if (part != null && !(part.getItem() instanceof ItemIntegratedCircuit)) {
                list2.add(part);
            }
        }

        if (list1.size() != list2.size()) {
            return false;
        }

        for (int i = 0; i < list1.size(); i++) {
            if (!GTUtility.areStacksEqual(list1.get(i), list2.get(i))) {
                return false;
            }
        }

        return true;
    }

    private static boolean fluidStacksMatch(final FluidStack[] mStack1, final FluidStack[] mStack2) {
        if (mStack1 == null || mStack2 == null || mStack1.length != mStack2.length) {
            return false;
        }

        for (int c = 0; c < mStack1.length; c++) {
            if (!GTUtility.areFluidsEqual(mStack1[c], mStack2[c])) {
                return false;
            }
        }

        return true;
    }
}
