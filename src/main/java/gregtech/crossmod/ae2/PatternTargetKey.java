package gregtech.crossmod.ae2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import appeng.api.crafting.PatternMatch;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import gregtech.api.recipe.FindRecipeQuery;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.ItemId;

/**
 * What a GregTech machine needs to run a pattern: the recipe maps it can use, its voltage and the sets of items it may
 * add to a recipe check, such as the circuit; one fitting set is enough.
 */
public record PatternTargetKey(List<RecipeMap<?>> recipeMaps, long voltage, List<List<ItemId>> itemSets) {

    /**
     * Builds a key with one item set; a non-positive voltage turns the voltage check off.
     */
    public static PatternTargetKey of(Collection<RecipeMap<?>> recipeMaps, long voltage, Collection<ItemStack> items) {
        return ofSets(recipeMaps, voltage, Collections.singletonList(items));
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
        return new PatternTargetKey(new ArrayList<>(recipeMaps), Math.max(voltage, 0), sets);
    }

    /**
     * Matcher registered with AE2; finds a recipe for each key once.
     */
    public static Map<PatternTargetKey, PatternMatch> match(ICraftingPatternDetails pattern,
        Set<PatternTargetKey> keys) {
        final Map<PatternTargetKey, PatternMatch> results = new HashMap<>();
        if (pattern.isCraftable()) {
            for (PatternTargetKey key : keys) {
                results.put(key, PatternMatch.NO_MATCH);
            }
            return results;
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
        for (PatternTargetKey key : keys) {
            results.put(key, key.match(pattern, patternItems, patternCircuit, fluidArray));
        }
        return results;
    }

    private PatternMatch match(ICraftingPatternDetails pattern, List<ItemStack> patternItems,
        @Nullable ItemStack patternCircuit, FluidStack[] fluidArray) {
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
                .fluids(fluidArray);
            // The query multiplies the voltage by the map's amperage, so "no check" must skip the call, not pass a max.
            if (voltage > 0) {
                query.voltage(voltage);
            }
            final GTRecipe recipe = query.find();
            if (recipe != null && producesAllOutputs(recipe, pattern)) {
                return true;
            }
        }
        return false;
    }

    private static boolean producesAllOutputs(GTRecipe recipe, ICraftingPatternDetails pattern) {
        for (IAEStack<?> output : pattern.getCondensedAEOutputs()) {
            if (output instanceof IAEItemStack item) {
                if (!containsItem(recipe.mOutputs, item.getItemStack())) {
                    return false;
                }
            } else if (output instanceof IAEFluidStack fluid) {
                if (!containsFluid(recipe.mFluidOutputs, fluid.getFluidStack())) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean containsItem(@Nullable ItemStack[] outputs, ItemStack wanted) {
        if (outputs == null) {
            return false;
        }
        for (ItemStack output : outputs) {
            if (GTUtility.areStacksEqual(output, wanted)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsFluid(@Nullable FluidStack[] outputs, FluidStack wanted) {
        if (outputs == null) {
            return false;
        }
        for (FluidStack output : outputs) {
            if (output != null && output.isFluidEqual(wanted)) {
                return true;
            }
        }
        return false;
    }
}
