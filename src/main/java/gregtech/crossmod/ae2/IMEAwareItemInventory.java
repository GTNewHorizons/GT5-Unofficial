package gregtech.crossmod.ae2;

import net.minecraft.item.ItemStack;

/**
 * Implement this interface for MetaTileEntity that supports {@link appeng.api.storage.IMEMonitor} to use
 * {@link MEItemInventoryHandler} for proxying methods.
 */
public interface IMEAwareItemInventory {

    ItemStack getItemStack();

    void setItemStack(ItemStack stack);

    int getItemCount();

    void setItemCount(int count);

    int getItemCapacity();

    /**
     * Output slot etc.
     */
    ItemStack getExtraItemStack();

    void setExtraItemStack(ItemStack stack);

    default boolean isValidItem(ItemStack item) {
        return true;
    }
}
