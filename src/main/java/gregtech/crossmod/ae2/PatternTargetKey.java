package gregtech.crossmod.ae2;

import java.util.ArrayList;
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
 * What a GregTech machine needs to run a pattern: its current recipe map, its voltage and the items it adds to every
 * recipe check, such as the circuit.
 */
public record PatternTargetKey(RecipeMap<?> recipeMap, long voltage, List<ItemId> items) {

    /**
     * Builds a key; a non-positive voltage turns the voltage check off.
     */
    public static PatternTargetKey of(RecipeMap<?> recipeMap, long voltage, ItemStack[] items) {
        final List<ItemId> ids = new ArrayList<>(items.length);
        for (ItemStack stack : items) {
            if (stack != null) {
                ids.add(ItemId.createWithStackSize(stack));
            }
        }
        return new PatternTargetKey(recipeMap, Math.max(voltage, 0), ids);
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

        for (PatternTargetKey key : keys) {
            results.put(key, key.match(pattern, patternItems, patternCircuit, patternFluids));
        }
        return results;
    }

    private PatternMatch match(ICraftingPatternDetails pattern, List<ItemStack> patternItems,
        @Nullable ItemStack patternCircuit, List<FluidStack> patternFluids) {
        final List<ItemStack> inputs = new ArrayList<>(patternItems);
        for (ItemId id : items) {
            final ItemStack stack = id.getItemStack(id.stackSize());
            // The pattern's circuit lands next to the machine's own, and the machine runs whichever recipe it finds
            // first.
            if (patternCircuit != null && GTUtility.isAnyIntegratedCircuit(stack)
                && !GTUtility.areStacksEqual(stack, patternCircuit)) {
                return PatternMatch.NO_MATCH;
            }
            inputs.add(stack);
        }

        final FindRecipeQuery query = recipeMap.findRecipeQuery()
            .items(inputs.toArray(new ItemStack[0]))
            .fluids(patternFluids.toArray(new FluidStack[0]));
        // The query multiplies the voltage by the map's amperage, so "no check" must skip the call, not pass a max.
        if (voltage > 0) {
            query.voltage(voltage);
        }
        final GTRecipe recipe = query.find();
        if (recipe == null) {
            return PatternMatch.NO_MATCH;
        }
        return producesAllOutputs(recipe, pattern) ? PatternMatch.MATCH : PatternMatch.NO_MATCH;
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
