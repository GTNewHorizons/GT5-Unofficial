package gregtech.api.objects;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.util.GTUtility;
import gregtech.api.util.item.ItemHolder;
import it.unimi.dsi.fastutil.Hash;

/**
 * An optimization of {@link ItemStack} to have a better {@code hashcode} and {@code equals} in order to improve
 * {@code HashMap} and {@code Set} performance
 */
public class GTItemStack extends ItemHolder {

    /**
     * A better {@link Hash.Strategy} for {@link ItemStack}. Implementation originally from {@code GT_ItemStack2}.
     */
    public static final Hash.Strategy<ItemStack> ITEMSTACK_HASH_STRATEGY2 = new Hash.Strategy<>() {

        @Override
        public int hashCode(ItemStack o) {
            return o.getItem()
                .hashCode() * 38197 + Items.feather.getDamage(o);
        }

        @Override
        public boolean equals(ItemStack a, ItemStack b) {
            if (a == b) return true;
            if (a == null || b == null) return false;
            return a.getItem() == b.getItem() && Items.feather.getDamage(a) == Items.feather.getDamage(b);
        }
    };

    public final Item mItem;
    public final byte mStackSize;
    public final short mMetaData;

    public GTItemStack(Item aItem, long aStackSize, long aMetaData) {
        super(new ItemStack(aItem, 1, (int) aMetaData));
        mItem = aItem;
        mStackSize = (byte) aStackSize;
        mMetaData = (short) aMetaData;
    }

    public GTItemStack(ItemStack aStack) {
        this(aStack, false);
    }

    public GTItemStack(ItemStack aStack, boolean wildcard) {
        this(
            aStack == null ? null : aStack.getItem(),
            aStack == null ? 0 : aStack.stackSize,
            aStack == null ? 0 : wildcard ? GTValues.W : Items.feather.getDamage(aStack));
    }

    public GTItemStack(int aHashCode) {
        this(GTUtility.intToStack(aHashCode));
    }

    public final ItemStack toStack() {
        if (mItem == null) return null;
        return new ItemStack(mItem, 1, mMetaData);
    }

    public final boolean isStackEqual(ItemStack aStack) {
        return GTUtility.areStacksEqual(toStack(), aStack);
    }

    public final boolean isStackEqual(GTItemStack aStack) {
        return GTUtility.areStacksEqual(toStack(), aStack.toStack());
    }

    @Override
    public boolean equals(Object aStack) {
        if (aStack == this) return true;
        if (aStack instanceof GTItemStack) {
            return ((GTItemStack) aStack).mItem == mItem && ((GTItemStack) aStack).mMetaData == mMetaData;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return GTUtility.stackToInt(toStack());
    }

    /**
     * @see #internalCopyStack(ItemStack, boolean)
     */
    public static ItemStack internalCopyStack(ItemStack aStack) {
        return internalCopyStack(aStack, false);
    }

    /**
     * Replicates the copy behavior of {@link #toStack()} but for normal {@link ItemStack}s.
     *
     * @param aStack   the stack to copy
     * @param wildcard whether to use wildcard damage value
     * @return a copy of the stack with stack size 1 and no NBT
     */
    public static ItemStack internalCopyStack(ItemStack aStack, boolean wildcard) {
        return new ItemStack(aStack.getItem(), 1, wildcard ? GTValues.W : Items.feather.getDamage(aStack));
    }
}
