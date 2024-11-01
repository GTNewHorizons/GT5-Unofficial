package gregtech.common.items.matterManipulator;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

/**
 * Something that can provide items.
 */
public interface IItemProvider {

    /**
     * Gets a stack of the item provided by this provider.
     * The extraction must be atomic - if it fails, this method must not delete items.
     * 
     * @param inv
     * @param consume When false, no items will be extracted and the item will be provided as normal.
     * @return The item, or null if the pseudo inventory didn't have the required items.
     */
    public @Nullable ItemStack getStack(IPseudoInventory inv, boolean consume);
}
