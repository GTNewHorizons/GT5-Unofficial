package gregtech.common.items.matterManipulator;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

public class PortableItemStack {

    public UniqueIdentifier item;
    public Integer metadata, amount;

    public transient ItemStack itemStack;

    public PortableItemStack() {}

    public PortableItemStack(Item item, int metadata) {
        this.item = GameRegistry.findUniqueIdentifierFor(item);
        this.metadata = metadata == 0 ? null : metadata;
        this.amount = null;
    }

    public PortableItemStack(ItemStack stack) {
        item = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        metadata = Items.feather.getDamage(stack);
        if (metadata == 0) metadata = null;
        amount = stack.stackSize == 1 ? null : stack.stackSize;
    }

    public static PortableItemStack withoutStackSize(ItemStack stack) {
        stack = stack.copy();
        stack.stackSize = 1;
        return new PortableItemStack(stack);
    }

    public ItemStack toStack() {
        if (itemStack == null) {
            itemStack = new ItemStack(
                GameRegistry.findItem(item.modId, item.name),
                amount == null ? 1 : amount,
                metadata == null ? 0 : metadata);
        }

        return itemStack;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((item == null) ? 0 : item.hashCode());
        result = prime * result + ((metadata == null) ? 0 : metadata.hashCode());
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((itemStack == null) ? 0 : itemStack.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PortableItemStack other = (PortableItemStack) obj;
        if (item == null) {
            if (other.item != null) return false;
        } else if (!item.equals(other.item)) return false;
        if (metadata == null) {
            if (other.metadata != null) return false;
        } else if (!metadata.equals(other.metadata)) return false;
        if (amount == null) {
            if (other.amount != null) return false;
        } else if (!amount.equals(other.amount)) return false;
        if (itemStack == null) {
            if (other.itemStack != null) return false;
        } else if (!itemStack.equals(other.itemStack)) return false;
        return true;
    }
}
