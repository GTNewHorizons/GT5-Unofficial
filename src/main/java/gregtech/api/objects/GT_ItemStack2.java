package gregtech.api.objects;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * GT_ItemStack, but with a better hashCode(). Due to this change, it should not be placed in the same hash based data
 * structure with GT_ItemStack. It also shouldn't be used to construct search query into a hash based data structure
 * that contains GT_ItemStack.
 *
 * @deprecated See {@link GT_ItemStack#ITEMSTACK_HASH_STRATEGY2}
 */
@Deprecated
public class GT_ItemStack2 extends GT_ItemStack {

    public GT_ItemStack2(Item aItem, long aStackSize, long aMetaData) {
        super(aItem, aStackSize, aMetaData);
    }

    public GT_ItemStack2(ItemStack aStack) {
        super(aStack);
    }

    public GT_ItemStack2(ItemStack aStack, boolean wildcard) {
        super(aStack, wildcard);
    }

    @Override
    public boolean equals(Object aStack) {
        if (aStack == this) return true;
        if (aStack instanceof GT_ItemStack) {
            return ((GT_ItemStack) aStack).mItem == mItem && ((GT_ItemStack) aStack).mMetaData == mMetaData;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return mItem.hashCode() * 38197 + mMetaData;
    }
}
