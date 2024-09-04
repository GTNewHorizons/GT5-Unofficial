package tectech.util;

import net.minecraft.item.ItemStack;

public class ItemStackLong {

    public final ItemStack itemStack;
    public long stackSize;

    public ItemStackLong(ItemStack itemStack, long stackSize) {
        this.itemStack = itemStack;
        this.stackSize = stackSize;
    }

    // Copy constructor.
    public ItemStackLong(ItemStackLong itemStackLong) {
        this.itemStack = itemStackLong.itemStack;
        this.stackSize = itemStackLong.stackSize;
    }

    public long getStackSize() {
        return stackSize;
    }

    public long compareTo(ItemStackLong itemStackLong) {
        return (stackSize - itemStackLong.stackSize);
    }
}
