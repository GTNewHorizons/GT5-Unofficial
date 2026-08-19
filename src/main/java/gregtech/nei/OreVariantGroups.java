package gregtech.nei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import codechicken.nei.PositionedStack;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeConstants;
import gregtech.common.ores.GTOreAdapter;
import gregtech.nei.GTNEIDefaultHandler.CachedDefaultRecipe;

/// Collapses the per-stone-variant ore recipes `ShapeConsumerSupport#delegateOreVariants` fans out into one
/// NEI entry per (material, action): the shared [GTRecipeConstants#VARIANT_GROUP] tag plus the recipe's other
/// inputs identify the members, and their stacks become permutations of a single displayed recipe.
///
/// Display only. The recipe map still holds every member, so machine lookup, "show recipes" and "show uses"
/// all still answer for each individual stone variant -- the merged entry's permutation lists contain all of
/// them, so a query on any variant lands on it.
///
/// NEI cycles input permutations on its own timer and never touches output permutations
/// ([codechicken.nei.recipe.NEIRecipeWidget]), and the slots hold different numbers of permutations anyway.
/// A [Cycle] therefore drives the whole entry from the ore slot: whichever variant NEI has cycled that slot
/// to, every other slot is set to the same member before it is drawn.
final class OreVariantGroups {

    private OreVariantGroups() {}

    /// The recipes of `sorted` as displayed entries, in the same order, with each variant group replaced by
    /// one merged entry at the position of its first member. A group whose members do not share a slot layout
    /// stays expanded.
    static List<CachedDefaultRecipe> collapse(List<GTRecipe> sorted, Function<GTRecipe, CachedDefaultRecipe> factory) {
        Map<String, List<GTRecipe>> groups = new LinkedHashMap<>();
        List<Object> slots = new ArrayList<>(sorted.size());

        for (GTRecipe recipe : sorted) {
            String key = groupKey(recipe);
            if (key == null) {
                slots.add(recipe);
                continue;
            }

            List<GTRecipe> members = groups.get(key);
            if (members == null) {
                members = new ArrayList<>();
                groups.put(key, members);
                slots.add(key);
            }
            members.add(recipe);
        }

        List<CachedDefaultRecipe> displayed = new ArrayList<>(slots.size());

        for (Object slot : slots) {
            if (slot instanceof GTRecipe recipe) {
                displayed.add(factory.apply(recipe));
                continue;
            }

            List<GTRecipe> members = groups.get(slot);
            CachedDefaultRecipe merged = members.size() > 1 ? merge(members, factory) : null;

            if (merged != null) {
                displayed.add(merged);
            } else {
                for (GTRecipe recipe : members) displayed.add(factory.apply(recipe));
            }
        }

        return displayed;
    }

    /// The key recipes of one group share: the tag plus the identity of every input that is not a variant
    /// stack. Stack sizes are left out, since a richer stone multiplies both the ore and its reagents.
    private static @Nullable String groupKey(GTRecipe recipe) {
        String tag = recipe.getMetadata(GTRecipeConstants.VARIANT_GROUP);
        if (tag == null) return null;

        StringBuilder key = new StringBuilder(tag);

        for (ItemStack input : recipe.mInputs) {
            key.append('|');
            if (input == null || input.getItem() == null) continue;
            key.append(
                isVariantStack(input) ? "*"
                    : Item.itemRegistry.getNameForObject(input.getItem()) + '#' + input.getItemDamage());
        }

        for (FluidStack input : recipe.mFluidInputs) {
            key.append('|');
            if (input != null && input.getFluid() != null) key.append(
                input.getFluid()
                    .getName());
        }

        return key.toString();
    }

    /// Whether a stack is one of the ore blocks a variant group cycles through.
    private static boolean isVariantStack(ItemStack stack) {
        Block block = Block.getBlockFromItem(stack.getItem());
        return block != null && GTOreAdapter.INSTANCE.supports(block, stack.getItemDamage());
    }

    /// The group's first member with every other member's stacks appended to its slots, or null when the
    /// members disagree on slot layout or none of the inputs is a variant stack.
    private static @Nullable CachedDefaultRecipe merge(List<GTRecipe> recipes,
        Function<GTRecipe, CachedDefaultRecipe> factory) {
        List<CachedDefaultRecipe> members = new ArrayList<>(recipes.size());
        for (GTRecipe recipe : recipes) members.add(factory.apply(recipe));

        CachedDefaultRecipe first = members.get(0);

        for (CachedDefaultRecipe member : members) {
            if (member.mInputs.size() != first.mInputs.size()) return null;
            if (member.mOutputs.size() != first.mOutputs.size()) return null;
        }

        int keySlot = -1;
        for (int slot = 0; slot < first.mInputs.size(); slot++) {
            if (isVariantStack(first.mInputs.get(slot).item)) {
                keySlot = slot;
                break;
            }
        }
        if (keySlot < 0) return null;

        int[] keyStarts = concatenate(members, keySlot, true);
        if (keyStarts == null) return null;

        List<PositionedStack> followers = new ArrayList<>();
        List<int[]> followerStarts = new ArrayList<>();

        for (int slot = 0; slot < first.mInputs.size(); slot++) {
            if (slot == keySlot) continue;
            collect(members, slot, true, followers, followerStarts);
        }
        for (int slot = 0; slot < first.mOutputs.size(); slot++) {
            collect(members, slot, false, followers, followerStarts);
        }

        first.setVariantCycle(
            new Cycle(
                first.mInputs.get(keySlot),
                memberOfPermutation(keyStarts, first.mInputs.get(keySlot).items.length),
                followers.toArray(new PositionedStack[0]),
                followerStarts.toArray(new int[0][])));

        return first;
    }

    private static void collect(List<CachedDefaultRecipe> members, int slot, boolean input,
        List<PositionedStack> followers, List<int[]> followerStarts) {
        int[] starts = concatenate(members, slot, input);
        if (starts == null) return;

        followers.add(stackAt(members.get(0), slot, input));
        followerStarts.add(starts);
    }

    /// Replaces the first member's slot contents with every member's, and answers where each member's run
    /// starts. Null when all the members hold the same stacks there, in which case the slot is left alone so
    /// NEI has nothing to cycle.
    private static int @Nullable [] concatenate(List<CachedDefaultRecipe> members, int slot, boolean input) {
        List<ItemStack> merged = new ArrayList<>();
        int[] starts = new int[members.size()];
        boolean varies = false;

        for (int member = 0; member < members.size(); member++) {
            ItemStack[] stacks = stackAt(members.get(member), slot, input).items;
            starts[member] = merged.size();
            varies |= member > 0 && !sameStacks(stacks, stackAt(members.get(0), slot, input).items);
            Collections.addAll(merged, stacks);
        }

        if (!varies) return null;

        stackAt(members.get(0), slot, input).items = merged.toArray(new ItemStack[0]);
        return starts;
    }

    private static PositionedStack stackAt(CachedDefaultRecipe recipe, int slot, boolean input) {
        return input ? recipe.mInputs.get(slot) : recipe.mOutputs.get(slot);
    }

    private static boolean sameStacks(ItemStack[] left, ItemStack[] right) {
        if (left.length != right.length) return false;

        for (int i = 0; i < left.length; i++) {
            if (left[i].getItem() != right[i].getItem()) return false;
            if (left[i].getItemDamage() != right[i].getItemDamage()) return false;
            if (left[i].stackSize != right[i].stackSize) return false;
        }

        return true;
    }

    private static int[] memberOfPermutation(int[] starts, int length) {
        int[] owner = new int[length];

        for (int member = 0; member < starts.length; member++) {
            int end = member + 1 < starts.length ? starts[member + 1] : length;
            for (int i = starts[member]; i < end; i++) owner[i] = member;
        }

        return owner;
    }

    /// Pins every merged slot of one entry to the member NEI has cycled its ore slot to.
    static final class Cycle {

        private final PositionedStack key;
        private final int[] memberOfPermutation;
        private final PositionedStack[] followers;
        private final int[][] followerStarts;

        Cycle(PositionedStack key, int[] memberOfPermutation, PositionedStack[] followers, int[][] followerStarts) {
            this.key = key;
            this.memberOfPermutation = memberOfPermutation;
            this.followers = followers;
            this.followerStarts = followerStarts;
        }

        /// Re-pins on every call rather than only when the ore slot moves: a follower with fewer permutations
        /// than the ore slot advances on the shared timer even while the ore slot stays put.
        void sync() {
            int permutation = key.getPermutationIndex(key.item);
            if (permutation < 0 || permutation >= memberOfPermutation.length) return;

            int member = memberOfPermutation[permutation];

            for (int i = 0; i < followers.length; i++) {
                followers[i].setPermutationToRender(followerStarts[i][member]);
            }
        }
    }
}
