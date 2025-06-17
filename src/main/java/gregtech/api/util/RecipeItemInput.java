package gregtech.api.util;

import java.util.Objects;

import net.minecraft.item.ItemStack;

/** A single recipe input, used for an internal cache to speed up recipe matching */
public final class RecipeItemInput {

    /** Item count is ignored on this stack, do not mutate it either */
    public final ItemStack unifiedStack;
    /** Number of input items required */
    public long inputAmount;
    /** True if the input is NBT-sensitive */
    public final boolean usesNbtMatching;

    public RecipeItemInput(ItemStack stack, boolean recipeIsNBTSensitive) {
        Objects.requireNonNull(stack);
        this.inputAmount = stack.stackSize;
        final boolean stackNeedsNBT = GTRecipe.shouldCheckNBT(stack);
        this.usesNbtMatching = recipeIsNBTSensitive || stackNeedsNBT;
        if (stackNeedsNBT) {
            this.unifiedStack = stack;
        } else {
            this.unifiedStack = GTOreDictUnificator.get_nocopy(true, stack);
            if (!this.usesNbtMatching) {
                this.unifiedStack.setTagCompound(null);
            }
        }
    }

    /**
     * @return True if the passed in stack is of the same item type as this input (respecting
     * {@link RecipeItemInput#usesNbtMatching}).
     */
    public boolean matchesType(final ItemStack other) {
        return GTUtility.areStacksEqual(this.unifiedStack, other, !usesNbtMatching);
    }
}
