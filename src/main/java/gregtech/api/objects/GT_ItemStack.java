package gregtech.api.objects;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.item.ItemHolder;

/**
 * An optimization of {@link ItemStack} to have a better {@code hashcode} and {@code equals} in order to improve
 * {@code HashMap} and {@code Set} performance
 */
public class GT_ItemStack extends ItemHolder {

    public final Item mItem;
    public final byte mStackSize;
    public final short mMetaData;

    public GT_ItemStack(Item aItem, long aStackSize, long aMetaData) {
        super(new ItemStack(aItem));
        mItem = aItem;
        mStackSize = (byte) aStackSize;
        mMetaData = (short) aMetaData;
    }

    public GT_ItemStack(ItemStack aStack) {
        this(aStack, false);
    }

    public GT_ItemStack(ItemStack aStack, boolean wildcard) {
        this(
            aStack == null ? null : aStack.getItem(),
            aStack == null ? 0 : aStack.stackSize,
            aStack == null ? 0 : wildcard ? GT_Values.W : Items.feather.getDamage(aStack));
    }

    public GT_ItemStack(int aHashCode) {
        this(GT_Utility.intToStack(aHashCode));
    }

    public final ItemStack toStack() {
        if (mItem == null) return null;
        return new ItemStack(mItem, 1, mMetaData);
    }

    public final boolean isStackEqual(ItemStack aStack) {
        return GT_Utility.areStacksEqual(toStack(), aStack);
    }

    public final boolean isStackEqual(GT_ItemStack aStack) {
        return GT_Utility.areStacksEqual(toStack(), aStack.toStack());
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
        return GT_Utility.stackToInt(toStack());
    }
}
