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

/// Collapses the per-stone-variant ore recipes into one NEI entry per material and action. Recipes sharing a
/// [GTRecipeConstants#VARIANT_GROUP] tag and other inputs merge, and their stacks become permutations of one
/// displayed recipe. This is display only. The recipe map keeps every variant.
///
/// NEI cycles each input slot on its own and never cycles outputs, so a [Cycle] keeps every slot of the entry
/// on the variant the ore slot shows.
final class OreVariantGroups {

    private OreVariantGroups() {}

    /// Returns the recipes of `sorted` as displayed entries, in the same order, with each variant group replaced by
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

    /// Returns the key all recipes of one group share, or null for an untagged recipe. The key is the tag plus
    /// every input that is not a variant stack. Stack sizes are left out, since a richer stone multiplies both the
    /// ore and its reagents.
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

    /// Returns whether a stack is one of the ore blocks a variant group cycles through.
    private static boolean isVariantStack(ItemStack stack) {
        Block block = Block.getBlockFromItem(stack.getItem());
        return block != null && GTOreAdapter.INSTANCE.supports(block, stack.getItemDamage());
    }

    /// Returns the group's first member with every other member's stacks appended to its slots, or null when the
    /// members disagree on slot layout or have no ore slot that differs between them.
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

    /// Replaces the first member's slot contents with every member's, and returns where each member's run
    /// starts. Returns null and leaves the slot alone when all members hold the same stacks there.
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

        /// Pins every follower slot to the member the ore slot currently shows. A follower with fewer permutations
        /// advances on its own, so this needs a call before every read of the slots.
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
