package gregtech.api.interfaces.metatileentity;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

/**
 * Implement this interface if your MetaTileEntity supports item locking.
 */
public interface IItemLockable {

    /**
     * Set the locked item.
     * <p>
     * Implementers should make a copy of this item, as it can be either a physical item or a ghost item dragged from
     * NEI.
     *
     * @param itemStack An item stack to lock
     * @see com.gtnewhorizons.modularui.api.forge.ItemHandlerHelper#copyStackWithSize(ItemStack, int)
     */
    void setLockedItem(@Nullable ItemStack itemStack);

    /**
     * Get the locked item.
     *
     * @return an ItemStack of the locked item. Returns null if there is no locked item.
     */
    @Nullable
    ItemStack getLockedItem();

    /**
     * Clears the lock on the machine.
     */
    void clearLock();

    boolean isLocked();

    default boolean acceptsItemLock() {
        return false;
    }
}
