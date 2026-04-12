package gtPlusPlus.xmod.gregtech.loaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
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
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.util.math.MathUtils;

public class RecipeGenBlastSmelterGTNH {

    private static Map<String, FluidStack> mCachedIngotToFluidRegistry = new HashMap<>();
    private static Map<String, String> mCachedHotToColdRegistry = new HashMap<>();

    private static void setIngotToFluid(final ItemStack stack, final FluidStack fluid) {
        if (stack != null && fluid != null) {
            mCachedIngotToFluidRegistry.put(getUniqueDataIdentifier(stack), fluid);
        }
    }

    private static void setHotToCold(final ItemStack hot, final ItemStack cold) {
        if (hot != null && cold != null) {
            mCachedHotToColdRegistry.put(getUniqueDataIdentifier(hot), getUniqueDataIdentifier(cold));
        }
    }

    private static FluidStack getFluidFromIngot(final ItemStack ingot) {
        if (mCachedIngotToFluidRegistry.containsKey(getUniqueDataIdentifier(ingot))) {
            return mCachedIngotToFluidRegistry.get(getUniqueDataIdentifier(ingot));
        }
        if (mCachedHotToColdRegistry.containsKey(getUniqueDataIdentifier(ingot))) {
            return mCachedIngotToFluidRegistry.get(mCachedHotToColdRegistry.get(getUniqueDataIdentifier(ingot)));
        }
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

    public static void generateGTNHBlastSmelterRecipesFromEBFList() {

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
                setIngotToFluid(validInput, validOutput);
            }
        }

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
                setHotToCold(validInput, validOutput);
            }
        }

        GTRecipe[] recipeCandidates = GTPPRecipeMaps.alloyBlastSmelterRecipes.getAllRecipes()
            .stream()
            .filter(r -> r.mOutputs.length == 0 && r.mFluidOutputs.length == 1)
            .toArray(GTRecipe[]::new);
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
                continue;
            }

            FluidStack mMoltenStack = null;
            int mMoltenCount = 0;
            // If We have a valid Output, let's try use our cached data to get it's molten form.
            if (x.mOutputs != null && x.mOutputs[0] != null) {
                mMoltenCount = x.mOutputs[0].stackSize;
                FluidStack tempFluid = getFluidFromIngot(x.mOutputs[0]);
                if (tempFluid != null) {
                    mMoltenStack = new FluidStack(tempFluid, mMoltenCount * 144);
                }
            }

            // This recipe isn't enabled
            if (!enabled) {
                continue;
            }
            // We don't have a valid molten fluidstack
            if (!isValid(inputs, outputs, inputsF, mMoltenStack)) {
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
        }
        mCachedIngotToFluidRegistry = null;
        mCachedHotToColdRegistry = null;
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

    private static String getUniqueDataIdentifier(final ItemStack stack) {
        final Item mItem = stack.getItem();
        final int mDamage = stack.getItemDamage();
        final int mStackSize = stack.stackSize;
        return String.valueOf(Item.getIdFromItem(mItem)) + mDamage + mStackSize + 10;
    }
}
