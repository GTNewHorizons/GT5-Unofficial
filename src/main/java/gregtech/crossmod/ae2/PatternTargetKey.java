package gregtech.crossmod.ae2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import com.github.bsideup.jabel.Desugar;

import appeng.api.crafting.IPatternTargetKey;
import appeng.api.crafting.PatternMatch;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.FindRecipeQuery;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.ItemId;

/**
 * What a GregTech machine needs to run a pattern: the recipe maps it can use, its voltage and the sets of items it may
 * add to a recipe check, such as the circuit; one fitting set is enough.
 */
@Desugar
public record PatternTargetKey(List<RecipeMap<?>> recipeMaps, long voltage, List<List<ItemId>> itemSets,
    @Nullable ItemId specialItem) implements IPatternTargetKey {

    /**
     * Builds a key with one item set; a non-positive voltage turns the voltage check off.
     */
    public static PatternTargetKey of(Collection<RecipeMap<?>> recipeMaps, long voltage, Collection<ItemStack> items) {
        return ofSets(recipeMaps, voltage, Collections.singletonList(items));
    }

    /** Builds a key with the machine's special-slot item. */
    public static PatternTargetKey of(Collection<RecipeMap<?>> recipeMaps, long voltage, Collection<ItemStack> items,
        @Nullable ItemStack specialItem) {
        final PatternTargetKey key = of(recipeMaps, voltage, items);
        return specialItem == null ? key
            : new PatternTargetKey(key.recipeMaps, key.voltage, key.itemSets, ItemId.createWithStackSize(specialItem));
    }

    /**
     * Builds a key that matches when any of the item sets fits; a non-positive voltage turns the voltage check off.
     */
    public static PatternTargetKey ofSets(Collection<RecipeMap<?>> recipeMaps, long voltage,
        Collection<? extends Collection<ItemStack>> itemSets) {
        final List<List<ItemId>> sets = new ArrayList<>(itemSets.size());
        for (Collection<ItemStack> items : itemSets) {
            final List<ItemId> ids = new ArrayList<>(items.size());
            for (ItemStack stack : items) {
                if (stack != null) {
                    ids.add(ItemId.createWithStackSize(stack));
                }
            }
            sets.add(ids);
        }
        return new PatternTargetKey(new ArrayList<>(recipeMaps), Math.max(voltage, 0), sets, null);
    }

    /**
     * Returns MATCH when the machine finds a recipe for the pattern with any of its item sets.
     */
    @Override
    public PatternMatch match(ICraftingPatternDetails pattern) {
        if (pattern.isCraftable()) {
            return PatternMatch.NO_MATCH;
        }

        final List<ItemStack> patternItems = new ArrayList<>();
        final List<FluidStack> patternFluids = new ArrayList<>();
        ItemStack patternCircuit = null;
        for (IAEStack<?> input : pattern.getCondensedAEInputs()) {
            if (input instanceof IAEItemStack item) {
                final ItemStack stack = item.getItemStack();
                patternItems.add(stack);
                if (GTUtility.isAnyIntegratedCircuit(stack)) {
                    patternCircuit = stack;
                }
            } else if (input instanceof IAEFluidStack fluid) {
                patternFluids.add(fluid.getFluidStack());
            }
        }

        final FluidStack[] fluidArray = patternFluids.toArray(new FluidStack[0]);
        for (List<ItemId> items : itemSets) {
            if (fits(items, pattern, patternItems, patternCircuit, fluidArray)) {
                return PatternMatch.MATCH;
            }
        }
        return PatternMatch.NO_MATCH;
    }

    private boolean fits(List<ItemId> items, ICraftingPatternDetails pattern, List<ItemStack> patternItems,
        @Nullable ItemStack patternCircuit, FluidStack[] fluidArray) {
        final List<ItemStack> inputs = new ArrayList<>(patternItems);
        for (ItemId id : items) {
            final ItemStack stack = id.getItemStack(id.stackSize());
            // The pattern's circuit lands next to the machine's own, and the machine runs whichever recipe it finds
            // first.
            if (patternCircuit != null && GTUtility.isAnyIntegratedCircuit(stack)
                && !GTUtility.areStacksEqual(stack, patternCircuit)) {
                return false;
            }
            inputs.add(stack);
        }

        final ItemStack[] inputArray = inputs.toArray(new ItemStack[0]);
        for (RecipeMap<?> recipeMap : recipeMaps) {
            final FindRecipeQuery query = recipeMap.findRecipeQuery()
                .items(inputArray)
                .fluids(fluidArray)
                .specialSlot(specialItem == null ? null : specialItem.getItemStack(specialItem.stackSize()));
            // The query multiplies the voltage by the map's amperage, so "no check" must skip the call, not pass a max.
            if (voltage > 0) {
                query.voltage(voltage);
            }
            final GTRecipe recipe = query.find();
            if (recipe != null && isRecipeMultiple(recipe, pattern, patternItems, fluidArray)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether the pattern is the recipe times a whole number, with every pattern input used by the recipe.
     */
    private static boolean isRecipeMultiple(GTRecipe recipe, ICraftingPatternDetails pattern,
        List<ItemStack> patternItems, FluidStack[] patternFluids) {
        long multiplier = outputMultiplier(recipe, pattern);
        if (multiplier < 0) {
            return false;
        }
        if (multiplier == 0) {
            // Only chanced outputs in the pattern, so the inputs set the multiplier.
            multiplier = inputMultiplier(recipe, patternItems, patternFluids);
            if (multiplier <= 0) {
                return false;
            }
        }
        return consumesAllItems(recipe, patternItems, multiplier)
            && consumesAllFluids(recipe, patternFluids, multiplier);
    }

    /**
     * Returns how many recipe runs the pattern outputs make, 0 if only chanced outputs set it, or -1 if they disagree.
     */
    private static long outputMultiplier(GTRecipe recipe, ICraftingPatternDetails pattern) {
        long multiplier = 0;
        for (IAEStack<?> output : pattern.getCondensedAEOutputs()) {
            final long perRun;
            if (output instanceof IAEItemStack item) {
                perRun = guaranteedItemOutput(recipe, item.getItemStack());
            } else if (output instanceof IAEFluidStack fluid) {
                perRun = guaranteedFluidOutput(recipe, fluid.getFluidStack());
            } else {
                continue;
            }
            if (perRun < 0 || (perRun > 0 && output.getStackSize() % perRun != 0)) {
                return -1;
            }
            if (perRun == 0) {
                continue;
            }
            final long runs = output.getStackSize() / perRun;
            if (multiplier != 0 && multiplier != runs) {
                return -1;
            }
            multiplier = runs;
        }
        return multiplier;
    }

    /**
     * Returns the amount of the item one recipe run always makes, 0 if only by chance, or -1 if never.
     */
    private static long guaranteedItemOutput(GTRecipe recipe, ItemStack wanted) {
        if (recipe.mOutputs == null) {
            return -1;
        }
        long amount = 0;
        boolean found = false;
        for (int i = 0; i < recipe.mOutputs.length; i++) {
            if (GTUtility.areStacksEqual(recipe.mOutputs[i], wanted)) {
                found = true;
                if (recipe.getOutputChance(i) >= 10000) {
                    amount += recipe.mOutputs[i].stackSize;
                }
            }
        }
        return found ? amount : -1;
    }

    /**
     * Returns the amount of the fluid one recipe run always makes, 0 if only by chance, or -1 if never.
     */
    private static long guaranteedFluidOutput(GTRecipe recipe, FluidStack wanted) {
        if (recipe.mFluidOutputs == null) {
            return -1;
        }
        long amount = 0;
        boolean found = false;
        for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
            final FluidStack output = recipe.mFluidOutputs[i];
            if (output != null && output.isFluidEqual(wanted)) {
                found = true;
                if (recipe.getFluidOutputChance(i) >= 10000) {
                    amount += output.amount;
                }
            }
        }
        return found ? amount : -1;
    }

    /**
     * Returns how many recipe runs the first consumed input covers, or -1 if it is not a whole number.
     */
    private static long inputMultiplier(GTRecipe recipe, List<ItemStack> patternItems, FluidStack[] patternFluids) {
        for (GTRecipe.RecipeItemInput input : recipe.getCachedCombinedItemInputs()) {
            if (input.inputAmount <= 0) {
                continue;
            }
            long provided = 0;
            for (ItemStack stack : patternItems) {
                if (input.matchesRecipe(GTOreDictUnificator.getAssociation(stack), stack)) {
                    provided += stack.stackSize;
                }
            }
            return provided % input.inputAmount == 0 ? provided / input.inputAmount : -1;
        }
        for (FluidStack input : recipe.mFluidInputs) {
            if (input == null || input.amount <= 0) {
                continue;
            }
            long provided = 0;
            for (FluidStack fluid : patternFluids) {
                if (fluid.isFluidEqual(input)) {
                    provided += fluid.amount;
                }
            }
            return provided % input.amount == 0 ? provided / input.amount : -1;
        }
        return 1;
    }

    /**
     * Returns whether the recipe run the given number of times uses up the pattern's items exactly.
     */
    private static boolean consumesAllItems(GTRecipe recipe, List<ItemStack> patternItems, long multiplier) {
        final int count = patternItems.size();
        final long[] left = new long[count];
        final ItemData[] associations = new ItemData[count];
        final boolean[] used = new boolean[count];
        final boolean[] nonConsumed = new boolean[count];
        for (int i = 0; i < count; i++) {
            left[i] = patternItems.get(i).stackSize;
            associations[i] = GTOreDictUnificator.getAssociation(patternItems.get(i));
        }

        // Same walk as GTRecipe.consumeInput, but every input is taken in full: chanced consumption would be random.
        for (GTRecipe.RecipeItemInput input : recipe.getCachedCombinedItemInputs()) {
            long needed = input.inputAmount * multiplier;
            for (int i = 0; i < count; i++) {
                if (!input.matchesRecipe(associations[i], patternItems.get(i))) {
                    continue;
                }
                used[i] = true;
                if (input.inputAmount == 0) {
                    nonConsumed[i] = true;
                }
                final long taken = Math.min(needed, left[i]);
                left[i] -= taken;
                needed -= taken;
            }
            if (needed > 0) {
                return false;
            }
        }

        for (int i = 0; i < count; i++) {
            // A circuit or mold in the pattern stays in the machine, so only it may be left over.
            if (!used[i] || (left[i] > 0 && !nonConsumed[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns whether the recipe run the given number of times uses up the pattern's fluids exactly.
     */
    private static boolean consumesAllFluids(GTRecipe recipe, FluidStack[] patternFluids, long multiplier) {
        final long[] left = new long[patternFluids.length];
        final boolean[] used = new boolean[patternFluids.length];
        final boolean[] nonConsumed = new boolean[patternFluids.length];
        for (int i = 0; i < patternFluids.length; i++) {
            left[i] = patternFluids[i].amount;
        }

        for (int slot = 0; slot < recipe.mFluidInputs.length; slot++) {
            final FluidStack input = recipe.mFluidInputs[slot];
            if (input == null) {
                continue;
            }
            final FluidStack[] alternatives = recipe.mAltFluidInputs != null && slot < recipe.mAltFluidInputs.length
                && recipe.mAltFluidInputs[slot] != null ? recipe.mAltFluidInputs[slot] : new FluidStack[] { input };
            final FluidStack provided = firstProvided(alternatives, patternFluids);
            if (provided == null) {
                return false;
            }
            long needed = (long) provided.amount * multiplier;
            for (int i = 0; i < patternFluids.length; i++) {
                if (!patternFluids[i].isFluidEqual(provided)) {
                    continue;
                }
                used[i] = true;
                if (provided.amount <= 0) {
                    nonConsumed[i] = true;
                    continue;
                }
                final long taken = Math.min(needed, left[i]);
                left[i] -= taken;
                needed -= taken;
            }
            if (needed > 0) {
                return false;
            }
        }

        for (int i = 0; i < patternFluids.length; i++) {
            // The recipe does not consume this fluid, so its pattern amount may remain.
            if (!used[i] || (left[i] > 0 && !nonConsumed[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the first recipe alternative that the pattern supplies, or null.
     */
    @Nullable
    private static FluidStack firstProvided(FluidStack[] alternatives, FluidStack[] patternFluids) {
        for (FluidStack alternative : alternatives) {
            if (alternative == null) {
                continue;
            }
            for (FluidStack fluid : patternFluids) {
                if (alternative.isFluidEqual(fluid)) {
                    return alternative;
                }
            }
        }
        return null;
    }
}
