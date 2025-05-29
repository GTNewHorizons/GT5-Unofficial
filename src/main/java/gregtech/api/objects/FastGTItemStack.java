package gregtech.api.objects;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.util.GTUtility;

/**
 * A faster low-allocation and mutable version of {@link GTItemStack}. This collection is only meant to be used to fetch
 * values from a Map. It is incredibly unsafe and should not be used to actually store anything, since it's mutable. As
 * such, it will never equal another {@link FastGTItemStack}. It should only be compared against {@link GTItemStack}s.
 *
 * This extends {@link GTItemStack} only so that it can be used in maps that require a {@link GTItemStack} specifically.
 * The {@link GTItemStack} fields won't be initialized properly, and the methods won't work either.
 */
public class FastGTItemStack extends GTItemStack {

    public Item item;
    public int metadata;

    public FastGTItemStack(Item item, int metadata) {
        super(null, 0);
        this.item = item;
        this.metadata = metadata;
    }

    public FastGTItemStack(ItemStack stack) {
        this(stack, false);
    }

    public FastGTItemStack(ItemStack stack, boolean wildcard) {
        this(stack.getItem(), wildcard ? GTValues.W : stack.itemDamage);
    }

    public FastGTItemStack(int hash) {
        this(Item.getItemById(hash & (~0 >>> 16)), hash >>> 16);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other instanceof GTItemStack stack) {
            return stack.mItem == item && stack.mMetaData == metadata;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return GTUtility.itemToInt(item, metadata);
    }
}
